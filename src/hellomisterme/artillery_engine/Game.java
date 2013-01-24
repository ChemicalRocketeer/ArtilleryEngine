package hellomisterme.artillery_engine;

import hellomisterme.artillery_engine.graphics.Render;
import hellomisterme.artillery_engine.io.Keyboard;
import hellomisterme.artillery_engine.io.Savegame;
import hellomisterme.artillery_engine.io.Screenshot;
import hellomisterme.artillery_engine.world.World;
import hellomisterme.artillery_engine.world.testWorld;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Random;

/**
 * The Game handles display and management of the game loop.
 * 
 * @since 10-14-12
 * @author David Aaron Suddjian
 */
@SuppressWarnings("serial")
public class Game extends Canvas implements Runnable {

	/**
	 * This game's title
	 */
	public static String title = "Artillery Engine Pre-Alpha.0.06.2";

	/**
	 * A height to width ratio that objects can use for image transformations or for movement speed
	 */
	public static final double ISOMETRIC_RATIO = .5;

	/**
	 * A random number that can be used by objects in the game
	 */
	public static final Random RAND = new Random(System.currentTimeMillis());

	/**
	 * The width of the game window
	 */
	public static int width = 800;

	/**
	 * The height of the game window
	 */
	public static int height = width * 10 / 16;

	/**
	 * The game's tick frequency, determining how quickly ingame actions happen
	 */
	public static final int TICKS_PER_SECOND = 60;

	private boolean running = false;

	private BufferedImage image;
	private Render render;

	private static World world;

	private DevInfo devInfo;

	// control booleans TODO put these in a different class or something, I don't think they really belong here...
	private boolean devMode = false;
	private boolean devModeOrdered = false;
	private boolean screenshotOrdered = false;
	private boolean ioOrdered = false;

	public Game() {
		setPreferredSize(new Dimension(width, height));
	}

	/**
	 * This is it. The game loop. If something happens in the game, it begins here.
	 * 
	 * TODO make all the extra frames actually do something useful (so, tweening)
	 * 
	 * Called by this Game's Thread thread.
	 */
	public void run() {
		running = true;

		long totalFrames = 0; // the total number of frames generated (never gets decremented, so it's a long)
		int totalSeconds = 0; // the number of times totalFrames has been updated (never gets decremented, so it's a long)
		int frameCount = 0; // FPS counter variable
		int tickCount = 0; // TPS counter variable
		long lastRecord = System.currentTimeMillis(); // the last time frameCount and tickCount were written to console

		// variables to regulate tick frequency
		double ns = 1000000000.0 / (double) TICKS_PER_SECOND; // time between ticks
		double delta = 1; // difference between now and the last tick
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
	 * Calls everybody's tick() methods, and checks for over-all game-related events, like screenshot key presses
	 */
	private void tick() {
		checkStatus();
		world.tick();
		world.callTick();
	}

	/**
	 * Checks certain large-scale state conditions like screenshots and devmode
	 */
	private void checkStatus() {
		// if the screenshot key is pressed
		if (Keyboard.pressed(Keyboard.screenshot)) {
			if (!screenshotOrdered) { // if the screenshot key was up before
				new Screenshot(image);
				screenshotOrdered = true; // remember that screenshot was pressed
			}
		} else { // screenshot key not pressed
			screenshotOrdered = false;
		}

		// these if statements are organized to prevent save/load operations in the same tick
		// if an io key is pressed
		if (Keyboard.pressed(Keyboard.save)) {
			if (!ioOrdered) { // if an io key was up before
				new Savegame().saveData(world, "quicksave");
				ioOrdered = true; // remember that io was ordered
			}
		} else if (Keyboard.pressed(Keyboard.load)) {
			if (!ioOrdered) { // if an io key was up before
				new Savegame().loadData(world, "quicksave");
				ioOrdered = true; // remember that io was ordered
			}
		} else { // io keys not pressed
			ioOrdered = false;
		}

		// if devmode is being activated/deactivated
		if (Keyboard.pressed(Keyboard.devmode)) {
			if (!devModeOrdered) {
				devMode = !devMode; // toggle
				devModeOrdered = true;
			}
		} else {
			devModeOrdered = false;
		}
	}

	/**
	 * Draws the current frame
	 */
	private void render() {
		BufferStrategy strategy = getBufferStrategy(); // this Game's BufferStrategy
		Graphics g = strategy.getDrawGraphics(); // get the next Graphics object from the strategy
		
		render.clear();

		world.render(render);

		if (devMode) devInfo.render(image.createGraphics());
		
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null); // draw the rendered image onto the Graphics object
		g.dispose(); // let go of the Graphics object
		strategy.show(); // have the strategy do its thing
	}

	/**
	 * Prepares game assets
	 * 
	 * TODO add loading bar and/or do some of this in a different thread and stuff
	 */
	public void setup() {
		createBufferStrategy(3); // set up the buffer strategy for rendering

		// initialize visual elements
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); // this is what gets drawn onto the buffer strategy
		render = new Render(width, height);
		render.pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData(); // link the image's data with render

		world = new testWorld(width, height);
		addKeyListener(new Keyboard());
		devInfo = new DevInfo(getBufferStrategy().getDrawGraphics());
		Screenshot.readScreenshotNumber();
	}

	/**
	 * Stops running the game
	 */
	public synchronized void stop() {
		running = false;
	}

	public static World getWorld() {
		return world;
	}
}
