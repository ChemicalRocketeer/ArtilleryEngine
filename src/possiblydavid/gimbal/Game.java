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

/**
 * The Game class displays and runs the game
 * 
 * @author David Aaron Suddjian
 */
public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;

	public static int width = 800;
	public static int height = width / 16 * 10;
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
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	}

	public synchronized void start() {
		running = true;
		thread = new Thread(this);
		thread.start();
	}

	public synchronized void stop() {
		running = false;
		try {
			thread.join(); // end thread thread
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {		
		while (running) {
			tick();
			render();
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

		render.render();

		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = render.pixels[i];
		}

		Graphics g = strategy.getDrawGraphics();

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());

		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);

		// generate colored test rectangles
		Random rand = new Random(System.nanoTime());
		g.setColor(new Color(rand.nextInt(255) + 1, rand.nextInt(255) + 1, rand.nextInt(255) + 1));
		int xPos = rand.nextInt(getWidth());
		int yPos = rand.nextInt(getHeight());
		g.fillRect(xPos, yPos, rand.nextInt(getWidth() - xPos), rand.nextInt(getHeight() - yPos));

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
