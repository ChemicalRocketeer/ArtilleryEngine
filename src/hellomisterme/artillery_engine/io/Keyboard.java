package hellomisterme.artillery_engine.io;

import hellomisterme.artillery_engine.Err;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Scanner;

/**
 * Keyboard stores keyboard input and can be accessed by other objects that need to know whether a key is pressed or not.
 * 
 * Keyboard can read data from settings.txt
 * 
 * @since 10-23-12
 * @author David Aaron Suddjian
 */
public class Keyboard implements KeyListener {
	
	// All possible keys (but not actually all the theoretically POSSIBLE keys because that would be an immense array)
	private static BitSet keys = new BitSet(KeyEvent.KEY_LAST);
	
	public static final String SETTINGS_FILE = "settings.txt";
	
	public static enum Controls {
		UP				(new int[] {KeyEvent.VK_W}),
		DOWN			(new int[] {KeyEvent.VK_S}),
		LEFT			(new int[] {KeyEvent.VK_A}),
		RIGHT			(new int[] {KeyEvent.VK_D}),
		ROTLEFT			(new int[] {KeyEvent.VK_Q}),
		ROTRIGHT		(new int[] {KeyEvent.VK_E}),
		CAMROTLEFT		(new int[] {KeyEvent.VK_OPEN_BRACKET}),
		CAMROTRIGHT		(new int[] {KeyEvent.VK_CLOSE_BRACKET}),
		THROTTLEUP		(new int[] {KeyEvent.VK_SHIFT}),
		THROTTLEDOWN	(new int[] {KeyEvent.VK_CONTROL}),
		THROTTLECUT		(new int[] {KeyEvent.VK_X}),
		ADDBADDIE		(new int[] {KeyEvent.VK_G}),
		SCREENSHOT		(new int[] {KeyEvent.VK_F1}),
		SAVE			(new int[] {KeyEvent.VK_F5}),
		LOAD			(new int[] {KeyEvent.VK_F9}),
		DEVMODE			(new int[] {KeyEvent.VK_F3}),
		FULLSCREEN		(new int[] {KeyEvent.VK_F11}),
		PAUSE			(new int[] {KeyEvent.VK_ESCAPE});
		
		private final int[] defaultCodes;
		private int[] codes;
		
		Controls(int[] codes) {
			this.defaultCodes = codes;
			this.codes = defaultCodes;
		}
		
		public boolean pressed() {
			return Keyboard.pressed(codes);
		}
		
		public void setKeyCodes(String settings) {
			int index = settings.indexOf(name());
			if (index != -1) { // if the name is found in the settings string
				settings = settings.substring(index + name().length(), settings.indexOf(';', index)); // trim settings so it only contains the comma-delimited codes
				Scanner scan = new Scanner(settings);
				scan.useDelimiter(","); // because the codes are comma-delimited
				List<Byte> newCodes = new ArrayList<Byte>(); // make a list to hold all the new codes
				while (scan.hasNextByte()) {
					byte code = scan.nextByte();
					if (!newCodes.contains(code)) { // add all the codes from the string to
						newCodes.add(new Byte(code));
					}
				}
				codes = new int[newCodes.size()];
				for (int i = 0; i < codes.length; i++) {
					codes[i] = newCodes.get(i).byteValue();
				}
				scan.close();
			}
		}
	}
	
	public Keyboard() {
		readSettingsFile();
	}
	
	/**
	 * Looks for SETTINGS_FILE in the working directory and uses any keyCodes found instead of the defaults. If there are no keyCodes found, the default codes are used.
	 * 
	 * SETTINGS_FILE must follow the code block format:
	 * 
	 * <code>
	 * settings{
	 * UP: 38, 87;
	 * DOWN: 38, 87, 39, 68;
	 * RIGHT: 68, 39;
	 * }
	 * </code>
	 * 
	 * Spaces, tabs, and case do not matter, but line breaks do.
	 */
	public static void readSettingsFile() {
		String settings = "";
		try {
			// read SETTINGS_FILE to settings
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(SETTINGS_FILE), "UTF8"));
			// go to the start of the settings block, removing whitespace (readLine removes the line breaks)
			while (!in.readLine().replaceAll("\\s", "").replaceAll("\\t", "").toUpperCase().equals("KEYBINDINGS{")) {
			}
			;
			// read the settings block, removing whitespace
			System.out.println(settings);
			String line = "";
			while (!(line = in.readLine().replaceAll("\\s", "").replaceAll("\\t", "")).toUpperCase().equals("}")) {
				if (!line.equals("")) settings += line;
			}
			for (Controls c : Controls.values()) {
				c.setKeyCodes(settings);
			}
			in.close();
		} catch (Exception e) {
			Err.error("Keyboard can't read " + SETTINGS_FILE + "!");
			e.printStackTrace();
		}
	}
	
	public static boolean pressed(Controls c) {
		return c.pressed();
	}
	
	/**
	 * Returns whether any key in an array of keys is pressed or not.
	 * 
	 * Keyboard's static key value arrays can be passed to easily check multiple keys that should all do the same thing.
	 * 
	 * @param keyCodes
	 *        the array of codes to check
	 */
	public static boolean pressed(int[] keyCodes) {
		for (int keyCode : keyCodes) {
			if (keys.get(keyCode))
				return true;
		}
		return false;
	}
	
	/**
	 * Returns whether a key is pressed or not.
	 * 
	 * @param keyCode
	 *        the id of the Key you want to know about
	 * @return true if the key is pressed, else false
	 */
	public static boolean pressed(byte keyCode) {
		return keys.get(keyCode);
	}
	
	/**
	 * Called by JVM when a key is pressed. Do not call this method unless you are making a robot.
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() < keys.size()) {
			keys.set(e.getKeyCode(), true);
		} else {
			Err.error("Invalid key press: " + e.getKeyCode());
		}
	}
	
	/**
	 * Called by JVM when a key is released. Do not call this method unless you are making a robot.
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() < keys.size()) {
			keys.set(e.getKeyCode(), false);
		} else {
			Err.error("Invalid key release: " + e.getKeyCode());
		}
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		
	}
}
