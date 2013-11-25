package hellomisterme.artillery_engine.io;

import hellomisterme.artillery_engine.Err;
import hellomisterme.artillery_engine.Game;
import hellomisterme.artillery_engine.util.Transform;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
/**
 * Savegame holds methods for interacting with saved game data on the hard drive.
 * 
 * @since 11-2-12
 * @author David Aaron Suddjian
 */
public class ArteWriter {

	/** This ArteWriter's directory */
	public final String dir;
	/** This ArteWriter's filename */
	public final String filename;
	/** This ArteWriter's full path */
	public final String path;

	private final List<Class<? extends Savable>> classes = new ArrayList<>();
	// the header contains the list of classes used in the save file, along with their one-time save data and any other header data. Do not write to these!
	private final ByteArrayOutputStream headData = new ByteArrayOutputStream();
	private final DataOutputStream header = new DataOutputStream(headData);
	// the header contains the list of classes used in the save file, along with their one-time save data and any other header data. Do not write to these!
	private final ByteArrayOutputStream classData = new ByteArrayOutputStream();
	private final DataOutputStream classWriter = new DataOutputStream(classData);
	// data contains world and entity data. Do not write to these! Write to out!
	private final ByteArrayOutputStream worldData = new ByteArrayOutputStream(Game.currentInstance().getWorld().entityCount() * 100);
	private final DataOutputStream world = new DataOutputStream(worldData);
	// only write to this one! Switch between header and non-header mode by pointing this variable to one or the other!
	private DataOutputStream out = header;

	public ArteWriter(String dir, String filename, Game game) {
		this.dir = dir;
		this.filename = filename;
		this.path = dir + filename;
	}

	public void save(Game game) throws IOException, InstantiationException, IllegalAccessException {
		out = header;
		writeGameData(game);
		out = world;
		Game.currentInstance().getWorld().write(this);
		out = header;
		writeClassList();
		writeOneTimeClassData();
		finish();
	}

	private void writeGameData(Game game) throws IOException {
		write(Game.VERSION);
		write(game.isDevModeEnabled());
	}

	private void writeClassList() throws IOException {
		String[] classNames = new String[classes.size()];
		for (int i = 0; i < classNames.length; i++) {
			classNames[i] = classes.get(i).getCanonicalName();
		}
		write(classNames);
	}

	private void writeOneTimeClassData() throws InstantiationException, IllegalAccessException {
		for (Class<? extends Savable> c : classes) {
			c.newInstance().writeOncePerClass(this);
		}
	}

	private void finish() {
		File directory = new File(dir);
		if (!directory.isDirectory()) directory.mkdirs();
		OutputStream outfile = null;
		try {
			outfile = new BufferedOutputStream(new FileOutputStream(new File(path)));
			outfile.write(headData.toByteArray());
			outfile.write(worldData.toByteArray());
		} catch (FileNotFoundException e) {
			Err.error("Can't setup the save file " + path + "!", e);
		} catch (IOException e) {
			Err.error("Can't save game data!", e);
		}
	}

	public void write(Savable x) throws IOException {
		writeCode(SavefileTypecode.SAVABLE);
		writeClassCode(x);
		x.write(this);
	}

	public void write(int x) throws IOException {
		writeCode(SavefileTypecode.INT);
		out.writeInt(x);
	}

	public void write(long x) throws IOException {
		writeCode(SavefileTypecode.LONG);
		out.writeLong(x);
	}

	public void write(boolean x) throws IOException {
		writeCode(SavefileTypecode.BOOLEAN);
		out.writeBoolean(x);
	}

	public void write(String x) throws IOException {
		writeCode(SavefileTypecode.STRING);
		out.writeUTF(x);
	}

	public void write(Transform x) throws IOException {
		writeCode(SavefileTypecode.TRANSFORM);
		out.writeDouble(x.rotation);
		out.writeDouble(x.position.x);
		out.writeDouble(x.position.y);
		out.writeDouble(x.scale.x);
		out.writeDouble(x.scale.y);
	}

	public void write(Savable[] x) throws IOException {
		writeCode(SavefileTypecode.ARRAY); // when an array is written, two typecodes are recorded: the first shows it is an array, and the second shows what type it is.
		writeCode(SavefileTypecode.SAVABLE);
		out.writeInt(x.length);
		for (Savable s : x) {
			writeClassCode(s);
			s.write(this);
		}
	}

	public void write(int[] x) throws IOException {
		writeCode(SavefileTypecode.ARRAY);
		writeCode(SavefileTypecode.INT);
		out.writeInt(x.length);
		for (int i : x) {
			out.writeInt(i);
		}
	}

	public void write(byte[] x) throws IOException {
		writeCode(SavefileTypecode.ARRAY);
		writeCode(SavefileTypecode.BYTE);
		out.writeInt(x.length);
		for (byte i : x) {
			out.writeByte(i);
		}
	}

	public void write(String[] x) throws IOException {
		writeCode(SavefileTypecode.ARRAY);
		writeCode(SavefileTypecode.STRING);
		out.writeInt(x.length);
		for (String i : x) {
			out.writeUTF(i);
		}
	}

	private void writeCode(SavefileTypecode type) throws IOException {
		out.writeByte(type.ordinal());
	}

	private void writeClassCode(Savable s) throws IOException {
		Class<? extends Savable> c = s.getClass();
		if (classes.indexOf(c) == -1) { // classes does not have a copy of the class of the given Savable
			classes.add(c);
		}
		out.writeInt(classes.indexOf(c));
	}
}
