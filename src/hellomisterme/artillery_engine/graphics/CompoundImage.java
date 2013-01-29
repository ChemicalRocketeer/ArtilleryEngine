package hellomisterme.artillery_engine.graphics;

import hellomisterme.artillery_engine.Err;
import hellomisterme.artillery_engine.io.Savable;

import java.awt.Graphics2D;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A CompoundImage stores several images as one for faster image processing, but changing it takes time.
 * 
 * @since 1-15-13
 * @author David Aaron Suddjian
 */

public class CompoundImage extends Render implements BasicImage, Renderable, Savable {

	/**
	 * Cartesian Coordinate
	 */
	public int x = 0, y = 0;

	private ArrayList<ImageData> images;

	/**
	 * Generates a new CompoundImage with the desired width and height
	 * 
	 * @param width
	 * @param height
	 */
	public CompoundImage(int width, int height) {
		super(width, height);
		images = new ArrayList<ImageData>();
		pixels = new int[width * height];
	}

	/**
	 * Takes all the images represented and combines them
	 */
	public void compile() {
		for (int i : pixels) {
			pixels[i] = 0xFFFFFFFF;
		}
		for (ImageData img : images) {
			render(new Sprite(img.path), img.x, img.y);
		}
	}

	public void render(Render r, Graphics2D g) {
		r.render(this, x, y);
	}

	public void add(String path, int x, int y) {
		images.add(new ImageData(path, x, y));
	}

	/**
	 * Writes the number of images represented by this CompoundImage, then calls each image's save method.
	 */
	@Override
	public void save(DataOutputStream out) {
		try {
			out.writeInt(getWidth());
			out.writeInt(getHeight());
			out.writeInt(x);
			out.writeInt(y);
			out.writeInt(images.size()); // write the number of images there are
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (ImageData img : images) { // save each image
			img.save(out);
		}
	}

	/**
	 * Loads saved data
	 * 
	 * @see #save(DataOutputStream)
	 */
	@Override
	public void load(DataInputStream in, String version) {
		int i = 0;
		try {
			setSize(in.readInt(), in.readInt());
			x = in.readInt();
			y = in.readInt();
			i = in.readInt(); // read how many images there are
		} catch (IOException e) {
			Err.error("Can't read CompoundImage data!");
			e.printStackTrace();
		}

		images = new ArrayList<ImageData>(i);
		for (; i > 0; i--) {
			images.set(i, new ImageData(in, version)); // load each image
		}

		compile();
	}

	public int[] getPixels() {
		return pixels;
	}
}
