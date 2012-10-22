package possiblydavid.gimbal.graphics;

import java.util.List;

import possiblydavid.gimbal.entities.Entity;

/**
 * Render handles computation of the pixels that go onto the screen.
 * 
 * @author David Aaron Suddjian
 */
public class Render {

	private int width;
	private int height;
	public int[] pixels;

	public Render(int width, int height) {
		this.width = width;
		this.height = height;
		pixels = new int[width * height];
	}

	public void render(List<Entity> entities) {
		for (int i = 0; i < entities.size(); i++) {
			render(entities.get(i).getImage(), entities.get(i).getX(), entities.get(i).getY());
		}
	}

	/**
	 * Draws LightweightImage data at the indicated point on screen
	 * 
	 * @param img
	 *            the LightweightImage to be drawn
	 * @param xPos
	 *            the x coordinate of the top-left corner of the LightweightImage
	 * @param yPos
	 *            the y coordinate of the top-left corner of the LightweightImage
	 */
	public void render(LightweightImage img, int xPos, int yPos) {
		// check if img is completely off-screen
		if (xPos + img.getWidth() < 0 || yPos + img.getHeight() < 0 || xPos >= width || yPos >= height) {
			return;
		}

		// vars used to avoid printing unnecessary pixels or going out of bounds
		int xIndex; // x value to start at when copying pixels from img
		int xClip; // x value to stop at when copying pixels from img
		int yIndex; // y value to start at when copying pixels from img
		int yClip; // y value to stop at when copying pixels from img
		// OOB right
		if (xPos >= width - img.getWidth()) {
			xClip = width - xPos;
		} else {
			xClip = img.getWidth();
		}
		// OOB left
		if (xPos < 0) {
			xIndex = -xPos;
		} else {
			xIndex = 0;
		}
		// OOB top
		if (yPos < 0) {
			yIndex = -yPos;
		} else {
			yIndex = 0;
		}
		// OOB bottom
		if (yPos > height - img.getHeight()) {
			yClip = height - yPos;
		} else {
			yClip = img.getHeight();
		}

		yPos *= width; // yPos can now be used to shift the image down in the later equation without having to multiply by width every time

		// actual img copy loop
		for (int y = yIndex; y < yClip; y++) {
			for (int x = xIndex; x < xClip; x++) {
				int index = xPos + x + yPos + y * width; // the current location in pixels[]
				// calculate the new pixel value and put into pixels[]
				pixels[index] = blendRGB(pixels[index], img.getPixels()[x + y * img.getWidth()]);
			}
		}
	}

	/**
	 * Blends two colors (provided only one has an alpha channel) using the alpha value. Alpha values closer to 255 will make the returned color closer to `argb`, while alpha values closer to 0 will
	 * make it closer to `rgb`.
	 * 
	 * @param rgb
	 *            the rgb color to be blended (if there is a "bottom" color, it's probably this)
	 * @param argb
	 *            the rgb color with alpha to be blended (this is probably the "top" color, if there is one)
	 * @return the two colors blended together
	 */
	public static int blendRGB(int rgb, int argb) {
		if (rgb == (argb & 0xFFFFFF)) { // if the colors are the same
			return rgb;
		}

		int a = (argb >> 24) & 0xFF; // hex alpha value of argb

		if (a == 0) { // if argb is transparent, no action necessary
			return rgb;
		} else if (a == 0xFF) { // argb is 100% opaque
			return argb & 0xFFFFFF; // return rgb-only value of argb
		}

		float alpha = (float) (a / 255.0); // alpha value of argb between 0 and 1, has to be a float or it will only be 0 or 1!
		// individual sub-pixel values of rgb
		int r = (rgb >> 16) & 0xFF;
		int g = (rgb >> 8) & 0xFF;
		int b = rgb & 0xFF;
		// (argb >> number & 0xFF) is the sub-pixel value of argb.
		// return hex color made of the calculated RGB values: R << 16 or G << 8 or B
		return ((int) (((argb >> 16 & 0xFF) - r) * alpha + r) << 16) | ((int) (((argb >> 8 & 0xFF) - g) * alpha + g) << 8) | (int) (((argb & 0xFF) - b) * alpha + b);
	}

	/**
	 * Clears pixels so they can be filled later
	 * 
	 * TODO: fill with black for final game instead of pink for testing
	 */
	public void clear() {
		// fill all pixels with pink
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = 0xFF00FF;
		}
	}
}