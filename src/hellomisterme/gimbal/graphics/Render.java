package hellomisterme.gimbal.graphics;

import hellomisterme.gimbal.Err;
import hellomisterme.gimbal.Game;
import hellomisterme.gimbal.entities.Entity;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.List;

import javax.imageio.ImageIO;

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
	public int[] pixels = new int[width * height];

	private int screenshotNumber = 0;
	private String screenshotPrefix = "screen ";

	public Render(int width, int height) {
		this.width = width;
		this.height = height;
		pixels = new int[width * height];
		readScreenshotNumber();
	}

	/**
	 * Saves a picture of whatever's currently on the screen to screenshots/default-generated-filename in PNG format.
	 */
	public void screenshot() {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		int[] shot = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		for (int i = 0; i < pixels.length; i++) {
			shot[i] = pixels[i];
		}
		Calendar c = Calendar.getInstance();
		screenshotNumber++;
		String fileName = c.get(Calendar.MONTH) + 1 + "-" + c.get(Calendar.DAY_OF_MONTH) + "-" + c.get(Calendar.YEAR) + ""; // date
		fileName += " " + c.get(Calendar.HOUR_OF_DAY) + "_" + c.get(Calendar.MINUTE) + "_" + c.get(Calendar.SECOND) + ""; // time
		fileName = "screenshots/" + screenshotPrefix + screenshotNumber + "  " + fileName + ".png"; // other file name stuff
		try {
			File outFile = new File(fileName);
			outFile.mkdirs();
			ImageIO.write(image, "png", outFile);
		} catch (Exception e) {
			Err.error("Can't write screenshot \"" + fileName + "\" to file!");
			e.printStackTrace();
		}
		System.out.println("Screenshot saved as " + fileName);
	}

	/**
	 * Reads the number of the latest screenshot from the screenshots folder, and sets it as the number to start at when writing future screenshots.
	 */
	public void readScreenshotNumber() {
		File[] shots = new File("screenshots").listFiles();
		if (shots == null) {
			screenshotNumber = 0;
			return;
		}
		for (int i = 0; i < shots.length; i++) {
			String name = shots[i].getName();
			int index = name.indexOf(screenshotPrefix); // find the screenshotPrefix in file
			index += screenshotPrefix.length();
			if (index != -1) {
				name = name.substring(index); // name now starts with its screenshot number
				index = name.indexOf(' '); // now use index to find the end of the number
				if (index != -1) {
					name = name.substring(0, index);
					BigInteger num = new BigInteger(name);
					if (num.intValue() > screenshotNumber) {
						screenshotNumber = num.intValue();
					}
				} else {
					System.out.print("Render.readScreenshotNumber: " + name + " is an illegal filename!"); // TODO remove/modify
				}
			} else {
				System.out.print("Render.readScreenshotNumber: " + name + " is an illegal filename!"); // TODO remove/modify
			}

		}
	}

	public void render(List<Entity> entities) {
		clear();
		for (int i = 0; i < entities.size(); i++) {
			if (entities.get(i) != null) {
				render(entities.get(i).getImage(), entities.get(i).getX(), entities.get(i).getY());
			} else {
				System.out.println("At entities[" + i + "]:");
				Err.error("Cannot render null Entity at Render.render.entities[" + i + "]!");
			}
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
	 * TODO: fill with black for final game instead of pink for testing
	 */
	public void clear() {
		// fill all pixels with black
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = 0;
		}
	}
}