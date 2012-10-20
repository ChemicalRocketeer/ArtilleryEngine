package possiblydavid.gimbal.graphics;

import java.util.List;

import possiblydavid.gimbal.Entity;

/**
 * Render handles computation of the pixels that go onto the screen.
 * 
 * TODO add transparency support
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
				int color = img.getPixels()[x + y * img.getWidth()]; // the color of the current pixel in img
				int a = (color >> 24) & 0xFF; // alpha value of color

				if (a == 0) { // if color is transparent, no action necessary
					break;
				}

				int index = xPos + x + yPos + y * width; // the current location in pixels[]
				if (a != 0xFF) { // if not 100% opaque
					System.out.print("part-opaque: " + a + "  ");
					int locColor = pixels[index]; // the current color stored in the local pixels array
					int newColor = 0; // the color to be stored in pixels[]
					// sub-pixel values of pixels[index]
					int r = (locColor >> 16) & 0xFF;
					int g = (locColor >> 8) & 0xFF;
					int b = locColor & 0xFF;

					// (color >> number & 0xFF) is the sub-pixel value of color.
					r = (((color >> 16 & 0xFF) - r) / 0xFF) * a + r; // calculate the new r sub-pixel value
					System.out.print(r + "  ");
					g = (((color >> 8 & 0xFF) - g) / 0xFF) * a + g;
					System.out.print(g + "  ");
					b = (((color & 0xFF) - b) / 0xFF) * a + b;
					System.out.print(b + "  ");
					
					newColor =  (r << 16) | (g << 8) | b;
					pixels[index] = (r << 16) | (g << 8) | b; // place the calculated newColor into pixels
					System.out.println(Integer.toHexString(newColor));
					
					/*
					for (int i = 0; i < 3; i++) { // cycle through sub-pixels
						int subPix = locColor >> i * 8 & 0xFF; // the current local sub-pixel value
						System.out.print(subPix + "." + Integer.toHexString(color >> i * 8 & 0xFF) + ". ");
						// color >> i * 8 & 0xFF is the current sub-pixel value of color. I didn't give it a variable since it's only used once per loop cycle.
						subPix = (((color >> i * 8 & 0xFF) - subPix) / 0xFF) * a + subPix; // calculate the new sub-pixel value
						newColor = (subPix << i * 8) | newColor; // "or" the new sub-pixel value to newColor
					}
					System.out.println(Integer.toHexString(newColor));
					pixels[index] = newColor; // place the calculated newColor into pixels
					*/
				} else { // color must be 100% opaque
					System.out.println("opaque");
					pixels[index] = color; // just set pixels[index] to color
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
		// fill all pixels with pink
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = 0xFF00FF;
		}
	}
}