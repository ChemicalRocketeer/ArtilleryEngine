package possiblydavid.gimbal.graphics;

/**
 * A LightImg efficiently stores image data. Currently creates a blank white 20x20 square.
 * There is no height variable. Height is calculated using the width variable and the length of the pixel array.
 * 
 * @author David Aaron Suddjian
 */
public class LightImg {
	private int width;
	private int[] pixels;

	public LightImg() {
		useDefaultImage();
	}

	/**
	 * Sets this LightImg to the default 20x20 white square.
	 */
	public void useDefaultImage() {
		width = 20;
		pixels = new int[width*width];
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
