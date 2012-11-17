package hellomisterme.gimbal.io;

import hellomisterme.gimbal.Err;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.math.BigInteger;
import java.util.Calendar;

import javax.imageio.ImageIO;

/**
 * ScreenshotManager keeps track of screenshot data and writes screenshots to file
 * 
 * @author David Aaron Suddjian
 */
public class ScreenshotManager {

	private int number = 0;
	private String prefix = "screen ";

	public ScreenshotManager() {
		readScreenshotNumber();
	}

	/**
	 * Saves a picture of whatever's currently on the screen to screenshots/default-generated-filename in PNG format.
	 */
	public void screenshot(int[] pixels, int width) {
		BufferedImage image = new BufferedImage(width, pixels.length / width, BufferedImage.TYPE_INT_RGB);
		int[] shot = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		for (int i = 0; i < pixels.length; i++) {
			shot[i] = pixels[i];
		}
		number++;
		Calendar c = Calendar.getInstance();
		String fileName = c.get(Calendar.MONTH) + 1 + "-" + c.get(Calendar.DAY_OF_MONTH) + "-" + c.get(Calendar.YEAR) + ""; // date
		fileName += " " + c.get(Calendar.HOUR_OF_DAY) + "_" + c.get(Calendar.MINUTE) + "_" + c.get(Calendar.SECOND) + ""; // time
		fileName = "screenshots/" + prefix + number + "  " + fileName + ".png"; // other file name stuff
		try {
			File outFile = new File(fileName);
			outFile.mkdirs(); // make any necessary directories
			ImageIO.write(image, "png", outFile); // write the pixel data
		} catch (Exception e) {
			Err.error("Can't write screenshot \"" + fileName + "\" to file!");
			e.printStackTrace();
		}
		System.out.println("Screenshot saved as " + fileName); // TODO put text on screen instead
	}

	/**
	 * Reads the number of the latest screenshot from the screenshots folder, and sets it as the number to start at when writing future screenshots.
	 */
	public void readScreenshotNumber() {
		File[] shots = new File("screenshots").listFiles();
		if (shots == null) {
			number = 0;
			return;
		}
		for (int i = 0; i < shots.length; i++) {
			String name = shots[i].getName();
			int index = name.indexOf(prefix); // find the prefix in file
			index += prefix.length();
			if (index != -1) {
				name = name.substring(index); // name now starts with its screenshot number
				index = name.indexOf(' '); // now use frame to find the end of the number
				if (index != -1) {
					name = name.substring(0, index);
					BigInteger num = new BigInteger(name);
					if (num.intValue() > number) {
						number = num.intValue();
					}
				} else {
					System.out.println("Render.readScreenshotNumber: " + name + " ends illegally!"); // TODO remove/modify
				}
			} else {
				System.out.println("Render.readScreenshotNumber: " + name + " begins illegally!"); // TODO remove/modify
			}

		}
	}
}
