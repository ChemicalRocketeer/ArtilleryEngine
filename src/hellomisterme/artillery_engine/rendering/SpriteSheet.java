package hellomisterme.artillery_engine.rendering;

import hellomisterme.artillery_engine.Err;
import hellomisterme.artillery_engine.components.images.PixelData;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

public class SpriteSheet {

	private class ImageWrapper {
		public String path;
		public BufferedImage bufferedImage;
		public PixelData pixelData;

		public ImageWrapper(String path, BufferedImage img, PixelData pixelData) {
			this.path = path;
			bufferedImage = img;
			this.pixelData = pixelData;
		}
	}

	private List<ImageWrapper> images;
	private Map<String, ImageWrapper> pathMap;

	public SpriteSheet() {
		images = new ArrayList<ImageWrapper>();
		pathMap = new HashMap<String, ImageWrapper>();
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
			pathMap.put(path, wrapper);
		} catch (IOException e) {
			Err.error("Can't read image from file " + path + "!", e);
		}
	}

	public void remove(String path) {
		for (ImageWrapper i : images) {
			if (i.path.equals(path)) images.remove(i);
		}
	}

	public void remove(int index) {
		images.remove(index);
	}

	public BufferedImage getBufferedImage(int index) {
		return images.get(index).bufferedImage;
	}

	public PixelData getPixelData(int index) {
		return images.get(index).pixelData;
	}

	public int getIndex(String path, boolean addImageIfNotFound) {
		for (int i = 0; i < images.size(); i++) {
			if (images.get(i).path.equals(path)) return i;
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
		ImageWrapper wrapper = pathMap.get(path);
		if (wrapper == null) {
			if (addImageIfNotFound) {
				addImage(path);
				return getBufferedImage(path, false);
			} else {
				return null;
			}
		}
		return wrapper.bufferedImage;
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
		ImageWrapper wrapper = pathMap.get(path);
		if (wrapper == null) {
			if (addImageIfNotFound) {
				addImage(path);
				return getPixelData(path, false);
			} else {
				return null;
			}
		}
		return wrapper.pixelData;
	}
}
