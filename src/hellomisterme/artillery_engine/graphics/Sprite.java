package hellomisterme.artillery_engine.graphics;

import hellomisterme.artillery_engine.Err;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * A Sprite efficiently stores data for an unanimated sprite. Creates a blank white 20x20 square by default.
 * 
 * There is no height variable. Height is calculated using the width variable and the length of the pixel array.
 * 
 * @since 10-14-12
 * @author David Aaron Suddjian
 */
public class Sprite implements BasicImage {
	protected int width;
	protected int[] pixels;

	/**
	 * Creates a default Sprite by calling useDefaultImage
	 * 
	 * @see #useDefaultImage()
	 */
	public Sprite() { // has to be here so the LightwightImage can be initialized and then manipulated without wasting system resources.
		useDefaultImage();
	}
	
	/**
	 * Creates a new Sprite from the specified path
	 * 
	 * @param path the path to load the image from
	 */
	public Sprite(String path) {
		loadImage(path);
	}

	/**
	 * Loads color data from an image file at the relative path
	 * 
	 * TODO check for efficiency
	 * 
	 * @param path
	 *            the path to the image file
	 */
	public void loadImage(String path) {
		BufferedImage src;
		try {
			src = ImageIO.read(new File(path)); // read image file from disk
			pixels = new int[src.getWidth() * src.getHeight()];
			src.getRGB(0, 0, src.getWidth(), src.getHeight(), pixels, 0, src.getWidth()); // copy colors from src to pixels
			width = src.getWidth();
		} catch (IOException e) {
			Err.error("Sprite can't read " + path + "!");
			e.printStackTrace();
		}
	}

	/**
	 * Sets this Sprite to the default 20x20 white square.
	 */
	public void useDefaultImage() {
		width = 20;
		pixels = new int[width * width];
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = 0xCCFFFFFF;
		}
	}

	/**
	 * Caution: this method allows the original pixels to be edited!
	 * 
	 * Can return null
	 * 
	 * @return this Sprite's pixel data
	 */
	public int[] getPixels() {
		return pixels;
	}

	public int getWidth() {
		return width;
	}

	protected void setWidth(int w) {
		width = w;
	}

	public int getHeight() {
		return pixels.length / width;
	}
}
