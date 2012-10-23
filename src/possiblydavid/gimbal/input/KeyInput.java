package possiblydavid.gimbal.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import possiblydavid.gimbal.Tick;

/**
 * KeyInput stores keyboard input and can be accessed by other objects that need to know whether a key is pressed or not.
 * 
 * TODO improve implementation
 * 
 * @since 10-23-12
 * @author David Aaron Suddjian
 *
 */
public class KeyInput implements KeyListener, Tick{
	
	// All possible keys (but not actually all the POSSIBLE keys because that would be an immense array)
	private boolean[] keys = new boolean[120];
	// The keys that are relevant to this program
	public boolean up, down, left, right;
	
	public void tick() {
		up = keys[KeyEvent.VK_W] || keys[KeyEvent.VK_UP];
		down = keys[KeyEvent.VK_S] || keys[KeyEvent.VK_DOWN];
		left = keys[KeyEvent.VK_A] || keys[KeyEvent.VK_DOWN];
		right = keys[KeyEvent.VK_D] || keys[KeyEvent.VK_RIGHT];
	}
	
	/**
	 * Returns whether a key is pressed or not.
	 * 
	 * @param e the Key you want to know about
	 * @return true if the key is pressed, else false
	 */
	public boolean pressed(KeyEvent e) {
		return keys[e.getKeyCode()];
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
		// TODO Auto-generated method stub
		
	}

}
