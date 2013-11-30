package hellomisterme.artillery_engine;

import hellomisterme.artillery_engine.controls.FunctionController;
import hellomisterme.artillery_engine.controls.Keyboard;
import hellomisterme.artillery_engine.geometry.AABB;
import hellomisterme.artillery_engine.io.ArteWriter;
import hellomisterme.artillery_engine.rendering.Render;
import hellomisterme.artillery_engine.rendering.Screen;
import hellomisterme.artillery_engine.rendering.SpriteSheet;
import hellomisterme.artillery_engine.rendering.Texture;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferStrategy;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

/**
 * The Game handles display and management of the currentGameInstance loop.
 * 
 * @since 10-14-12
 * @author David Aaron Suddjian
 */
public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;

	public static final String VERSION = "Alpha.0.1.2";
	public String title = "Space Game " + VERSION;

	/** A random number generator that can be used by objects in the currentGameInstance */
	public static final Random RAND = new Random((long) Math.toDegrees(System.currentTimeMillis() << System.nanoTime()));

	public static final int TICKS_PER_SECOND = 60;

	private static Game currentGameInstance; // the current Game instance, so objects can find it easily
	private Render render;
	private static SpriteSheet spritesheet;
	private World world;
	private GameLog log;
	private DevInfo devInfo;
	private ContextManager contextManager;

	// 'state of game' flags
	private boolean running = false;
	private boolean paused = false;
	private boolean devModeEnabled = true;

	// state controls
	private List<FunctionController> gameStateControls;
	private boolean devModeOrdered = false;
	private boolean pauseOrdered = false;
	private boolean screenshotOrdered = false;
	private boolean ioOrdered = false;

	public Game(World w, ContextManager contextManager) {
		currentGameInstance = this;
		if (contextManager == null)
			contextManager = new DefaultContextManager(this);
		this.contextManager = contextManager;
		this.world = w;
		spritesheet = new SpriteSheet();
		devInfo = new DevInfo();
		addKeyListener(new Keyboard());

		gameStateControls = new LinkedList<>();
		gameStateControls.add(new FunctionController(Keyboard.Control.FULLSCREEN, new FunctionController.ControlResponder() {
			public void respondToControl(FunctionController controller) {
				toggleFullscreen();
			}
		}));
		gameStateControls.add(new FunctionController(Keyboard.Control.SCREENSHOT, new FunctionController.ControlResponder() {
			public void respondToControl(FunctionController controller) {
				render.screen.screenshot();
			}
		}));
		gameStateControls.add(new FunctionController(Keyboard.Control.PAUSE, new FunctionController.ControlResponder() {
			public void respondToControl(FunctionController controller) {
				paused = !paused;
			}
		}));
		gameStateControls.add(new FunctionController(Keyboard.Control.DEVMODE, new FunctionController.ControlResponder() {
			public void respondToControl(FunctionController controller) {
				setDevMode(!isDevModeEnabled());
				render.screen.simpleRendering = isDevModeEnabled();
			}
		}));
		gameStateControls.add(new FunctionController(Keyboard.Control.SAVE, new FunctionController.ControlResponder() {
			public void respondToControl(FunctionController controller) {
				// new ArteWriter("saves/quicksave");
			}
		}));
		gameStateControls.add(new FunctionController(Keyboard.Control.LOAD, new FunctionController.ControlResponder() {
			public void respondToControl(FunctionController controller) {
				// new ArteWriter("quicksave.arte");
			}
		}));

		world.init();

		setupGraphics(); // screen and render are now initialized
		world.devmodeRender(render);
		devInfo.devmodeRender(render);
		render.screen.clear();
	}

	/**
	 * This is it. The currentGameInstance loop. If something happens in the currentGameInstance, it begins here.
	 */
	@Override
	public void run() {
		running = true;

		long totalFrames = 0; // the total number of frames generated (never gets decremented, so it's a long)
		int totalSeconds = 0; // the number of times totalFrames has been updated (never gets decremented, so it's a long)
		int frameCount = 0; // FPS counter variable
		int tickCount = 0; // TPS counter variable
		long lastRecord = System.currentTimeMillis(); // the last time frameCount and tickCount were written to console

		// variables to regulate tick frequency
		double ns = 1000000000.0 / TICKS_PER_SECOND; // time between ticks
		double delta = 1; // difference between now and the last tick (1 so that it starts immediately)
		long lastTime = System.nanoTime();

		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;

			// run the tick cycle on time
			while (delta >= 1) {
				tick();
				delta--;
				tickCount++;
			}

			render();
			frameCount++;

			// every second
			if (System.currentTimeMillis() >= lastRecord + 1000) {
				totalFrames += frameCount;
				totalSeconds++;

				// keep dev info updated
				devInfo.fps = frameCount;
				devInfo.avg = (int) (totalFrames / totalSeconds);
				devInfo.tps = tickCount;
				devInfo.sec = totalSeconds;
				devInfo.totalMemory = Runtime.getRuntime().totalMemory() / 1048576; // 1048576 bytes in a MB
				devInfo.usedMemory = devInfo.totalMemory - Runtime.getRuntime().freeMemory() / 1048576;

				// reset variables
				frameCount = 0;
				tickCount = 0;
				lastRecord = System.currentTimeMillis();
			}
		}
	}

	/**
	 * Draws the current frame
	 */
	private void render() {
		render.screen.clear();
		render.setCamera(world.getCamera());
		world.render(render);
		if (isDevModeEnabled()) {
			world.devmodeRender(render);
			devInfo.devmodeRender(render);
			if (paused) {
				Graphics2D g = render.screen.getGraphics();
				g.drawString("paused", (int) (getWidth() - 50), 15);
				g.dispose();
			}
		}

		render.textureArea(new Texture("graphics/backgrounds/tree.png", 20, 50), new AABB(100, 950, 100, 850));

		BufferStrategy strategy = getBufferStrategy(); // this Game's BufferStrategy
		Graphics2D g = (Graphics2D) strategy.getDrawGraphics(); // get the next Graphics object from the strategy
		render.screen.drawScreen(g); // draw the rendered image onto the Graphics object
		g.dispose(); // let go of the Graphics object
		strategy.show(); // have the strategy do its thing
	}

	/**
	 * Calls everybody's tick() methods, and checks for over-all currentGameInstance-related events, like screenshot key presses
	 */
	private void tick() {
		checkStatus();
		if (!paused) {
			world.tick();
		}
	}

	/**
	 * Checks state conditions like screenshots and devmode
	 */
	private void checkStatus() {

		for (FunctionController control : gameStateControls) {
			control.tick();
		}

		if (Keyboard.Control.SCREENSHOT.pressed()) {
			if (!screenshotOrdered) { // if the screenshot key was up before
				render.screen.screenshot();
				screenshotOrdered = true; // remember that screenshot was pressed
			}
		} else { // screenshot key not pressed
			screenshotOrdered = false;
		}

		if (Keyboard.Control.PAUSE.pressed()) {
			if (!pauseOrdered) { // if the pause key was up before
				paused = !paused;
				pauseOrdered = true; // remember that pause was pressed
			}
		} else { // screenshot key not pressed
			pauseOrdered = false;
		}

		// these if statements are organized to prevent save/load operations in the same tick
		if (Keyboard.Control.SAVE.pressed()) { // if an io key is pressed
			if (!ioOrdered) { // if an io key was up before
				// new ArteWriter("saves/quicksave");
				ioOrdered = true; // remember that io was ordered
			}
		} else if (Keyboard.Control.LOAD.pressed()) {
			if (!ioOrdered) { // if an io key was up before
				// new ArteWriter("quicksave.arte");
				ioOrdered = true; // remember that io was ordered
			}
		} else { // io keys not pressed
			ioOrdered = false;
		}

		if (Keyboard.Control.DEVMODE.pressed()) {
			if (!devModeOrdered) {
				setDevMode(!isDevModeEnabled());
				render.screen.simpleRendering = isDevModeEnabled();
				devModeOrdered = true;
			}
		} else {
			devModeOrdered = false;
		}

		/*
		 * if (!Keyboard.Control.FULLSCREEN.pressed()) {
		 * if (!fullscreenOrdered) {
		 * if (!contextManager.isFullscreen()) {
		 * contextManager.goFullscreen();
		 * } else {
		 * contextManager.goWindowed();
		 * }
		 * fullscreenOrdered = true;
		 * setupGraphics();
		 * }
		 * } else {
		 * fullscreenOrdered = false;
		 * }
		 */
	}

	public void toggleFullscreen() {
		if (!contextManager.isFullscreen()) {
			contextManager.goFullscreen();
		} else {
			contextManager.goWindowed();
		}
		setupGraphics();
	}

	private void setupGraphics() {
		this.createBufferStrategy(3);
		render = new Render(new Screen(getWidth(), getHeight()));
		requestFocus();
	}

	public void save(ArteWriter aw) {

	}

	public void pause() {
		paused = true;
	}

	public void unpause() {
		paused = false;
	}

	public boolean isRunning() {
		return running;
	}

	public synchronized void stop() {
		running = false;
	}

	public boolean isDevModeEnabled() {
		return devModeEnabled;
	}

	public void setDevMode(boolean devMode) {
		this.devModeEnabled = devMode;
	}

	public World getWorld() {
		return world;
	}

	public static SpriteSheet getSpriteSheet() {
		return spritesheet;
	}

	public GameLog getLog() {
		return log;
	}

	public static Game currentInstance() {
		return currentGameInstance;
	}

	/**
	 * A ContextManager is an interface between the Game and the space where the game is running.
	 * The game could be running in an Applet or a JFrame or as a part of a whole other program, but the Game doesn't know the difference.
	 */
	public static interface ContextManager {
		public boolean isFullscreen();

		/**
		 * Switches the context to fullscreen mode.
		 * 
		 * @return a Dimension representing the Game's new size on the screen
		 */
		public Dimension goFullscreen();

		/**
		 * Switches the context to windowed mode.
		 * 
		 * @return a Dimension representing the Game's new size on the screen
		 */
		public Dimension goWindowed();

		public Dimension getSize();

		public int getWidth();

		public int getHeight();

		public void setTitle(String title);

		public String getTitle();
	}

	/**
	 * The default ContextManager, used if a Game is made without a ContextManager specified.
	 * Creates a JFrame and puts the Game in it.
	 */
	private static class DefaultContextManager implements ContextManager {

		private Game game;
		private JFrame frame;
		public final Dimension DEFAULTSIZE = new Dimension(800, (int) (800 * 9.0 / 16.0));
		private Dimension size;
		private boolean fullscreen = true;
		private String title;

		public DefaultContextManager(Game g) {
			frame = new JFrame();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setResizable(false);
			frame.setTitle(title);
			frame.add(g);
			game = g;

			if (fullscreen)
				goFullscreen();
			else
				goWindowed();
		}

		@Override
		public Dimension goFullscreen() {
			DisplayMode dm = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
			size = new Dimension(dm.getWidth(), dm.getHeight());
			game.setPreferredSize(size);
			frame.dispose();
			frame.setUndecorated(true);
			GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(frame);
			game.requestFocus();
			fullscreen = true;
			return new Dimension(size);
		}

		@Override
		public Dimension goWindowed() {
			size = new Dimension(DEFAULTSIZE);
			game.setPreferredSize(size);
			frame.dispose();
			frame.setUndecorated(false);
			frame.pack(); // automatically set window size
			frame.setLocationRelativeTo(null); // center
			frame.setVisible(true);
			game.requestFocus();
			fullscreen = false;
			return new Dimension(size);
		}

		public Dimension getSize() {
			return new Dimension(size);
		}

		@Override
		public void setTitle(String title) {
			frame.setTitle(title);
			this.title = title;
		}

		@Override
		public String getTitle() {
			return title;
		}

		@Override
		public boolean isFullscreen() {
			return fullscreen;
		}

		@Override
		public int getWidth() {
			return size.width;
		}

		@Override
		public int getHeight() {
			return size.height;
		}
	}

	public static void main(String[] args) {
		Thread thread = new Thread(new Game(new World(), null));
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			Err.error("Can't stop the game thread!", e);
		}
	}
}
