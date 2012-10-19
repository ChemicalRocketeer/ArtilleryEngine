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
		yPos *= width; // yPos can now be used to shift the image down in the later equation
		for (int y = 0; y < img.getHeight(); y++) {
			for (int x = 0; x < img.getWidth(); x++) {
				if (xPos + x < 0) continue; // fix image wrapping on left side of screen
				if (xPos + x > width) break; // fix image wrapping on right side
				int index = xPos + x + yPos + y * width;
				if (index < 0) break; // fix negative out of bounds
				
				if (index < pixels.length) {
					pixels[index] = img.getPixels()[x + y * img.getWidth()]; // place the appropriate pixel from img into pixels
				} else {
					return; // fix positive out of bounds
				}
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