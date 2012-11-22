package hellomisterme.gimbal;

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
	public static final Random GLOBAL_RAND = new Random(System.currentTimeMillis());
	public static String title = "Gimbal";
	public static int width = 800;
	public static int height = width * 10 / 16;
	public static final int TICKS_PER_SECOND = 60;

	private JFrame frame;
	boolean running = false;

	private BufferedImage image;
	private Render render;

	private World world;

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

		int totalFrames = 0; // the total number of frames generated
		int totalSeconds = 0; // the number of times totalFrames has been updated
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

			// count and print the FPS, TPS, AVG, and SEC to titlebar
			if (System.currentTimeMillis() >= lastRecord + 1000) {
				totalFrames += frameCount;
				totalSeconds++;
				if (frame != null) {
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
	 * Calls the world's tick() and callTick() methods, and checks for over-all game-related events, like screenshot key presses
	 */
	private void tick() {
		// if the screenshot key is pressed
		if (KeyInput.pressed(KeyInput.screenshot)) {
			if (screenshotOrdered == false) { // if the screenshot key was up before
				render.screenshot();
				screenshotOrdered = true; // remember that screenshot was pressed
			}
		} else { // screenshot key not pressed
			screenshotOrdered = false;
		}

		// these if statements are organized the way they are to prevent save/load operations in the same tick
		// if an io key is pressed
		if (KeyInput.pressed(KeyInput.save)) {
			if (ioOrdered == false) { // if an io key was up before
				Savegame.saveData(world, "quicksave");
				ioOrdered = true; // remember that io was ordered
			}
		} else if (KeyInput.pressed(KeyInput.load)) {
			if (ioOrdered == false) { // if an io key was up before
				Savegame.loadData(world, "quicksave");
				ioOrdered = true; // remember that io was ordered
			}
		} else { // io keys not pressed
			ioOrdered = false;
		}

		world.tick();
		world.callTick();
	}

	/**
	 * Calls render method of render, and displays the image
	 */
	private void render() {
		render.render(world.getEntities());

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
	 * Sets this Game's frame variable to f. Does not add this Game to f. That must be done before calling this method.
	 * 
	 * @param f
	 *            the frame this Game will be allowed to manipulate
	 */
	public void setFrame(JFrame f) {
		frame = f;
	}

}
