package hellomisterme.artillery_engine.io;

import hellomisterme.artillery_engine.Err;
import hellomisterme.artillery_engine.Game;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ArteReader {

	public final String path;
	public final String version;
	private final DataInputStream in;

	private List<Class<? extends Savable>> classes = new ArrayList<Class<? extends Savable>>();

	public ArteReader(String path, Savable savable) {
		this.path = path;
		String v = null;
		DataInputStream infile = null;
		try {
			infile = new DataInputStream(new BufferedInputStream(new FileInputStream(new File(path))));
			v = infile.readUTF();
			if (v.equals(Game.version)) {
				savable.read(this);
			} else {
				Err.error("Trying to load a save with an invalid version number!");
			}
			infile.close();
		} catch (Exception e) {
			Err.error("ArteReader can't read " + path, e);
		}
		version = v;
		in = infile;
	}

	private void readHeader() throws IOException, TypecodeMismatchException {

	}

	public int readInt() throws IOException, TypecodeMismatchException {
		checkSavefileTypecode(SavefileTypecode.INT);
		return in.readInt();
	}

	public String readString() throws IOException, TypecodeMismatchException {
		checkSavefileTypecode(SavefileTypecode.STRING);
		return in.readUTF();
	}

	private void checkSavefileTypecode(SavefileTypecode intended) throws IOException, TypecodeMismatchException {
		// if the typecode read doesn't match the one given, throw an exception
		byte code = in.readByte();
		if (code != intended.ordinal()) throw new TypecodeMismatchException(intended.ordinal(), code);
	}

	/**
	 * Thrown when the typecode read doesn't match what was expected. Contains both for easier handling
	 * 
	 * @author David Aaron Suddjian
	 */
	public static class TypecodeMismatchException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		private int intendedTypeCode, foundTypeCode;

		public TypecodeMismatchException(int intendedTypeCode, int foundTypeCode) {
			super("SavefileTypecode Mismatch: " + intendedTypeCode + " called for, " +
					// If the found typecode is known, then the message will contain the name of that type. Otherwise, it will just say "unknown type"
					(foundTypeCode >= SavefileTypecode.values().length ? SavefileTypecode.values()[foundTypeCode].toString() : "unknown type") + " read");
			this.intendedTypeCode = intendedTypeCode;
			this.foundTypeCode = foundTypeCode;
		}

		public int getIntendedTypeCode() {
			return intendedTypeCode;
		}

		public int getFoundTypeCode() {
			return foundTypeCode;
		}
	}
}
