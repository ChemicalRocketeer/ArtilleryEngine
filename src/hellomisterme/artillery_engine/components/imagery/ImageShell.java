package hellomisterme.artillery_engine.components.imagery;

import hellomisterme.artillery_engine.Err;
import hellomisterme.artillery_engine.components.IngameComponent;
import hellomisterme.artillery_engine.rendering.Render;
import hellomisterme.artillery_engine.rendering.Renderable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * A class dealing with image data.
 * 
 * @since 11-12-12
 * @author David Aaron Suddjian
 */
public abstract class ImageShell extends IngameComponent implements Renderable {

	public boolean visible = true;
	protected String path;

	@Override
	public void render(Render render) {
		if (visible) {
			render.render(this, globalPosition(), Render.INGAME_CAMERA);
		}
	}

	public abstract void setImage(String path);

	/**
	 * Returns all the pixels of this ImageShell as ints.
	 * 
	 * @return an int[] of all the pixels
	 */
	public abstract int[] getPixels();

	/**
	 * Returns the width value of this ImageShell.
	 * 
	 * @return the width value
	 */
	public abstract int getWidth();

	/**
	 * Returns the height value of this ImageShell.
	 * 
	 * @return the height value
	 */
	public abstract int getHeight();

	@Override
	public void write(DataOutputStream out) {
		try {
			out.writeBoolean(visible);
			out.writeUTF(path);
		} catch (IOException e) {
			Err.error("ImageShell can't write!", e);
		}
	}

	@Override
	public void read(DataInputStream in) {
		super.read(in);
		try {
			visible = in.readBoolean();
			path = in.readUTF();
		} catch (IOException e) {
			Err.error("ImageShell can't read!", e);
		}
	}
}
