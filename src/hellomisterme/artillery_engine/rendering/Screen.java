package hellomisterme.artillery_engine.rendering;

import hellomisterme.util.Transform;
import hellomisterme.util.Vector2;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

/**
 * All the render methods in Screen render to the given coordinates on screen. To render to coordinates in the game world, use the methods in Render.
 * 
 * @author David Aaron Suddjian
 */

public class Screen {
	
	private int width, height;
	
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
	
	public Screen(int width, int height) {
		this.width = width;
		this.height = height;
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData(); // link the 'pixels' array to image's pixel data
	}
	
	public void render(BufferedImage img, Transform t) {
		Graphics2D graphics = image.createGraphics();
		// graphics.rotate(rotation, center.x, center.y);
		// graphics.scale(scale.x, scale.y);
		graphics.translate(t.position.x, t.position.y);
		graphics.drawImage(img, null, 0, 0);
		graphics.dispose();
	}
	
	public void drawArrow(Vector2 start, Vector2 end, Color c) {
		int endx = (int) end.x;
		int endy = (int) end.y;
		Graphics2D g = image.createGraphics();
		g.setColor(c);
		g.drawLine((int) start.x, (int) start.y, endx, endy);
		// to draw the arrow tip we can just rotate and scale this
		// It's reversed from the direction the arrow will actually point to make that easier
		Vector2 arrow = start.SUB(end);
		arrow.mul(0.2);
		arrow.rotate(0.5); // ~30 degrees
		g.drawLine(endx, endy, (int) (end.x + arrow.x), (int) (end.y + arrow.y));
		arrow.rotate(-1.0); // ~-60 degrees
		g.drawLine(endx, endy, (int) (end.x + arrow.x), (int) (end.y + arrow.y));
		g.dispose();
	}
	
	/**
	 * Draws image data at the indicated point on screen
	 * 
	 * @param img the ImageShell to be drawn
	 * @param xPos the x coordinate of the top-left corner of the PixelData
	 * @param yPos the y coordinate of the top-left corner of the PixelData
	 */
	public void render(PixelData img, int xPos, int yPos) {
		// check if img is completely off-screen
		if (xPos + img.getWidth() < 0 || yPos + img.getHeight() < 0 || xPos >= getWidth() || yPos >= getHeight()) {
			return;
		}
		
		// vars used to avoid printing unnecessary pixels or going out of bounds
		// all these vars are in screen coords, not image coords
		int xStartPoint; // xLocation value to start at when copying pixels from img
		int xEndPoint; // xLocation value to stop at when copying pixels from img
		int yStartPoint; // yLocation value to start at when copying pixels from img
		int yEndPoint; // yLocation value to stop at when copying pixels from img
		
		// OOB right
		if (xPos >= width - img.getWidth()) {
			xEndPoint = width; // end at our right border instead of the border of the image, which is too far right
		} else {
			xEndPoint = img.getWidth() + xPos; // end at the right border of the image
		}
		// OOB left
		if (xPos < 0) {
			xStartPoint = 0; // start at our left border because the image is too far left
		} else {
			xStartPoint = xPos; // start at the left border of the image
		}
		// OOB up
		if (yPos < 0) {
			yStartPoint = 0; // start at our top border because the image is too far up
		} else {
			yStartPoint = yPos; // start at the top border of the image
		}
		// OOB down
		if (yPos > height - img.getHeight()) {
			yEndPoint = height; // end at our bottom border because the image is too far down
		} else {
			yEndPoint = img.getHeight() + yPos; // end at the bottom border of the image
		}
		
		int[] imgPixels = img.getPixels();
		int imgY = (yStartPoint - yPos) * img.getWidth(); // the total number of pixels above the current line in image coords
		for (int screenY = yStartPoint; screenY < yEndPoint; screenY++, imgY += img.getWidth()) {
			int screenYBlock = screenY * width; // the total amount of pixels making up the rows traversed so far in screen coords
			int imgX = xStartPoint - xPos; // the total number of pixels to the left of the current one in image coords
			for (int screenX = xStartPoint; screenX < xEndPoint; screenX++, imgX++) {
				int index = screenX + screenYBlock; // the current location in pixels[] (screen coords)
				// calculate the new pixel value and put into pixels[]
				pixels[index] = blendRGB(imgPixels[imgX + imgY], pixels[index]);
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
	
	public int getHeight() {
		return height;
	}
	
	public int getWidth() {
		return width;
	}
	
	/**
	 * Clears pixels so they can be filled later
	 */
	public void clear() {
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = 0;
		}
	}
}
