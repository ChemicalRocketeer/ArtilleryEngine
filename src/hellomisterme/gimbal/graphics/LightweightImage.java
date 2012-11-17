package hellomisterme.gimbal.graphics;

import hellomisterme.gimbal.Err;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * A LightweightImage efficiently stores image data. Creates a blank white 20x20 square by default.
 * 
 * There is no height variable. Height is calculated using the width variable and the length of the pixel array.
 * 
 * @since 10-14-12
 * @author David Aaron Suddjian
 */
public class LightweightImage implements GimbalImage {
	protected int width;
	protected int[] pixels;
	protected String filePath;

	public LightweightImage(String path) {
		setImage(path);
		filePath = path;
	}

	/**
	 * Has to be here so the LightwightImage can be initialized and then manipulated without wasting system resources.
	 */
	public LightweightImage() {
		useDefaultImage();
	}

	/**
	 * Saves this LightweightImage's filepath (not the actual image data).
	 */
	public void save(DataOutputStream out) {
		try {
			out.writeUTF(filePath);
		} catch (IOException e) {
			Err.error("LightweightImage can't save data!");
			e.printStackTrace();
		}
	}

	/**
	 * Loads data from the image at whatever filepath is read.
	 */
	public void load(DataInputStream in) {
		try {
			setImage(in.readUTF());
		} catch (IOException e) {
			Err.error("LightweightImage can't read data!");
		}
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
			src.getRGB(0, 0, src.getWidth(), src.getHeight(), pixels, 0, src.getWidth()); // copy colors from src to pixels
			width = src.getWidth();
			filePath = path;
		} catch (IOException e) {
			Err.error("LightweightImage can't read " + path + "!");
			e.printStackTrace();
		}
	}

	/**
	 * Sets this LightweightImage to the default 20x20 white square.
	 */
	public void useDefaultImage() {
		filePath = "";
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
	 * @return this LightweightImage's pixel data
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
