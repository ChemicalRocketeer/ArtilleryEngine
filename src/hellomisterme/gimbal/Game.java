package hellomisterme.gimbal;

import hellomisterme.gimbal.graphics.Render;
import hellomisterme.gimbal.input.KeyInput;
import hellomisterme.gimbal.io.Savegame;
import hellomisterme.gimbal.world.World;
import hellomisterme.gimbal.world.testWorld;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

/**
 * The Game handles display and management of game objects.
 * 
 * @version Pre-Alpha.0.04
 * @since 10-14-12
 * @author David Aaron Suddjian
 */
public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;

	public static String title = "Gimbal";
	public static int width = 800;
	public static int height = width * 10 / 16;
	public static final int TICKS_PER_SECOND = 60;

	private Thread thread;
	private JFrame frame;
	boolean running = false;

	private BufferedImage image;
	private Render render;	

	private World world;

	private boolean screenshotOrdered = false;
	private boolean ioOrdered = false;

	public Game() {
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
	 * Starts running this Game
	 */
	public synchronized void start() {
		createBufferStrategy(3);
		running = true;
		thread = new Thread(this);
		thread.start();
	}

	/**
	 * This is it. The game loop. If something happens in the game, it begins here.
	 * 
	 * TODO make all the extra frames actually do something useful
	 * 
	 * Called by this Game's Thread thread.
	 */
	public void run() {
		
		int totalFrames = 0; // the total number of frames generated
		int totalSeconds = 0; // the number of times totalFrames has been updated
		int frameCount = 0; // FPS timer variable
		int tickCount = 0; // TPS timer variable
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

			// count and print the FPS, TPS, AVG, and SEC to console and titlebar
			if (System.currentTimeMillis() >= lastRecord + 1000) {
				totalFrames += frameCount;
				totalSeconds++;
				frame.setTitle(title + "   -   FPS: " + frameCount + ",   AVG: " + (totalFrames / totalSeconds) + ",   TPS: " + tickCount + ",   SEC: " + totalSeconds);
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
		
		// if an io key is pressed
		if (KeyInput.pressed(KeyInput.save)) {
			if (ioOrdered == false) { // if an io key was up before
				Savegame.saveData(world, "quicksave");
				ioOrdered = true; // remember that io was ordered
			}
		} else if (KeyInput.pressed(KeyInput.load)) {
			if (ioOrdered == false) { // if the io key was up before
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
		try {
			thread.join(); // end thread, can't have that dangling there
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setupFrame() {
		setPreferredSize(new Dimension(width, height)); // set canvas size
		frame = new JFrame();
		frame.setResizable(false);
		frame.setTitle(title);
		frame.add(this); // add Game to frame
		frame.pack(); // automatically set window size
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		requestFocus(); // get focus if OS allows it
		frame.setLocationRelativeTo(null); // center
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		Game game = new Game();
		game.setupFrame();
		game.start();
	}

}
