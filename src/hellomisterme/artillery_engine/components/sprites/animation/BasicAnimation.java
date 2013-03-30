package hellomisterme.artillery_engine.components.sprites.animation;

import hellomisterme.artillery_engine.Err;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

/**
 * An BasicAnimation represents a series of images. It has a "current frame" that can be changed by cycling through the animation, and is returned as the Sprite's image representation. The animation
 * starts at frame 0.
 * 
 * All the frames must have the same dimensions, otherwise results are unpredictable.
 * 
 * @since 11-11-12
 * @author David Aaron Suddjian
 */
public class BasicAnimation implements Animation {

	private int width, height;
	protected int[][] frames;

	/**
	 * Uses the default encoded image.
	 */
	public BasicAnimation() {
		useDefaultImage();
	}

	/**
	 * This BasicAnimation will use the frames found at the specified path.
	 * 
	 * @param path
	 *            The path of the directory where the images are located
	 * @see #setImage(String)
	 */
	public BasicAnimation(String path) {
		setImage(path);
	}

	/**
	 * Loads the frames of an animation from the specified path. The path must be a directory, not a file.
	 * 
	 * Each frame in the animation must be saved as its own image file, in png format. The files must be saved as 0.png, 1.png, etc. for as many files as there are frames in the animation.
	 * 
	 * @param path
	 *            The path of the directory where the images are located
	 */
	public void setImage(String path) {
		if (new File(path).isDirectory()) {
			int fileCount = 0;
			while (new File(path + "/" + fileCount + ".png").exists()) {
				fileCount++;
			}
			frames = new int[fileCount][];

			BufferedImage src = null;
			for (int i = 0; i < frames.length; i++) {
				File file = new File(path + "/" + i + ".png");
				try {
					src = ImageIO.read(file); // read image file from disk - if this returns null, it's because the image is unreadable
					frames[i] = new int[src.getWidth() * src.getHeight()];
					src.getRGB(0, 0, src.getWidth(), src.getHeight(), frames[i], 0, src.getWidth()); // copy colors from src to pixels
				} catch (Exception e) {
					Err.error("BasicImage can't read " + file.getAbsolutePath() + "! Make sure all the game files are correctly named!");
					e.printStackTrace();
				}
			}
			width = src.getWidth(); // set width to the width of the last image
			width = src.getHeight(); // set width to the width of the last image
		} else {
			Err.error(path + "Does not exist! Make sure your game files are all in the right place!");
		}
	}

	/**
	 * Generates a default animation of 2 frames.
	 */
	public void useDefaultImage() {
		width = 20;
		height = 20;
		frames = new int[2][width * height];
		for (int i = 0; i < width * height; i++) {
			frames[0][i] = 0x33FFFFFF;
			frames[1][i] = 0xAAFFFFFF;
		}
	}

	/**
	 * @return the pixels of the current frame. Can return null!
	 */
	@Override
	public int[] getPixels(int frame) {
		return frames[frame];
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}
}
