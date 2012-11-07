package hellomisterme.gimbal.graphics;

import hellomisterme.gimbal.Err;
import hellomisterme.gimbal.io.Savable;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


/**
 * A LightweightImage efficiently stores image data. Currently creates a blank white 20x20 square.
 * 
 * There is no height variable. Height is calculated using the width variable and the length of the pixel array.
 * 
 * @since 10-14-12
 * @author David Aaron Suddjian
 */
public class LightweightImage implements Savable {
	private int width;
	private int[] pixels;
	private String filePath;
	
	public LightweightImage(String path) {
		setImage(path);
		filePath = path;
	}

	/**
	 * Has to be here so the LightwightImage can be initialized without a call by Entity's subclasses
	 */
	public LightweightImage() {
		
	}
	
	public void save(DataOutputStream out) {
		try {
			out.writeUTF(filePath);
		} catch (IOException e) {
			Err.error("LightweightImage can't save data!");
			e.printStackTrace();
		}
	}
	
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
			filePath = path;
		} catch (IOException e) {
			Err.error("LightweightImage can't read " + path + "!");
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
	 * @return this LightweightImage's pixel data
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
