package hellomisterme.gimbal.graphics;

import hellomisterme.gimbal.Err;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;

import javax.imageio.ImageIO;

/**
 * A LightweightAnimation represents a series of images. It has a "current frame" that can be changed by cycling through the animation, and is returned as the LightweightImage's image representation.
 * The animation starts at frame 0.
 * 
 * All the frames must have the same dimensions, otherwise results are unpredictable.
 * 
 * @since 11-11-12
 * @author David Aaron Suddjian
 */
public class LightweightAnimation extends GimbalImage {

	protected int frame = 0; // the current frame
	private int width;
	protected String filePath;
	protected int[][] frames;

	public LightweightAnimation() {
		useDefaultImage();
	}

	public LightweightAnimation(String path) {
		setImage(path);
	}

	/**
	 * Writes the data necessary to reconstruct this LightweightAnimation. Data is arranged as follows:
	 * 
	 * <ul>
	 * <li>The current frame as an int</li>
	 * <li>The width as an int</li>
	 * <li>A boolean true if filePath exists, else false</li>
	 * <li>If the filePath exists, writes it as UTF</li>
	 */
	public void save(DataOutputStream out) {
		try {
			out.writeInt(frame);
			out.writeInt(width);
			if (filePath != null) {
				out.writeBoolean(true);
				out.writeUTF(filePath);
			} else {
				out.writeBoolean(false);
			}
		} catch (Exception e) {
			Err.error("Can't save LightweightAnimation data!");
			e.printStackTrace();
		}
	}

	public void load(DataInputStream in) { // TODO fix
		try {
			frame = in.readInt();
			width = in.readInt();
			if (in.readBoolean()) {
				setImage(in.readUTF());
			}
		} catch (Exception e) {
			Err.error("Can't load LightweightAnimation data!");
			e.printStackTrace();
		}
	}

	/**
	 * Loads the frames of an animation from the specified directory location. The location must be a folder, not a file.
	 */
	public void setImage(String path) {
		if (new File(path).isDirectory()) {
			int fileCount = 0;
			while (new File(path + "/" + fileCount + ".png").exists()) {
				fileCount++;
			}
			frames = new int[fileCount][];
			
			for (int i = 0; i < fileCount; i++) {
				File file = new File(path + "/" + i + ".png");
				try {
					BufferedImage src = ImageIO.read(file); // read image file from disk
					if (src == null) {
						Err.error("File format of frame " + i + " not recognized in LightweightAnimation!");
						useDefaultImage();
						return;
					}
					filePath = path;
					frames[i] = extractPixels(src);
					setWidth(src.getWidth());
				} catch (Exception e) {
					Err.error("GimbalImage can't read " + file.getAbsolutePath() + "!");
					useDefaultImage();
					e.printStackTrace();
				};
			}
			
		} else {
			Err.error("LightweightAnimation cannot read data from " + path);
			useDefaultImage();
		}
	}

	/**
	 * Switches to the next frame
	 */
	public void next() {
		frame++;
		if (frame >= frames.length) {
			frame = 0;
		}
	}

	/**
	 * Generates a default animation of 2 frames
	 */
	public void useDefaultImage() {
		frame = 0;
		width = 20;
		filePath = "";
		frames = new int[2][width * width];
		for (int i = 0; i < width * width; i++) {
			frames[0][i] = 0x33FFFFFF;
			frames[1][i] = 0xAAFFFFFF;
		}
	}

	/**
	 * Sets the current frame to the specified number. Will loop if the number specified is greater than the number of frames.
	 * 
	 * @param f
	 *            the number of the desired frame
	 */
	public void setFrame(int f) {
		if (f < 0) {
			Err.error("Trying to use a negative frame in animation " + filePath);
			return;
		}
		if (frames.length == 0) {
			frame = 0;
		} else {
			frame = f % frames.length;
		}
	}

	public int getFrame() {
		return frame;
	}

	/**
	 * Returns the pixels of the current frame. Can return null!
	 */
	public int[] getPixels() {
		return frames[frame];
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int w) {
		width = w;
	}

	public int getHeight() {
		return frames[frame].length / width;
	}
}
