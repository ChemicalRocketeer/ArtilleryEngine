package hellomisterme.artillery_engine;

import hellomisterme.artillery_engine.io.ArteWriter;
import hellomisterme.artillery_engine.io.Keyboard;
import hellomisterme.artillery_engine.rendering.Render;
import hellomisterme.artillery_engine.rendering.Screen;
import hellomisterme.artillery_engine.rendering.SpriteSheet;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferStrategy;
import java.util.Random;

import javax.swing.JFrame;

/**
 * The Game handles display and management of the game loop.
 * 
 * @since 10-14-12
 * @author David Aaron Suddjian
 */
public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;
	
	public static final String VERSION = "Alpha.0.1.2";
	public String title = "Space Game " + VERSION;
	
	/** A random number generator that can be used by objects in the game */
	public static final Random RAND = new Random((long) Math.toDegrees(System.currentTimeMillis() << System.nanoTime()));
	
	private double aspectRatio = 9.0 / 16.0;
	private int defaultWidth = 800;
	private int width = defaultWidth;
	private int height = (int) (width * aspectRatio);
	
	public static final int TICKS_PER_SECOND = 60;
	
	private JFrame frame;
	private static Render render;
	private static SpriteSheet spritesheet;
	private static World world;
	private static GameLog log;
	private DevInfo devInfo;
	
	private boolean running = false;
	private boolean paused = false;
	private boolean fullscreen = false;
	private boolean devModeEnabled = true;
	
	private boolean devModeOrdered = false;
	private boolean pauseOrdered = false;
	private boolean screenshotOrdered = false;
	private boolean ioOrdered = false;
	private boolean fullscreenOrdered = true;
	
	public Game() {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setTitle(title);
		frame.add(this);
		setupGraphics(); // screen and render are now initialized
		
		addKeyListener(new Keyboard());
		devInfo = new DevInfo();
		spritesheet = new SpriteSheet();
		world = new World(5000, 5000);
		world.init();
		
		world.devmodeRender(render);
		devInfo.devmodeRender(render);
		render.screen.clear();
	}

	/**
	 * This is it. The game loop. If something happens in the game, it begins here.
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
				// Behavior.out.println(devInfo.fps);

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
				g.drawString("paused", width - 50, 15);
				g.dispose();
			}
		}

		BufferStrategy strategy = getBufferStrategy(); // this Game's BufferStrategy
		Graphics2D g = (Graphics2D) strategy.getDrawGraphics(); // get the next Graphics object from the strategy
		render.screen.drawScreen(g); // draw the rendered image onto the Graphics object
		g.dispose(); // let go of the Graphics object
		strategy.show(); // have the strategy do its thing
	}
	
	/**
	 * Calls everybody's tick() methods, and checks for over-all game-related events, like screenshot key presses
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
		if (Keyboard.Controls.SCREENSHOT.pressed()) {
			if (!screenshotOrdered) { // if the screenshot key was up before
				render.screen.screenshot();
				screenshotOrdered = true; // remember that screenshot was pressed
			}
		} else { // screenshot key not pressed
			screenshotOrdered = false;
		}
		
		if (Keyboard.Controls.PAUSE.pressed()) {
			if (!pauseOrdered) { // if the pause key was up before
				paused = !paused;
				pauseOrdered = true; // remember that pause was pressed
			}
		} else { // screenshot key not pressed
			pauseOrdered = false;
		}
		
		// these if statements are organized to prevent save/load operations in the same tick
		if (Keyboard.Controls.SAVE.pressed()) { // if an io key is pressed
			if (!ioOrdered) { // if an io key was up before
				// new ArteWriter("saves/quicksave");
				ioOrdered = true; // remember that io was ordered
			}
		} else if (Keyboard.Controls.LOAD.pressed()) {
			if (!ioOrdered) { // if an io key was up before
				// new ArteWriter("quicksave.arte");
				ioOrdered = true; // remember that io was ordered
			}
		} else { // io keys not pressed
			ioOrdered = false;
		}
		
		if (Keyboard.Controls.DEVMODE.pressed()) {
			if (!devModeOrdered) {
				setDevMode(!isDevModeEnabled());
				render.screen.simpleRendering = isDevModeEnabled();
				devModeOrdered = true;
			}
		} else {
			devModeOrdered = false;
		}
		
		if (!Keyboard.Controls.FULLSCREEN.pressed()) {
			if (!fullscreenOrdered) {
				fullscreen = !fullscreen; // toggle
				fullscreenOrdered = true;
				setupGraphics();
			}
		} else {
			fullscreenOrdered = false;
		}
	}
	
	private void setupGraphics() {
		if (fullscreen) {
			DisplayMode dm = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
			width = dm.getWidth();
			height = dm.getHeight();
		} else {
			width = defaultWidth;
			height = (int) (width * aspectRatio);
		}
		setPreferredSize(new Dimension(width, height));
		frame.dispose();
		if (fullscreen) {
			frame.setUndecorated(true);
			GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(frame);
		} else {
			frame.setUndecorated(false);
			frame.pack(); // automatically set window size
			frame.setLocationRelativeTo(null); // center
			frame.setVisible(true);
		}
		requestFocus();
		this.createBufferStrategy(3);
		
		render = new Render(new Screen(width, height));
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
	
	public static World getWorld() {
		return world;
	}
	
	public static SpriteSheet getSpriteSheet() {
		return spritesheet;
	}
	
	public static GameLog getLog() {
		return log;
	}
}
