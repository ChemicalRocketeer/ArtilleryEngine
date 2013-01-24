package hellomisterme.artillery_engine.io;

import hellomisterme.artillery_engine.Err;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.math.BigInteger;
import java.util.Calendar;

import javax.imageio.ImageIO;

/**
 * Screenshot keeps track of screenshot data and writes screenshots to file
 * 
 * @author David Aaron Suddjian
 */
public class Screenshot implements Runnable {

	private static int number = 0;
	public static final String prefix = "screen ";
	private BufferedImage image;

	/**
	 * Creates a new Screenshot using the provided BufferedImage
	 * 
	 * @param img the Image to use
	 */
	public Screenshot(BufferedImage img) {
		ColorModel cm = img.getColorModel();
		image = new BufferedImage(cm, img.copyData(null), cm.isAlphaPremultiplied(), null);
		Thread shot = new Thread(this);
		shot.start();
	}

	/**
	 * Saves a picture of whatever's currently on the screen to screenshots/default-generated-filename in PNG format.
	 */
	private static void screenshot(BufferedImage image) {
		number++;
		String fileName = "";

		// put date and time in the filename
		{
			Calendar c = Calendar.getInstance();
			fileName = "screenshots/" + prefix + number + "  " + c.get(Calendar.MONTH) + 1 + "-" + c.get(Calendar.DAY_OF_MONTH) + "-" + c.get(Calendar.YEAR) + " " + c.get(Calendar.HOUR_OF_DAY) + "'" + c.get(Calendar.MINUTE) + "'" + c.get(Calendar.SECOND) + ".png";
		}
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
	public static void readScreenshotNumber() {
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
				index = name.indexOf(' '); // find the end of the number
				if (index != -1) {
					name = name.substring(0, index);
					BigInteger num = new BigInteger(name);
					if (num.intValue() > number) {
						number = num.intValue();
					}
				}
			}
		}
	}

	@Override
	public void run() {
		screenshot(image);
	}
}
