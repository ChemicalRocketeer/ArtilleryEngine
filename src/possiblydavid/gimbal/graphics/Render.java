package possiblydavid.gimbal.graphics;

import java.util.List;

import possiblydavid.gimbal.Entity;

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

	public void render() {

	}

	public void render(List<Entity> entities) {
		for (int i = 0; i < entities.size(); i++) {
			render(entities.get(i).getImage(), entities.get(i).getX(), entities.get(i).getY());
		}
	}

	/**
	 * Draws LightImg data at the indicated point on screen
	 * 
	 * TODO: add rotation support
	 * 
	 * @param img
	 *            the LightImg to be drawn
	 * @param xPos
	 *            the x coordinate of the top-left corner of the LightImg
	 * @param yPos
	 *            the y coordinate of the top-left corner of the LightImg
	 */
	public void render(LightImg img, int xPos, int yPos) {
		if (xPos + img.getWidth() < 0 || yPos + img.getHeight() < 0 || xPos >= width || yPos >= height) {
			System.out.println("LightImg OOB, did not render"); // TODO: remove this test line
			return;
		}

		// vars used to avoid printing unnecessary pixels or going out of bounds
		int xIndex = 0; // x value to start at when copying pixels from img
		int xClip = img.getWidth(); // x value to stop at when copying pixels from img
		int yIndex = 0; // y value to start at when copying pixels from img
		int yClip = img.getHeight(); // y value to stop at when copying pixels from img
		// check if part of img goes out of bounds (OOB)
		if (xPos >= width - xClip) { // OOB right
			xClip = width - xPos;
		}
		if (xPos < 0) { // OOB left
			xIndex = -xPos;
		}
		if (yPos < 0) { // OOB top
			yIndex = -yPos;
		}
		if (yPos > height - yClip) { // OOB bottom
			yClip = height - yPos;
		}

		yPos *= width; // yPos can now be used to shift the image down in the later equation

		// actual img copy loop
		for (int y = yIndex; y < yClip; y++) {
			for (int x = xIndex; x < xClip; x++) {
				pixels[xPos + x + yPos + y * width] = img.getPixels()[x + y * img.getWidth()]; // place the appropriate pixel from img into pixels
			}
		}
	}

	/**
	 * Clears pixels so they can be filled later
	 * 
	 * TODO: fill with black for final game instead of pink for testing
	 */
	public void clear() {
		// fill all pixels with black
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = 0xFF00FF;
		}
	}
}