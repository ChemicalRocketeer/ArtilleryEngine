package hellomisterme.artillery_engine;

import hellomisterme.artillery_engine.entities.collision.Collision;
import hellomisterme.artillery_engine.entities.collision.Collision.Box;
import hellomisterme.artillery_engine.entities.collision.Collision.Circle;

import java.awt.Dimension;

import javax.swing.JApplet;
import javax.swing.JFrame;

/**
 * Sets up a new Game object, puts it in a JFrame, and starts it running.
 * 
 * @since 11-9-12
 * @author David Aaron Suddjian
 */
@SuppressWarnings("serial")
public class Start extends JApplet {

	private Game game;
	private Thread thread;

	private void runGame() {
		game = new Game();
		JFrame frame = new JFrame();
		frame.setResizable(false);
		frame.setTitle(Game.title);
		frame.add(game); // add game to frame
		frame.pack(); // automatically set window size
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null); // center
		frame.setVisible(true);
		game.requestFocus(); // get focus if OS allows it
		game.run();
		// TODO figure fullscreen out
		// GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(frame);
	}

	private static void collisionTest(Collision c, Collision c2) {
		c.collides(c2, 0, 0, 0, 0);
	}

	public static void main(String[] args) {
		Start.collisionTest(new Circle(0, 0, 10), new Box(0, 0, 10, 10));
		new Start().runGame();
	}

	public void init() {
		game = new Game();
		add(game);
		setSize(new Dimension(game.getWidth(), game.getHeight()));
	}

	public void start() {
		thread = new Thread(game);
		thread.start(); // calls game's run() method
	}

	public void stop() {
		game.stop();
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
