package possiblydavid.gimbal.graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * A LightImg efficiently stores image data. Currently creates a blank white 20x20 square.
 * 
 * There is no height variable. Height is calculated using the width variable and the length of the pixel array.
 * 
 * TODO add transparency support
 * 
 * @author David Aaron Suddjian
 */
public class LightImg {
	private int width;
	private int[] pixels;

	public LightImg() {
		setImage("art/test.png");
	}

	/**
	 * Loads color data from an image file at the relative path 
	 * 
	 * TODO check for efficiency
	 * 
	 * @param path
	 *            the path to the image file
	 */
	public void setImage(String path) {
		BufferedImage src;
		try {
			src = ImageIO.read(new File(path)); // read image file from disk
		} catch (IOException e) {
			// TODO replace with permanent solution
			e.printStackTrace();
			useDefaultImage(); // can't get the correct image, so use default
			return; // can't do the rest of this method, so exit
		}
		width = src.getWidth();
		pixels = new int[src.getWidth() * src.getHeight()]; // set pixels length
		// copy colors from src to pixels
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				pixels[x + y * width] = src.getRGB(x, y);
			}
		}
	}

	/**
	 * Converts an ARGB int to an RGB int
	 * 
	 * TODO check for efficiency
	 * 
	 * @param argb
	 *            the into to convert
	 * @return an RGB format int
	 */
	public int convertARGBtoRGB(int argb) {
		int a = (argb >> 24) & 0xFF;
		int r = (argb >> 16) & 0xFF;
		int g = (argb >> 8) & 0xFF;
		int b = argb & 0xFF;
		return (r << 16) | (g << 8) | b;
	}

	/**
	 * Sets this LightImg to the default 20x20 white square.
	 */
	public void useDefaultImage() {
		width = 20;
		pixels = new int[width * width];
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = 0xFFFFFF;
		}
	}

	/**
	 * Caution: this method allows the original pixels to be edited!
	 * 
	 * If you do not intend to change color values but are writing or using a method that may change them, try getPixelCopy instead.
	 * 
	 * @return this LightImg's original pixel data
	 */
	public int[] getPixels() {
		return pixels;
	}

	/**
	 * @return a copy of this LightImg's pixel data
	 */
	public int[] getPixelCopy() {
		int[] copy = new int[pixels.length];
		for (int i = 0; i < pixels.length; i++) {
			copy[i] = pixels[i];
		}
		return copy;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return pixels.length / width;
	}
}
