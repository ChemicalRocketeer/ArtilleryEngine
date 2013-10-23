package hellomisterme.artillery_engine;

import javax.swing.JApplet;

/**
 * Sets up a new Game object, puts it in a JFrame, and starts it running.
 * 
 * @since 11-9-12
 * @author David Aaron Suddjian
 */
@SuppressWarnings("serial")
public class Launcher extends JApplet {

	private Game game;
	private Thread thread;

	public static void main(String[] args) {
		Launcher launcher = new Launcher();
		launcher.init();
		launcher.start();
		// launcher.stop();
	}

	@Override
	public void init() {
		game = new Game();
		thread = new Thread(game);

	}

	@Override
	public void start() {
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
