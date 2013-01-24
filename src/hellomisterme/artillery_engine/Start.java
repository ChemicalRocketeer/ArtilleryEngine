package hellomisterme.artillery_engine;

import javax.swing.JFrame;

/**
 * Sets up a new Game object, puts it in a JFrame, and starts it running.
 * 
 * @since 11-9-12
 * @author David Aaron Suddjian
 */
public class Start {

	public static void main(String[] args) {
		Game game = new Game();
		
		JFrame frame = new JFrame();
		frame.setResizable(false);
		frame.setTitle(Game.title);
		frame.add(game); // add game to frame
		frame.pack(); // automatically set window size
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null); // center
		frame.setVisible(true);
		game.requestFocus(); // get focus if OS allows it
		game.setup();
		
		//TODO figure fullscreen out
		//GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(frame);

		Thread thread = new Thread(game);
		thread.start(); // calls game's run() method
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
}
