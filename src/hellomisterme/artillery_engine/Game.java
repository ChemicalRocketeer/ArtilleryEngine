package hellomisterme.artillery_engine;

import hellomisterme.artillery_engine.graphics.Render;
import hellomisterme.artillery_engine.io.Keyboard;
import hellomisterme.artillery_engine.io.Savegame;
import hellomisterme.artillery_engine.io.Screenshot;
import hellomisterme.artillery_engine.world.World;
import hellomisterme.artillery_engine.world.testWorld;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
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

	/**
	 * This game's title
	 */
	private final String title = "Artillery Engine Pre-Alpha.0.06.2";

	/**
	 * A random number that can be used by objects in the game
	 */
	public static final Random RAND = new Random((long) Math.toDegrees(System.currentTimeMillis() << System.nanoTime()));

	private static double aspectRatio = 9.0 / 16.0;
	private static int width = 1920;
	private static int height = (int) (width * aspectRatio);

	/**
	 * The game's tick frequency, determining how quickly ingame actions happen
	 */
	public static final int TICKS_PER_SECOND = 60;

	private boolean running = false;

	private JFrame frame;

	private BufferedImage image;
	private Graphics2D graphics;
	private Render render;
	private RenderingHints renderHints;

	private static World world;

	private DevInfo devInfo;

	// control booleans TODO put these in a different class or something, I don't think they really belong here...
	private boolean devMode = false;
	private boolean devModeOrdered = false;
	private boolean screenshotOrdered = false;
	private boolean ioOrdered = false;
	private boolean fullscreen = false;
	private boolean fullscreenOrdered = false;

	public Game() {
		// TODO read rendering hints from settings file and make them customizable
		renderHints = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		renderHints.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		renderHints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		renderHints.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		renderHints.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		renderHints.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		renderHints.put(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

		frame = new JFrame();
		frame.add(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setTitle(title);
		setupGraphics();

		addKeyListener(new Keyboard());
		devInfo = new DevInfo();
		DevInfo.setup(graphics);
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
		world = new testWorld(width, height);

		running = true;

		long totalFrames = 0; // the total number of frames generated (never gets decremented, so it's a long)
		int totalSeconds = 0; // the number of times totalFrames has been updated (never gets decremented, so it's a long)
		int frameCount = 0; // FPS counter variable
		int tickCount = 0; // TPS counter variable
		long lastRecord = System.currentTimeMillis(); // the last time frameCount and tickCount were written to console

		// variables to regulate tick frequency
		double ns = 1000000000.0 / TICKS_PER_SECOND; // time between ticks
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
	 * Draws the current frame
	 */
	private void render() {
		BufferStrategy strategy = getBufferStrategy(); // this Game's BufferStrategy
		Graphics g = strategy.getDrawGraphics(); // get the next Graphics object from the strategy
		graphics.setColor(Color.BLACK);

		render.clear();

		world.render(render, graphics);

		if (devMode) {
			world.player.getVelocity().draw(graphics, 8, world.player.getIntX(), world.player.getIntY());
			devInfo.render(graphics);
		}

		g.drawImage(image, 0, 0, width, height, null); // draw the rendered image onto the Graphics object
		g.dispose(); // let go of the Graphics object
		strategy.show(); // have the strategy do its thing
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
		if (Keyboard.Controls.SCREENSHOT.pressed()) {
			if (!screenshotOrdered) { // if the screenshot key was up before
				new Screenshot(image);
				screenshotOrdered = true; // remember that screenshot was pressed
			}
		} else { // screenshot key not pressed
			screenshotOrdered = false;
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
				devMode = !devMode; // toggle
				devModeOrdered = true;
			}
		} else {
			devModeOrdered = false;
		}

		// TODO fix bug where key is still thought to be pressed on transition from fullscreen to windowed
		if (Keyboard.Controls.FULLSCREEN.pressed()) {
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
		height = (int) (width * aspectRatio);
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

		createBufferStrategy(3);

		// initialize visual elements
		render = new Render(width, height);
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); // this is what gets drawn onto the buffer strategy
		graphics = image.createGraphics();
		graphics.addRenderingHints(renderHints);
		render.pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData(); // link the image's pixel data with render
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
