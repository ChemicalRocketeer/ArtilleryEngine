package possiblydavid.gimbal;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;

import javax.swing.JFrame;

import possiblydavid.gimbal.entities.Mover;
import possiblydavid.gimbal.graphics.Render;

/**
 * The Game handles display and management of game objects.
 * 
 * @version Pre-Alpha.0.01_2
 * @author David Aaron Suddjian
 */
public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;

	public static int width = 800;
	public static int height = width * 10 / 16;
	public static String title = "Gimbal";

	private Thread thread;
	private JFrame frame;
	boolean running = false;

	private Render render;
	private BufferedImage image;
	private int[] pixels;

	// TODO: put entities and tock in a world class or something
	private ArrayList<Entity> entities;
	private ArrayList<Tick> tock;

	public Game() {
		Dimension size = new Dimension(width, height);
		setPreferredSize(size);

		// initialize visual elements
		frame = new JFrame();
		render = new Render(width, height);
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

		// initialize entities and tock
		entities = new ArrayList<Entity>(2);
		tock = new ArrayList<Tick>(2);
	}

	/**
	 * Starts running this Game
	 */
	public synchronized void start() {
		running = true;
		thread = new Thread(this);

		Mover testMover = new Mover();
		Mover testMover2 = new Mover();
		addEntity(testMover);
		addTick(testMover);
		addEntity(testMover2);
		addTick(testMover2);
		testMover2.setX(30);
		testMover2.setY(10);

		thread.start();
	}

	private void addTick(Tick t) {
		tock.add(t);
	}

	public void addEntity(Entity e) {
		entities.add(e);
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

	/**
	 * This is it. The game loop. If something happens in the game, it begins here.
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
		double ns = 1000000000D / 59.9D; // target time between ticks, adjusted to best make up for accuracy errors
		double delta = 0; // difference between now and the last tick
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
				System.out.println("FPS: " + frameCount + ", TPS: " + tickCount + ", AVG: " + (totalFrames / totalSeconds) + ", SEC: " + totalSeconds);
				frame.setTitle(title + "   -   FPS: " + frameCount + ", TPS: " + tickCount);
				frameCount = 0;
				tickCount = 0;
				lastRecord = System.currentTimeMillis();
			}
		}
		stop();
	}

	public void tick() {
		for (int i = 0; i < tock.size(); i++) {
			tock.get(i).tick();
		}
	}

	public void render() {
		BufferStrategy strategy = getBufferStrategy();
		if (strategy == null) {
			createBufferStrategy(3);
			return;
		}

		render.clear();
		render.render(entities);

		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = render.pixels[i];
		}

		Graphics g = strategy.getDrawGraphics();
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		g.dispose();

		strategy.show();
	}

	public static void main(String[] args) {
		Game game = new Game();

		game.frame.setResizable(false);
		game.frame.setTitle(title);
		game.frame.add(game);
		game.frame.pack(); // automatically set size
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.setLocationRelativeTo(null); // center
		game.frame.setVisible(true);

		game.start();
	}

}
