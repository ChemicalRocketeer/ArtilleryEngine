package hellomisterme.artillery_engine.rendering;

import hellomisterme.artillery_engine.Err;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * A PixelData efficiently stores data for pixels of an image.
 * 
 * There is no height variable. Height is calculated using the width variable and the length of the pixel array.
 * 
 * @since 10-14-12
 * @author David Aaron Suddjian
 */
public class PixelData {

	private final int width;
	private final int[] pixels;

	public PixelData(String path) {
		BufferedImage src;
		int w = 0; // using temporary variables to make sure width and pixels get initialized whether the try block works or not
		int[] p = new int[0];
		try {
			src = ImageIO.read(new File(path)); // read image from disk
			p = new int[src.getWidth() * src.getHeight()];
			src.getRGB(0, 0, src.getWidth(), src.getHeight(), p, 0, src.getWidth()); // copy colors from src to pixels
			w = src.getWidth();
		} catch (IOException e) {
			Err.error("Cant's read pixel data from " + path + "!");
			e.printStackTrace();
		}
		width = w;
		pixels = p;
	}

	/**
	 * Creates a PixelData object using the given pixels and width.
	 * Does not copy the pixels to a new array, do that before using this constructor.
	 * 
	 * @param pixels the pixels of the image
	 * @param width the width of the image
	 */
	public PixelData(int[] pixels, int width) {
		this.pixels = pixels;
		this.width = width;
	}

	/**
	 * Caution: this method allows the original pixels to be edited!
	 * 
	 * Can return null
	 * 
	 * @return this PixelData's pixel data
	 */
	public int[] getPixels() {
		return pixels;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return pixels.length / width;
	}
}
