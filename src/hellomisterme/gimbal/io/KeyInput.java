package hellomisterme.gimbal.io;

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
	
	// The keys that are relevant to this program (subject to change by other objects during execution, a settings file for example)
	// TODO use unsigned bytes (chars?)
	public static short[] up 			= new short[] {KeyEvent.VK_W, KeyEvent.VK_UP };
	public static short[] down 			= new short[] {KeyEvent.VK_S, KeyEvent.VK_DOWN };
	public static short[] left 			= new short[] {KeyEvent.VK_A, KeyEvent.VK_LEFT };
	public static short[] right 		= new short[] {KeyEvent.VK_D, KeyEvent.VK_RIGHT };
	public static short[] addbaddie		= new short[] {KeyEvent.VK_G};
	public static short[] screenshot 	= new short[] {KeyEvent.VK_F1};
	public static short[] save	 		= new short[] {KeyEvent.VK_F5};
	public static short[] load	 		= new short[] {KeyEvent.VK_F9};
	public static short[] devmode	 	= new short[] {KeyEvent.VK_F3};

	public static final String SETTINGS_FILE = "settings.txt";

	public KeyInput() {
		readSettingsFile();
	}

	/**
	 * Parses through a settings String and assigns the keyCodes inside to KeyInput, using the format:
	 * 
	 * <code>
	 * up:int,int,int;down:int,int;left:int;right:int,int,int,int;
	 * </code>
	 * 
	 * The number of values is unlimited. No whitespace. Lowercase.
	 * 
	 * @param settings
	 *            the collective settings to apply to KeyInput
	 */
	public static void parsesettings(String settings) {
		up = extractKeyCodes(settings, "up:", up);
		down = extractKeyCodes(settings, "down:", down);
		left = extractKeyCodes(settings, "left:", left);
		right = extractKeyCodes(settings, "right:", right);
		screenshot = extractKeyCodes(settings, "screenshot:", screenshot);
		addbaddie = extractKeyCodes(settings, "addbaddie:", addbaddie);
		save = extractKeyCodes(settings, "save:", save);
		load = extractKeyCodes(settings, "load:", load);
		devmode = extractKeyCodes(settings, "devmode:", devmode);
	}

	/**
	 * Searches through the given string for the query, then extracts any keycodes following the query into a short[]. Returns a short[] of key codes using an unlimited-length String of format
	 * num,num,num,num
	 * 
	 * @param settings
	 *            the codes in the correct format
	 * @param query
	 *            the string to search for in settings
	 * @param key
	 *            the array to use as the default if the search doesn't find anything
	 * @return the codes extracted into a short[]
	 */
	public static short[] extractKeyCodes(String settings, String query, short[] key) {
		int index = settings.indexOf(query);
		if (index != -1) { // if the query is found in the settings string
			settings = settings.substring(index + query.length(), settings.indexOf(';', index)); // trim settings so it only contains the comma-delimited codes
			int commas = 0; // count the number of code values using the number of commas in settings
			for (int i = 0; i < settings.length(); i++) { // count how many codes there are by counting the commas
				if (settings.charAt(i) == ',') {
					commas++;
				}
			}
			key = new short[commas + 1]; // there will always be 1 more code than there are commas
			Scanner scan = new Scanner(settings);
			scan.useDelimiter(","); // because the codes are comma-delimited
			for (int i = 0; scan.hasNextInt(); i++) { 
				key[i] = scan.nextShort();
			}
		}
		return key;
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
