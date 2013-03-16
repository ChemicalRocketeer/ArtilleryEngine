package hellomisterme.artillery_engine.io;

import hellomisterme.artillery_engine.Err;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Savegame holds methods for interacting with saved game data on the hard drive.
 * 
 * @since 11-2-12
 * @author David Aaron Suddjian
 */
public class Savegame {

	/**
	 * The current numerical version of the save system.
	 */
	public static final String version = "0";
	private static String currentVersion = version;

	public static final String extension = ".arte";

	/**
	 * Saves the data of the given Savable in the saves folder
	 * 
	 * @param savable
	 *            the Savable to save data from
	 * @param name
	 *            used to name the save file
	 */
	public void saveData(Savable savable, String name) {
		try {
			new File("saves").mkdir(); // make the diectory
			DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(new File("saves/" + name + extension))));
			out.writeUTF(version);
			savable.write(out);
			out.close();
			System.out.println("Saved " + name + ".arte!"); // TODO put text on the screen instead
		} catch (IOException e) {
			Err.error("Savegame can't save saves/" + name + extension + "!");
			e.printStackTrace();
		}
	}

	public void loadData(Savable savable, String name) {
		try {
			DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(new File("saves/" + name + extension))));
			String v = in.readUTF();
			if (v.equals(version)) {
				savable.read(in);
			} else {
				Err.error("Trying to load a save with an invalid version number!");
			}
			in.close();
		} catch (IOException e) {
			Err.error("Savegame can't load saves/" + name + extension);
		}
	}

	public static String getCurrentVersion() {
		return currentVersion;
	}
}
