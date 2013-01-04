package hellomisterme.gimbal;

import hellomisterme.gimbal.entities.Entity;
import hellomisterme.gimbal.graphics.Render;
import hellomisterme.gimbal.io.KeyInput;
import hellomisterme.gimbal.io.Savegame;
import hellomisterme.gimbal.world.World;
import hellomisterme.gimbal.world.testWorld;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Random;

import javax.swing.JFrame;

/**
 * The Game handles display and management of game objects.
 * 
 * @version Pre-Alpha.0.06.1
 * @since 10-14-12
 * @author David Aaron Suddjian
 */
public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;

	public static final double ISOMETRIC_RATIO = .5; // height to width, used in movement and distance calculations
	public static final Random RAND = new Random(System.currentTimeMillis());
	
	public static String title = "Gimbal";
	public static int width = 800;
	public static int height = width * 10 / 16;
	public static final int TICKS_PER_SECOND = 60;

	private JFrame frame;
	boolean running = false;

	private BufferedImage image;
	private Render render;

	private World world;

	private boolean devMode = false;
	private boolean devModeOrdered = false;
	private boolean screenshotOrdered = false;
	private boolean ioOrdered = false;

	public Game() {
		setPreferredSize(new Dimension(width, height));

		// initialize visual elements
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); // used to send data to the BufferStrategy
		render = new Render(width, height);
		
		render.pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

		// initialize world
		world = new testWorld(width, height);

		// initialize keyboard input
		addKeyListener(new KeyInput());
	}

	/**
	 * This is it. The game loop. If something happens in the game, it begins here.
	 * 
	 * TODO make all the extra frames actually do something useful
	 * 
	 * Called by this Game's Thread thread.
	 */
	public void run() {
		createBufferStrategy(3);

		running = true;

		long totalFrames = 0; // the total number of frames generated (never gets decremented, so it's a long)
		long totalSeconds = 0; // the number of times totalFrames has been updated (never gets decremented, so it's a long)
		int frameCount = 0; // FPS counter variable
		int tickCount = 0; // TPS counter variable
		long lastRecord = System.currentTimeMillis(); // the last time frameCount and tickCount were written to console

		// regulate tick frequency
		double ns = 1000000000.0 / (double) TICKS_PER_SECOND; // time between ticks
		double delta = 1; // difference between now and the last tick
		long lastTime = System.nanoTime();

		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;

			while (delta >= 1) {
				tick();
				delta--;
				tickCount++;
			}

			render();
			frameCount++;

			// count and print the FPS, TPS, AVG, and SEC to titlebar every second
			if (System.currentTimeMillis() >= lastRecord + 1000) {
				totalFrames += frameCount;
				totalSeconds++;
				if (devMode) {
					frame.setTitle(title + "   -   FPS: " + frameCount + ",   AVG: " + (totalFrames / totalSeconds) + ",   TPS: " + tickCount + ",   SEC: " + totalSeconds);
				}
				frameCount = 0;
				tickCount = 0;
				lastRecord = System.currentTimeMillis();
			}
		}
		stop();
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
		if (KeyInput.pressed(KeyInput.screenshot)) {
			if (!screenshotOrdered) { // if the screenshot key was up before
				render.screenshot();
				screenshotOrdered = true; // remember that screenshot was pressed
			}
		} else { // screenshot key not pressed
			screenshotOrdered = false;
		}

		// these if statements are organized to prevent save/load operations in the same tick
		// if an io key is pressed
		if (KeyInput.pressed(KeyInput.save)) {
			if (!ioOrdered) { // if an io key was up before
				Savegame.saveData(world, "quicksave");
				ioOrdered = true; // remember that io was ordered
			}
		} else if (KeyInput.pressed(KeyInput.load)) {
			if (!ioOrdered) { // if an io key was up before
				Savegame.loadData(world, "quicksave");
				ioOrdered = true; // remember that io was ordered
			}
		} else { // io keys not pressed
			ioOrdered = false;
		}

		// if devmode is being activated/deactivated
		if (KeyInput.pressed(KeyInput.devmode)) {
			if (!devModeOrdered) {
				devMode = !devMode; // toggle
				if (devMode) frame.setTitle(title + "   -   FPS: ...   AVG: ...   TPS: ...   SEC: ..."); // TODO change to on-screen text
				else frame.setTitle(title);
				devModeOrdered = true;
			}
		} else {
			devModeOrdered = false;
		}
	}

	/**
	 * Draws the game's current frame
	 */
	private void render() {
		render.clear();
		for (Entity e : world.getEntities()) {
			e.render(render);
		}

		BufferStrategy strategy = getBufferStrategy(); // this Game's BufferStrategy
		Graphics g = strategy.getDrawGraphics(); // get the next Graphics object from the strategy
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null); // draw the rendered image onto the Graphics object
		g.dispose(); // let go of the Graphics object
		strategy.show(); // have the strategy do its thing
	}

	/**
	 * Stops running the game
	 */
	public synchronized void stop() {
		running = false;
	}

	/**
	 * Sets this Game's frame variable to frame. Does not add this Game to frame. That must be done before calling this method.
	 * 
	 * If this is not called, devmode will not work
	 * 
	 * @param frame
	 *            the frame this Game will be allowed to manipulate
	 */
	public void setFrame(JFrame frame) {
		this.frame = frame;
	}
}
