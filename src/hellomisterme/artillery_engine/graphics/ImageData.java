package hellomisterme.artillery_engine.graphics;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import hellomisterme.artillery_engine.Err;
import hellomisterme.artillery_engine.io.Savable;

/**
 * ImageData stores data that can be used to build an Entity.
 * 
 * @since 1-15-13
 * @author David Aaron Suddjian
 */
public class ImageData implements Savable {
	public String path;
	public int x, y;
	
	public ImageData(String path, int x, int y) {
		this.path = path;
		this.x = x;
		this.y = y;
	}

	/**
	 * Uses a save file to generate image data instead of 
	 * 
	 * @param in
	 * @param version
	 */
	public ImageData(DataInputStream in, String version) {
		load(in, version);
	}

	@Override
	public void save(DataOutputStream out) {
		try {
			out.writeUTF(path);
			out.writeInt(x);
			out.writeInt(y);
		} catch (IOException e) {
			Err.error("Can't write CompoundImage.ImageData!");
			e.printStackTrace();
		}
	}

	@Override
	public void load(DataInputStream in, String version) {
		try {
			path = in.readUTF();
			x = in.readInt();
			y = in.readInt();
		} catch (IOException e) {
			Err.error("Can't read CompoundImage.ImageData!");
			e.printStackTrace();
		}
	}
}
