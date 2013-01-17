package hellomisterme.gimbal.graphics;

import hellomisterme.gimbal.Err;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

/**
 * An AnimatedSprite represents a series of images. It has a "current frame" that can be changed by cycling through the animation, and is returned as the Sprite's image representation. The animation
 * starts at frame 0.
 * 
 * All the frames must have the same dimensions, otherwise results are unpredictable.
 * 
 * @since 11-11-12
 * @author David Aaron Suddjian
 */
public class AnimatedSprite implements GimbalImage {

	protected int frame = 0; // the current frame
	private int width;
	protected int[][] frames;

	/**
	 * Uses the default encoded image.
	 */
	public AnimatedSprite() {
		useDefaultImage();
	}

	/**
	 * This AnimatedSprite will use the frames found at the specified path.
	 * 
	 * @param path
	 *            The path of the directory where the images are located
	 * @see #setImage(String)
	 */
	public AnimatedSprite(String path) {
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
					Err.error("GimbalImage can't read " + file.getAbsolutePath() + "! Make sure all the game files are correctly named!");
					e.printStackTrace();
				}
			}
			width = src.getWidth(); // set width to the width of the last image
		} else {
			Err.error(path + "Does not exist! Make sure your game files are all in the right place!");
		}
	}

	/**
	 * Switches to the next frame.
	 */
	public void next() {
		frame++;
		if (frame >= frames.length) {
			frame = 0;
		}
	}

	/**
	 * Generates a default animation of 2 frames.
	 */
	public void useDefaultImage() {
		frame = 0;
		width = 20;
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
		if (frames.length == 0) {
			frame = 0;
		} else {
			frame = f % frames.length;
		}
	}

	/**
	 * @return the current frame
	 */
	public int getFrame() {
		return frame;
	}

	/**
	 * @return the pixels of the current frame. Can return null!
	 */
	public int[] getPixels() {
		return frames[frame];
	}

	/**
	 * @return the width in pixels of this AnimatedSprite
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return the height in pixels of this AnimatedSprite
	 */
	public int getHeight() {
		return frames[frame].length / width;
	}
}
