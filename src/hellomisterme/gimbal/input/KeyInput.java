package hellomisterme.gimbal.input;

import hellomisterme.gimbal.Err;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Scanner;


/**
 * KeyInput stores keyboard input and can be accessed by other objects that need to know whether a key is pressed or not.
 * 
 * KeyInput can read data from settings.txt
 * 
 * @since 10-23-12
 * @author David Aaron Suddjian
 */
public class KeyInput implements KeyListener {

	// All possible keys (but not actually all the theoretically POSSIBLE keys because that would be an immense array)
	private static boolean[] keys = new boolean[255];
	
	// The key codes that are relevant to this program (subject to change by other objects during execution, a settings file for example)
	// TODO use unsigned bytes (chars?)
	public static short[] up 			= new short[] {KeyEvent.VK_W, KeyEvent.VK_UP };
	public static short[] down 			= new short[] {KeyEvent.VK_S, KeyEvent.VK_DOWN };
	public static short[] left 			= new short[] {KeyEvent.VK_A, KeyEvent.VK_LEFT };
	public static short[] right 		= new short[] {KeyEvent.VK_D, KeyEvent.VK_RIGHT };
	public static short[] screenshot 	= new short[] {KeyEvent.VK_F1};
	public static short[] save	 		= new short[] {KeyEvent.VK_F5};
	public static short[] load	 		= new short[] {KeyEvent.VK_F9};
	
	public static final String SETTINGS_FILE = "settings.txt";

	public KeyInput() {
		readSettingsFile();
	}
	
	/**
	 * Parses through an settings String and assigns the keyCodes inside to KeyInput, using the format:
	 * 
	 * TODO refactor
	 * 
	 * <code>
	 * up:int,int,int;down:int,int;left:int;right:int,int,int,int;
	 * </code>
	 * 
	 * The number of 'int' values is unlimited. No whitespace. Lowercase.
	 * 
	 * @param settings
	 *            the collective settings to apply to KeyInput
	 */
	public static void parsesettings(String settings) {
		// check for up
		int index = settings.indexOf("up:");
		if (index != -1) {
			index += "up:".length();
			up = extractKeyCodes(settings.substring(index, settings.indexOf(';', index)));
		}
		// check for down
		index = settings.indexOf("down:");
	 	if (index != -1) {
			index += "down:".length();
			down = extractKeyCodes(settings.substring(index, settings.indexOf(';', index)));
		}
		// check for left
		index = settings.indexOf("left:");
		if (index != -1) {
			index += "left:".length();
			left = extractKeyCodes(settings.substring(index, settings.indexOf(';', index)));
			settings = settings.substring(settings.indexOf(';') + 1);
		}
		// check for right
		index = settings.indexOf("right:");
		if (index != -1) {
			index += "right:".length();
			right = extractKeyCodes(settings.substring(index, settings.indexOf(';', index)));
			settings = settings.substring(settings.indexOf(';') + 1);
		}
		// check for screenshot
		index = settings.indexOf("screenshot:");
		if (index != -1) {
			index += "screenshot:".length();
			screenshot = extractKeyCodes(settings.substring(index, settings.indexOf(';', index)));
			settings = settings.substring(settings.indexOf(';') + 1);
		}
		// check for save
		index = settings.indexOf("save:");
		if (index != -1) {
			index += "save:".length();
			save = extractKeyCodes(settings.substring(index, settings.indexOf(';', index)));
			settings = settings.substring(settings.indexOf(';') + 1);
		}
		// check for load
		index = settings.indexOf("load:");
		if (index != -1) {
			index += "load:".length();
			load = extractKeyCodes(settings.substring(index, settings.indexOf(';', index)));
			settings = settings.substring(settings.indexOf(';') + 1);
		}
	}

	/**
	 * Generates an int[] using an unlimited-length String of format num,num,num,num
	 * 
	 * @param codes
	 *            the codes in the correct format
	 * @return the codes extracted into an int[]
	 */
	public static short[] extractKeyCodes(String codes) {
		// count the number of code values using the number of commas in 'codes'
		int commas = 0;
		for (int i = 0; i < codes.length(); i++) {
			if (codes.charAt(i) == ',') {
				commas++;
			}
		}
		short[] extracted = new short[commas + 1]; // there will always be 1 more code than there are commas
		Scanner scan = new Scanner(codes);
		scan.useDelimiter(",");
		for (int i = 0; scan.hasNextInt(); i++) {
			extracted[i] = scan.nextShort();
		}
		return extracted;
	}

	/**
	 * Reads SETTINGS_FILE in the same directory and uses any keyCodes found instead of the defaults. If there are no keyCodes found, the default codes are used.
	 * 
	 * SETTINGS_FILE must follow the code block format:
	 * 
	 * <code>
	 * settings{
	 * up: 38, 87;
	 * down: 38, 87, 39, 68;
	 * right: 68, 39;
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
			while (!in.readLine().replaceAll("\\s", "").replaceAll("\\t", "").toLowerCase().equals("keybindings{")) {
			}
			// read the settings block, removing whitespace
			String line = "";
			while (!(line = in.readLine().replaceAll("\\s", "").replaceAll("\\t", "")).toLowerCase().equals("}")) {
				if (!line.equals(""))
					settings += line;
			}
			parsesettings(settings);
			in.close();
		} catch (Exception e) {
			Err.error("KeyInput can't read " + SETTINGS_FILE + "!");
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns whether any key in an array of keys is pressed or not.
	 * 
	 * KeyInput's static key value arrays can be passed to easily check multiple keys that should all do the same thing.
	 * 
	 * @param keyCodes
	 *            the array of codes to check
	 */
	public static boolean pressed(short[] keyCodes) {
		for (int keyCode : keyCodes) {
			if (keys[keyCode])
				return true;
		}
		return false;
	}

	/**
	 * Returns whether a key is pressed or not.
	 * 
	 * @param keyCode
	 *            the id of the Key you want to know about
	 * @return true if the key is pressed, else false
	 */
	public static boolean pressed(short keyCode) {
		return keys[keyCode];
	}

	/**
	 * Called by JVM when a key is pressed. Do not call this method unless you are making a robot.
	 */
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() >= keys.length) {
			Err.error("Invalid key press!");
			return;
		}
		keys[e.getKeyCode()] = true;
	}

	/**
	 * Called by JVM when a key is released. Do not call this method unless you are making a robot.
	 */
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() >= keys.length) {
			Err.error("Invalid key release!");
			return;
		}
		keys[e.getKeyCode()] = false;
	}

	public void keyTyped(KeyEvent e) {

	}
}
