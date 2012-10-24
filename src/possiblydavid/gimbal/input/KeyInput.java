package possiblydavid.gimbal.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * KeyInput stores keyboard input and can be accessed by other objects that need to know whether a key is pressed or not.
 * 
 * @since 10-23-12
 * @author David Aaron Suddjian
 */
public class KeyInput implements KeyListener {
	
	// All possible keys (but not actually all the POSSIBLE keys because that would be an immense array)
	private static boolean[] keys = new boolean[256];
	// The key codes that are relevant to this program (subject to change by other objects during execution, a preferences loader for example)
	public static int[] up = new int[] {KeyEvent.VK_W, KeyEvent.VK_UP};
	public static int[] down = new int[] {KeyEvent.VK_S, KeyEvent.VK_DOWN};
	public static int[] left = new int[] {KeyEvent.VK_A, KeyEvent.VK_LEFT};
	public static int[] right = new int[] {KeyEvent.VK_D, KeyEvent.VK_RIGHT};

	/**
	 * Returns whether any key in an array of keys is pressed or not.
	 * 
	 * KeyInput's static key value arrays can be passed to easily check multiple keys that should all do the same thing.
	 * 
	 * @param keyCodes the array of codes to check
	 */
	public static boolean pressed(int[] keyCodes) {
		for (int keyCode : keyCodes) {
			if (keys[keyCode]) return true;
		}
		return false;
	}
	
	/**
	 * Returns whether a key is pressed or not.
	 * 
	 * @param keyCode the id of the Key you want to know about
	 * @return true if the key is pressed, else false
	 */
	public static boolean pressed(int keyCode) {
		return keys[keyCode];
	}

	/**
	 * Called by JVM when a key is pressed. Do not call this method unless you are making a robot.
	 */
	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
	}

	/**
	 * Called by JVM when a key is released. Do not call this method unless you are making a robot.
	 */
	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
	}

	public void keyTyped(KeyEvent e) {
		
	}

}
