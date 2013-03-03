package hellomisterme.artillery_engine;

import java.awt.Dimension;

import javax.swing.JApplet;

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

	public Start() {
		game = new Game();
	}

	private void runGame() {
		game.run();
	}

	public static void main(String[] args) {
		new Start().runGame();
	}

	@Override
	public void init() {
		add(game);
		setSize(new Dimension(game.getWidth(), game.getHeight()));
	}

	@Override
	public void start() {
		thread = new Thread(game);
		thread.start(); // calls game's run() method
	}

	@Override
	public void stop() {
		game.stop();
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
