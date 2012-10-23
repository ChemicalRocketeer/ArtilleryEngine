package possiblydavid.gimbal.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import possiblydavid.gimbal.Tick;

public class KeyInput implements KeyListener, Tick{
	
	private boolean[] keys = new boolean[120];
	public boolean up, down, left, right;
	
	public void tick() {
		up = keys[KeyEvent.VK_W] || keys[KeyEvent.VK_UP];
		down = keys[KeyEvent.VK_S] || keys[KeyEvent.VK_DOWN];
		left = keys[KeyEvent.VK_A] || keys[KeyEvent.VK_DOWN];
		right = keys[KeyEvent.VK_D] || keys[KeyEvent.VK_RIGHT];
	}

	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
	}

	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
	}

	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
