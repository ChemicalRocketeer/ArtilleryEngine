package hellomisterme.artillery_engine.rendering;

import hellomisterme.artillery_engine.Err;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class SpriteSheet {

	private class ImageWrapper {
		public final String PATH;
		public final BufferedImage IMAGE;
		public final PixelData PIXEL_DATA;

		public ImageWrapper(String path, BufferedImage img, PixelData pixelData) {
			this.PATH = path;
			this.IMAGE = img;
			this.PIXEL_DATA = pixelData;
		}
	}

	private List<ImageWrapper> images;

	public SpriteSheet() {
		images = new ArrayList<ImageWrapper>();
	}

	/**
	 * @return the number of images stored in this spritesheet
	 */
	public int size() {
		return images.size();
	}

	public void addImage(String path) {
		try {
			BufferedImage img = ImageIO.read(new File(path));
			ImageWrapper wrapper = new ImageWrapper(path, img, new PixelData(img.getRGB(0, 0, img.getWidth(), img.getHeight(), null, 0, img.getWidth()), img.getWidth()));
			images.add(wrapper);
		} catch (IOException e) {
			Err.error("Can't read image from file " + path + "!", e);
		}
	}

	public void remove(String path) {
		// TODO use iterator
		for (ImageWrapper i : images) {
			if (i.PATH.equals(path))
				images.remove(i);
		}
	}

	public void remove(int index) {
		images.remove(index);
	}

	public BufferedImage getBufferedImage(int index) {
		return images.get(index).IMAGE;
	}

	public PixelData getPixelData(int index) {
		return images.get(index).PIXEL_DATA;
	}

	public int getIndex(String path, boolean addImageIfNotFound) {
		for (int i = 0; i < images.size(); i++) {
			if (images.get(i).PATH.equals(path))
				return i;
		}
		// nothing found, so create add the image
		if (addImageIfNotFound) {
			addImage(path);
			return getIndex(path, false);
		} else {
			return -1;
		}
	}

	/**
	 * Gets a BufferedImage based on the path name. Not an especially efficient way to retrieve an image. Use of the index number is advised instead.
	 * If addImageIfNotFound is true, then if the image is not found in this SpriteSheet, it will be added to the SpriteSheet and returned.
	 * 
	 * @param path the path of the image to return
	 * @param addImageIfNotFound true to add the image from the given path if it is not already in this SpriteSheet
	 * @return the BufferedImage located at the given path
	 */
	public BufferedImage getBufferedImage(String path, boolean addImageIfNotFound) {
		for (ImageWrapper w : images) {
			if (w.PATH.equals(path))
				return w.IMAGE;
		}
		if (addImageIfNotFound) {
			addImage(path);
			return images.get(images.size() - 1).IMAGE;
		} else {
			return null;
		}
	}

	/**
	 * Gets a PixelData based on the path name. Not an efficient way to retrieve an image. Use of the index number is advised instead.
	 * If addImageIfNotFound is true, then if the image is not found in this SpriteSheet, it will be added to the SpriteSheet and returned.
	 * 
	 * @param path the path of the image to return
	 * @param addImageIfNotFound true to add the image from the given path if it is not already in this SpriteSheet
	 * @return the BufferedImage located at the given path
	 */
	public PixelData getPixelData(String path, boolean addImageIfNotFound) {
		for (ImageWrapper w : images) {
			if (w.PATH.equals(path))
				return w.PIXEL_DATA;
		}
		if (addImageIfNotFound) {
			addImage(path);
			return images.get(images.size() - 1).PIXEL_DATA;
		} else {
			return null;
		}
	}
}
