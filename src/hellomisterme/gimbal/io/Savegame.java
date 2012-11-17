package hellomisterme.gimbal.io;

import hellomisterme.gimbal.Err;

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

	public static final int VERSION = 0;

	/**
	 * Saves the data of the given Savable as saves/name.gmbl
	 * 
	 * @param savable
	 *            the Savable to save data from
	 * @param name
	 *            used to name the save file
	 */
	public static void saveData(Savable savable, String name) {
		try {
			new File("saves").mkdir(); // make the diectory
			DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(new File("saves/" + name + ".gmbl"))));
			out.writeInt(VERSION);
			savable.save(out);
			out.close();
			System.out.println("Saved " + name + ".gmbl!"); // TODO put text on the screen instead
		} catch (IOException e) {
			Err.error("Savegame can't save saves/" + name + ".gmbl");
			e.printStackTrace();
		}
	}

	public static void loadData(Savable savable, String name) {
		try {
			DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(new File("saves/" + name + ".gmbl"))));
			int version = in.readInt();
			if (version == 0) {
				savable.load(in);
			} else {
				Err.error("Trying to load a save with an invalid version number!");
			}
			in.close();
		} catch (IOException e) {
			Err.error("Savegame can't load saves/" + name + ".gmbl");
		}
	}
}
