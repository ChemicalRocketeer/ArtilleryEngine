package hellomisterme.gimbal.graphics;

import hellomisterme.gimbal.io.Savable;

import java.awt.image.BufferedImage;

/**
 * A basic class to describe an image.
 * 
 * @since 11-12-12
 * @author David Aaron Suddjian
 */
public abstract class GimbalImage implements Savable {
	
	public abstract int[] getPixels();
	public abstract void setImage(String path);
	public abstract int getWidth();
	protected abstract void setWidth(int w);
	public abstract int getHeight();
	
	public static int[] extractPixels(BufferedImage src) {
		int[] pixels = new int[src.getWidth() * src.getHeight()]; // set pixels length
		// copy colors from src to pixels
		for (int y = 0; y < src.getHeight(); y++) {
			for (int x = 0; x < src.getWidth(); x++) {
				pixels[x + y * src.getWidth()] = src.getRGB(x, y);
			}
		}
		return pixels;
	}
}
