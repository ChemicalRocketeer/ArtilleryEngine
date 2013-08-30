package hellomisterme.artillery_engine.rendering;

import hellomisterme.artillery_engine.components.images.PixelData;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class Screen {

	private int width;
	private int height;

	/** Flag to tell objects how they should render themselves */
	public boolean simpleRendering = false;

	public static final RenderingHints speedHints = getSpeedHints();

	private static RenderingHints getSpeedHints() {
		RenderingHints hints = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
		hints.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
		hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		hints.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		hints.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
		hints.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		hints.put(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		return hints;
	}

	public static final RenderingHints qualityHints = getQualityHints();

	private static RenderingHints getQualityHints() {
		RenderingHints hints = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		hints.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		hints.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		hints.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		hints.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		hints.put(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
		return hints;
	}

	public RenderingHints renderHints = qualityHints;

	public int[] pixels;
	public BufferedImage image;
	public Graphics2D graphics;

	/**
	 * Draws image data at the indicated point on screen
	 * 
	 * @param img the ImageShell to be drawn
	 * @param xPos the x coordinate of the top-left corner of the PixelData
	 * @param yPos the y coordinate of the top-left corner of the PixelData
	 */
	public void render(PixelData img, int xPos, int yPos, int cameraMode) {
		setCameraMode(cameraMode);
		Point trans = new Point(xPos, yPos);
		getActiveCamera().view.transform(trans, trans);
		xPos = trans.x;
		yPos = trans.y;

		// check if img is completely off-screen
		if (xPos + img.getWidth() < 0 || yPos + img.getHeight() < 0 || xPos >= getWidth() || yPos >= getHeight()) {
			return;
		}

		// vars used to avoid printing unnecessary pixels or going out of bounds
		int xIndex; // xLocation value to start at when copying pixels from img
		int xClip; // xLocation value to stop at when copying pixels from img
		int yIndex; // yLocation value to start at when copying pixels from img
		int yClip; // yLocation value to stop at when copying pixels from img

		// OOB right
		if (xPos >= width - img.getWidth()) {
			xClip = width; // clip at our right border instead of the border of the image, which is too far right
		} else {
			xClip = img.getWidth() + xPos; // clip at the right border of the image
		}
		// OOB left
		if (xPos < 0) {
			xIndex = 0; // start at our left border because the image is too far left
		} else {
			xIndex = xPos; // start at the left border of the image
		}
		// OOB up
		if (yPos < 0) {
			yIndex = 0; // start at our top border because the image is too far up
		} else {
			yIndex = yPos; // start at the top border of the image
		}
		// OOB down
		if (yPos > height - img.getHeight()) {
			yClip = height; // clip at our bottom border because the image is too far down
		} else {
			yClip = img.getHeight() + yPos; // clip at the bottom border of the image
		}

		int[] imgPixels = img.getPixels();
		int imgYBlock = (yIndex - yPos) * img.getWidth();
		yPos *= width; // yPos can now be used to shift the image down in the later equation without having to multiply by width every time
		for (int y = yIndex; y < yClip; y++, imgYBlock += img.getWidth()) {
			int yBlock = y * width; // the total amount of pixels making up the rows traversed so far
			int imgXBlock = xIndex - xPos;
			for (int x = xIndex; x < xClip; x++, imgXBlock++) {
				int index = x + yBlock; // the current location in pixels[]
				// calculate the new pixel value and put into pixels[]
				pixels[index] = blendRGB(imgPixels[imgXBlock + imgYBlock], pixels[index]);
			}
		}

		/*
		 * -----
		 * //old pixel render loop, not as efficient but included becuase it does the same thing (or at least it should!) and is more readable
		 * for (int y = yIndex; y < yClip; y++) {
		 * for (int x = xIndex; x < xClip; x++) {
		 * int index = xPos + x + yPos + y * getWidth(); // the current location in pixels[]
		 * // calculate the new pixel value and put into pixels[]
		 * pixels[index] = blendRGB(pixels[index], image.getPixels()[x + y * image.getWidth()]);
		 * }
		 * }
		 * /* -----
		 */
	}

	/**
	 * Blends two rgb colors (provided src has an alpha channel) using the src alpha value. Alpha values closer to 255 will make the returned color closer to src, while alpha values closer to 0 will
	 * make it closer to dest.
	 * 
	 * @param dest the rgb color to be blended (the "bottom" color)
	 * @param src the argb color to be blended (the "top" color)
	 * @return the two colors blended together
	 */
	public static int blendRGB(int src, int dest) {

		int a = src >> 24 & 0xFF; // alpha value of src

		// no sameness edge case because that will basically never happen
		if (a == 0xFF) { // src is 100% opaque
			return src & 0xFFFFFF; // return rgb-only value of src
		} else if (a == 0x00000000) { // src is transparent
			return dest;
		}

		float alpha = (float) (a / 255.0); // alpha value of src between 0 and 1, has to be a float or it will only be 0 or 1!
		// individual rgb sub-pixel values
		int r = dest >> 16 & 0xFF;
		int g = dest >> 8 & 0xFF;
		int b = dest & 0xFF;
		// (src >> hex & 0xFF) is the sub-pixel value of src.
		// return hex color made of the calculated RGB values: R << 16 OR G << 8 OR B
		return (int) (((src >> 16 & 0xFF) - r) * alpha + r) << 16 | (int) (((src >> 8 & 0xFF) - g) * alpha + g) << 8 | (int) (((src & 0xFF) - b) * alpha + b);
	}
}
