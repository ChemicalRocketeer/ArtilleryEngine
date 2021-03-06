package hellomisterme.artillery_engine.io;

import hellomisterme.artillery_engine.Err;

import java.awt.image.BufferedImage;
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
	
	private static int number = readScreenshotNumber();
	public static final String PREFIX = "screen ";
	private final BufferedImage image;
	
	/**
	 * Creates a new Screenshot using the provided BufferedImage
	 * 
	 * @param img the BufferedImage to use
	 */
	public Screenshot(BufferedImage img) {
		this.image = img;
	}
	
	public void go() {
		Thread shot = new Thread(this);
		shot.start();
		try {
			shot.join();
		} catch (InterruptedException e) {
			Err.error("Can't take screenshot " + number + "!", e);
		}
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
			fileName = "screenshots/" + PREFIX + number + "  " + c.get(Calendar.MONTH) + 1 + "-" + c.get(Calendar.DAY_OF_MONTH) + "-" + c.get(Calendar.YEAR) + " " + c.get(Calendar.HOUR_OF_DAY) + "." + c.get(Calendar.MINUTE) + "." + c.get(Calendar.SECOND) + ".png";
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
	public static void resetScreenshotNumber() {
		number = readScreenshotNumber();
	}
	
	private static int readScreenshotNumber() {
		File[] shots = new File("screenshots").listFiles();
		if (shots == null) {
			return 0;
		}
		int n = 0;
		for (int i = 0; i < shots.length; i++) {
			String name = shots[i].getName();
			int index = name.indexOf(PREFIX); // find the PREFIX in file
			index += PREFIX.length();
			if (index != -1) {
				name = name.substring(index); // name now starts with its screenshot number
				index = name.indexOf(' '); // find the end of the number
				if (index != -1) {
					name = name.substring(0, index);
					BigInteger num = new BigInteger(name);
					if (num.intValue() > number) {
						n = num.intValue();
					}
				}
			}
		}
		return n;
	}
	
	@Override
	public void run() {
		screenshot(image);
	}
}
