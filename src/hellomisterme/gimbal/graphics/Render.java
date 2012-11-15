package hellomisterme.gimbal.graphics;

import hellomisterme.gimbal.Err;
import hellomisterme.gimbal.Game;
import hellomisterme.gimbal.entities.Entity;
import hellomisterme.gimbal.io.ScreenshotManager;

import java.util.List;

/**
 * Render handles computation and combination of the pixels that go onto the screen.
 * 
 * Also handles methods to do with the visual contents of the entire screen, like screenshots.
 * 
 * @since 10-14-12
 * @author David Aaron Suddjian
 */
public class Render {
	
	private int width = Game.width;
	private int height = Game.height;
	public int[] pixels = new int[getWidth() * getHeight()];
	
	private ScreenshotManager screenshot;

	public Render(int width, int height) {
		this.setWidth(width);
		this.setHeight(height);
		pixels = new int[width * height];
		screenshot = new ScreenshotManager();
	}

	public void render(List<Entity> entities) {
		clear();
		for (int i = 0; i < entities.size(); i++) {
			Entity ent = entities.get(i);
			if (ent != null) {
				if (ent.getImage() != null) {
					render(ent.getImage(), ent.getX(), ent.getY());
				}
			} else {
				Err.error("Entity number " + i + " doesn't exist!");
			}
		}
	}

	/**
	 * Draws LightweightImage data at the indicated point on screen
	 * 
	 * @param image
	 *            the GimbalImage to be drawn
	 * @param xPos
	 *            the x coordinate of the top-left corner of the LightweightImage
	 * @param yPos
	 *            the y coordinate of the top-left corner of the LightweightImage
	 */
	public void render(GimbalImage image, int xPos, int yPos) {
		// check if img is completely off-screen
		if (xPos + image.getWidth() < 0 || yPos + image.getHeight() < 0 || xPos >= getWidth() || yPos >= getHeight()) {
			return;
		}

		// vars used to avoid printing unnecessary pixels or going out of bounds
		int xIndex; // x value to start at when copying pixels from img
		int xClip; // x value to stop at when copying pixels from img
		int yIndex; // y value to start at when copying pixels from img
		int yClip; // y value to stop at when copying pixels from img
		// OOB right
		if (xPos >= getWidth() - image.getWidth()) {
			xClip = getWidth() - xPos;
		} else {
			xClip = image.getWidth();
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
		if (yPos > getHeight() - image.getHeight()) {
			yClip = getHeight() - yPos;
		} else {
			yClip = image.getHeight();
		}

		yPos *= getWidth(); // yPos can now be used to shift the image down in the later equation without having to multiply by width every time

		// actual img copy loop
		for (int y = yIndex; y < yClip; y++) {
			for (int x = xIndex; x < xClip; x++) {
				int index = xPos + x + yPos + y * getWidth(); // the current location in pixels[]
				// calculate the new pixel value and put into pixels[]
				pixels[index] = blendRGB(pixels[index], image.getPixels()[x + y * image.getWidth()]);
			}
		}
	}

	/**
	 * Blends two colors (provided only one has an alpha channel) using the alpha value. Alpha values closer to 255 will make the returned color closer to `argb`, while alpha values closer to 0 will
	 * make it closer to `rgb`.
	 * 
	 * @param rgb
	 *            the rgb color to be blended (the "bottom" color)
	 * @param argb
	 *            the rgb color with alpha to be blended (the "top" color)
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
	 */
	public void clear() {
		// fill all pixels with black
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = 0xFFFFFF;
		}
	}
	
	public void screenshot() {
		screenshot.screenshot(pixels, width);
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}
}