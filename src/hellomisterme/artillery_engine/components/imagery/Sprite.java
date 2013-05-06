package hellomisterme.artillery_engine.components.imagery;

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
public class Sprite extends ImageShell {
	protected int width;
	protected int[] pixels;

	public static Sprite create(String path) {
		Sprite sprite = new Sprite();
		sprite.setImage(path);
		return sprite;
	}

	/**
	 * Loads color data from an image file at the relative path
	 * 
	 * @param path
	 *            the path to the image file
	 */
	@Override
	public void setImage(String path) {
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
	@Override
	public int[] getPixels() {
		return pixels;
	}

	@Override
	public int getWidth() {
		return width;
	}

	protected void setWidth(int w) {
		width = w;
	}

	@Override
	public int getHeight() {
		return pixels.length / width;
	}
}
