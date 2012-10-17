package possiblydavid.gimbal.graphics;

/**
 * An LightweightImage efficiently stores image data. Currently creates a blank
 * white 20x20 square.
 * 
 * @author David Aaron Suddjian
 */
public class LightweightImage {
	private int width, height;
	private int[] pixels;

	public LightweightImage() {
		useDefaultImage();
	}

	/**
	 * Sets this LightweightImage to the default 20x20 white square.
	 */
	public void useDefaultImage() {
		width = 20;
		height = 20;
		for (int i = 0; i < width * height; i++) {
			pixels[i] = 0xFFFFFF;
		}
	}

	/**
	 * Caution: this method allows the original pixels to be edited!
	 * 
	 * If you do not intend to change color values but are writing or using a
	 * method that may change them, try getPixelCopy instead.
	 * 
	 * @return this LightweightImage's original pixel data
	 */
	public int[] getPixels() {
		return pixels;
	}

	/**
	 * @return a copy of this LightweightImage's pixel data
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
		return height;
	}
}
