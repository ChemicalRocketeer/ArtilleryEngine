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

	// All possible keys (but not actually all the POSSIBLE keys because that would be an immense array)
	private static boolean[] keys = new boolean[256];
	// The key codes that are relevant to this program (subject to change by other objects during execution, a preferences loader for example)
	public static int[] up = new int[] { KeyEvent.VK_W, KeyEvent.VK_UP };
	public static int[] down = new int[] { KeyEvent.VK_S, KeyEvent.VK_DOWN };
	public static int[] left = new int[] { KeyEvent.VK_A, KeyEvent.VK_LEFT };
	public static int[] right = new int[] { KeyEvent.VK_D, KeyEvent.VK_RIGHT };
	public static final int keyCount = 4;

	/**
	 * Returns whether any key in an array of keys is pressed or not.
	 * 
	 * KeyInput's static key value arrays can be passed to easily check multiple keys that should all do the same thing.
	 * 
	 * @param keyCodes
	 *            the array of codes to check
	 */
	public static boolean pressed(int[] keyCodes) {
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
	public static boolean pressed(int keyCode) {
		return keys[keyCode];
	}

	/**
	 * Reads settings.txt in the same directory and uses any keyCodes found instead of the defaults. If there are no keyCodes found, the default codes are used.
	 * 
	 * settings.txt must follow the code block format:
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
			// read settings.txt to settings
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream("settings.txt"), "UTF8"));
			// go to the start of the settings block, removing whitespace
			while (!in.readLine().replaceAll("\\s", "").replaceAll("\\t", "").toLowerCase().equals("keybindings{")) {
			}
			// read the settings block, removing whitespace
			String line = "";
			while (!(line = in.readLine().replaceAll("\\s", "").replaceAll("\\t", "")).toLowerCase().equals("}")) {
				if (!line.equals(""))
					settings += line;
			}
			//parse
			parsesettings(settings);
			in.close();
		} catch (Exception e) {
			Err.error(5);
			e.printStackTrace();
		}
	}

	/**
	 * Parses through an settings String and assigns the keyCodes inside to KeyInput, using the format:
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
	}

	/**
	 * Generates an int[] using an unlimited-length String of format num,num,num,num
	 * 
	 * @param codes
	 *            the codes in the correct format
	 * @return the codes extracted into an int[]
	 */
	public static int[] extractKeyCodes(String codes) {
		// count the number of code values using the number of commas in 'codes'
		int commas = 0;
		for (int i = 0; i < codes.length(); i++) {
			if (codes.charAt(i) == ',') {
				commas++;
			}
		}
		int[] extracted = new int[commas + 1]; // there will always be 1 more code than there are commas
		Scanner scan = new Scanner(codes);
		scan.useDelimiter(",");
		for (int i = 0; scan.hasNextInt(); i++) {
			extracted[i] = scan.nextInt();
		}
		return extracted;
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
