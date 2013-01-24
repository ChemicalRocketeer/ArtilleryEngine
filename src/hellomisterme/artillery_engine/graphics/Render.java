package hellomisterme.artillery_engine.graphics;

import hellomisterme.artillery_engine.Game;

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

	/**
	 * The array of pixels that will be manipulated through rendering
	 */
	public int[] pixels;

	public Render(int width, int height) {
		this.width = width;
		this.height = height;
	}

	/**
	 * Draws Sprite data at the indicated point on screen
	 * 
	 * @param image the BasicImage to be drawn
	 * @param xPos the xLocation coordinate of the top-left corner of the Sprite
	 * @param yPos the yLocation coordinate of the top-left corner of the Sprite
	 */
	public void render(BasicImage image, int xPos, int yPos) {
		// check if img is completely off-screen
		if (xPos + image.getWidth() < 0 || yPos + image.getHeight() < 0 || xPos >= getWidth() || yPos >= getHeight()) {
			return;
		}

		// vars used to avoid printing unnecessary pixels or going out of bounds
		int xIndex; // xLocation value to start at when copying pixels from img
		int xClip; // xLocation value to stop at when copying pixels from img
		int yIndex; // yLocation value to start at when copying pixels from img
		int yClip; // yLocation value to stop at when copying pixels from img
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

		/* ----- 
		
		//old pixel render loop, not as efficient but included becuase it does the same thing (or at least it should!) and is more readable
		
		yPos *= getWidth(); // yPos can now be used to shift the image down in the later equation without having to multiply by width every time
		
		for (int y = yIndex; y < yClip; y++) {
			for (int x = xIndex; x < xClip; x++) {
				int index = xPos + x + yPos + y * getWidth(); // the current location in pixels[]
				// calculate the new pixel value and put into pixels[]
				pixels[index] = blendRGB(pixels[index], image.getPixels()[x + y * image.getWidth()]);
			}
		}

		/* ----- */

		// variables to decrease computation time during the loop
		int[] imgPix = image.getPixels();
		int w = getWidth();
		int imgW = image.getWidth();
		int imgYpix = 0;
		int xPixel = xPos; 
		yPos *= getWidth();

		// actual pixel render loop
		for (int y = yIndex; y < yClip; y++, yPos += w, imgYpix += imgW, xPixel = xPos) {
			int index = 0;
			for (int x = xIndex; x < xClip; x++, xPixel++) {
				index = xPixel + yPos; // the current location in pixels[]
				// calculate the new pixel value and put into pixels[]
				this.pixels[index] = blendRGB(this.pixels[index], imgPix[x + imgYpix]);
			}
		}
	}

	/**
	 * Blends two rgb colors (provided one has an alpha channel) using the alpha value. Alpha values closer to 255 will make the returned color closer to argb, while alpha values closer to 0 will make
	 * it closer to rgb.
	 * 
	 * @param rgb the rgb color to be blended (the "bottom" color)
	 * @param argb the rgb color with alpha to be blended (the "top" color)
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
		// return hex color made of the calculated RGB values: R << 16 OR G << 8 OR B
		return ((int) (((argb >> 16 & 0xFF) - r) * alpha + r) << 16) | ((int) (((argb >> 8 & 0xFF) - g) * alpha + g) << 8) | (int) (((argb & 0xFF) - b) * alpha + b);
	}

	/**
	 * Clears pixels so they can be filled later
	 */
	public void clear() {
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = 0xFFFFFF;
		}
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	/**
	 * Resets the width and height of this Render. If pixels[] is shared another object, calling this will break the link.
	 * 
	 * @param w the new width
	 * @param h the new height
	 */
	public void setSize(int w, int h) {
		width = w;
		height = h;
		pixels = new int[width * height];
	}
}