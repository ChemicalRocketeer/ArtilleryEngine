package hellomisterme.artillery_engine;

import hellomisterme.artillery_engine.io.Keyboard;
import hellomisterme.artillery_engine.io.Savegame;
import hellomisterme.artillery_engine.io.Screenshot;
import hellomisterme.artillery_engine.rendering.Render;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
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
@SuppressWarnings("serial")
public class Game extends Canvas implements Runnable {

	private final String title = "Space Game Alpha.0.1.2";

	/**
	 * A random number generator that can be used by objects in the game
	 */
	public static final Random RAND = new Random((long) Math.toDegrees(System.currentTimeMillis() << System.nanoTime()));

	private static double aspectRatio = 9.0 / 16.0;
	private static int width = 1920;
	public static final int TICKS_PER_SECOND = 60;
	private boolean running = false;
	private boolean paused = false;

	private JFrame frame;
	private static Render render;
	private static World world;
	private static GameLog log;

	private boolean devMode = false;
	private DevInfo devInfo;

	// control booleans TODO put these in a different class or something, I don't think they really belong here...
	private boolean devModeOrdered = false;
	private boolean pauseOrdered = false;
	private boolean screenshotOrdered = false;
	private boolean ioOrdered = false;
	private boolean fullscreen = true;
	private boolean fullscreenOrdered = true;

	public Game() {
		frame = new JFrame();
		frame.add(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setTitle(title);
		setupGraphics();

		addKeyListener(new Keyboard());
		devInfo = new DevInfo();
		DevInfo.setup(render.graphics);
		Screenshot.readScreenshotNumber();

		run();
	}

	/**
	 * This is it. The game loop. If something happens in the game, it begins here.
	 * 
	 * TODO make all the extra frames actually do something useful (so, tweening)
	 * 
	 * Called by this Game's Thread thread.
	 */
	@Override
	public void run() {
		world = new World(getWidth(), getHeight());
		world.init();

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

			// keep track of stats every second and send to titlebar if devmode is on
			if (System.currentTimeMillis() >= lastRecord + 1000) {
				totalFrames += frameCount;
				totalSeconds++;

				// keep dev info updated
				devInfo.maxMem = Runtime.getRuntime().maxMemory();
				devInfo.usedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
				devInfo.fps = frameCount;
				System.out.println(devInfo.fps);
				devInfo.avg = (int) (totalFrames / totalSeconds);
				devInfo.tps = tickCount;
				devInfo.sec = totalSeconds;

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
		render.clear();
		world.render(render);

		if (devMode) {
			// world.player.getVelocity().draw(render, 8, world.player.getIntX(), world.player.getIntY());
			devInfo.render(render);
		}

		BufferStrategy strategy = getBufferStrategy(); // this Game's BufferStrategy
		Graphics g = strategy.getDrawGraphics(); // get the next Graphics object from the strategy
		g.drawImage(render.image, 0, 0, getWidth(), getHeight(), null); // draw the rendered image onto the Graphics object
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
	 * Checks certain large-scale state conditions like screenshots and devmode
	 */
	private void checkStatus() {
		if (Keyboard.Controls.SCREENSHOT.pressed()) {
			if (!screenshotOrdered) { // if the screenshot key was up before
				new Screenshot(render.image);
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
		// if an io key is pressed
		if (Keyboard.Controls.SAVE.pressed()) {
			if (!ioOrdered) { // if an io key was up before
				new Savegame().saveData(world, "quicksave");
				ioOrdered = true; // remember that io was ordered
			}
		} else if (Keyboard.Controls.LOAD.pressed()) {
			if (!ioOrdered) { // if an io key was up before
				new Savegame().loadData(world, "quicksave");
				ioOrdered = true; // remember that io was ordered
			}
		} else { // io keys not pressed
			ioOrdered = false;
		}

		if (Keyboard.Controls.DEVMODE.pressed()) {
			if (!devModeOrdered) {
				devMode = !devMode;
				render.simpleRendering = devMode;
				devModeOrdered = true;
			}
		} else {
			devModeOrdered = false;
		}

		// TODO fix bug where key is still thought to be pressed on transition from fullscreen to windowed
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
			width = 1920;
		} else {
			width = 800;
		}
		setPreferredSize(new Dimension(getWidth(), getHeight()));
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

		createBufferStrategy(3);

		if (render != null) render.dispose();
		render = new Render(getWidth(), getHeight());
	}

	@Override
	public int getWidth() {
		return width;
	}

	public static int getDisplayWidth() {
		return render.getWidth();
	}

	@Override
	public int getHeight() {
		return (int) (width * aspectRatio);
	}

	public static int getDisplayHeight() {
		return render.getHeight();
	}

	public void pause() {
		paused = true;
	}

	public synchronized void stop() {
		running = false;
	}

	public static World getWorld() {
		return world;
	}

	public static GameLog getLog() {
		return log;
	}
}
