package possiblydavid.gimbal;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Random;
import javax.swing.JFrame;

import possiblydavid.gimbal.graphics.Render;

/**
 * The Game handles display and management of game objects.
 * 
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

	public Game() {
		Dimension size = new Dimension(width, height);
		setPreferredSize(size);

		frame = new JFrame();
		render = new Render(width, height);
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		System.out.println("Game image: " + image.getType()); // testing
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	}

	/**
	 * Starts running this Game
	 */
	public synchronized void start() {
		running = true;
		thread = new Thread(this);
		thread.start();
	}

	/**
	 * Stops running the game
	 */
	public synchronized void stop() {
		running = false;
		try {
			thread.join(); // end thread thread
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This is it. The game loop. If something happens in the game, it begins here.
	 * 
	 * Called by this Game's thread.
	 */
	public void run() {
		int frameCount = 0; // FPS timer variable
		int tickCount = 0; // TPS timer variable
		long lastRecord = System.currentTimeMillis(); // the last time frameCount and tickCount were written to console

		// regulate tick frequency
		double msPerTick = 1000000000D / 59.87D; // target time between ticks, adjusted to best make up for accuracy errors
		double delta = 0; // difference between now and the last tick
		long lastTime = System.nanoTime();

		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / msPerTick;
			lastTime = now;
			
			if (delta > 1) {
				tick();
				delta --;
				tickCount++;
			}
			
			render();
			frameCount++;

			// count and print the FPS and TPS to console
			if (System.currentTimeMillis() >= lastRecord + 1000) {
				System.out.println("FPS: " + frameCount + ", TPS: " + tickCount);
				frameCount = 0;
				tickCount = 0;
				lastRecord = System.currentTimeMillis();
			}
		}
	}

	public void tick() {

	}

	public void render() {
		BufferStrategy strategy = getBufferStrategy();
		if (strategy == null) {
			createBufferStrategy(3);
			return;
		}

		render.clear();
		render.render();

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
