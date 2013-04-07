package hellomisterme.artillery_engine.rendering;

import hellomisterme.artillery_engine.Game;
import hellomisterme.artillery_engine.components.Camera;
import hellomisterme.artillery_engine.components.imagery.ImageShell;
import hellomisterme.util.Vector2;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

/**
 * Render handles computation and combination of the pixels that go onto the screen.
 * Also handles methods to do with the visual contents of the entire screen, like screenshots.
 * 
 * @since 10-14-12
 * @author David Aaron Suddjian
 */
public class Render {

	private int width;
	private int height;

	/** Flag to tell objects how they should render themselves */
	public boolean simpleRendering = false;
	public RenderingHints renderHints;
	private RenderingHints speedHints;
	private RenderingHints qualityHints;
	/** Render mode setting to change image quality/speed tradeoffs */
	public static final int RENDER_FOR_SPEED = 0, RENDER_FOR_QUALITY = 1;

	public int[] pixels;
	public BufferedImage image;
	public Graphics2D graphics;

	/** Camera mode setting to change where images are rendered */
	public static final int GUI_OVERLAY_CAMERA = 0, INGAME_CAMERA = 1;

	public Render(int width, int height) {
		speedHints = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
		speedHints.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
		speedHints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		speedHints.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		speedHints.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
		speedHints.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		speedHints.put(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

		qualityHints = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		qualityHints.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		qualityHints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		qualityHints.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		qualityHints.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		qualityHints.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		qualityHints.put(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);

		renderHints = qualityHints;

		setSize(width, height);
	}

	/**
	 * Renders the given BufferedImage.
	 * 
	 * This method does not change the current AffineTransform before drawing the image, but it sets it to the defaultTransform after it is done.
	 * 
	 * @param img
	 * @param center
	 * @param offset
	 * @param rotation
	 * @param scale
	 */
	public void render(BufferedImage img, Vector2 center, Vector2 offset, double rotation, Vector2 scale, int cameraMode) {
		setCameraMode(cameraMode);
		graphics.rotate(rotation, center.x, center.y);
		graphics.scale(scale.x, scale.y);
		graphics.translate(center.x / scale.x + offset.x, center.y / scale.y + offset.y);
		graphics.drawImage(img, null, 0, 0);
	}

	public void render(ImageShell img, Vector2 pos, int cameraMode) {
		render(img, (int) pos.x, (int) pos.y, cameraMode);
	}

	/**
	 * Draws image data at the indicated point on screen
	 * 
	 * @param img the ImageShell to be drawn
	 * @param xPos the xLocation coordinate of the top-left corner of the Sprite
	 * @param yPos the yLocation coordinate of the top-left corner of the Sprite
	 */
	public void render(ImageShell img, int xPos, int yPos, int cameraMode) {
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

	public void setCameraMode(int cameraMode) {
		if (cameraMode == INGAME_CAMERA) {
			graphics.setTransform(new AffineTransform(getActiveCamera().view));
		} else if (cameraMode == GUI_OVERLAY_CAMERA) {
			graphics.setTransform(new AffineTransform());
		}
	}

	public void setRenderMode(int renderMode) {
		if (renderMode == RENDER_FOR_QUALITY) {
			renderHints = qualityHints;
		} else if (renderMode == RENDER_FOR_SPEED) {
			renderHints = speedHints;
		}
	}

	public Camera getActiveCamera() {
		return Game.getWorld().activeCamera;
	}

	/**
	 * Clears pixels so they can be filled later
	 */
	public void clear() {
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = 0;
		}
	}

	public void dispose() {
		graphics.dispose();
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public void setSize(int w, int h) {
		width = w;
		height = h;
		image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		graphics = image.createGraphics();
		graphics.addRenderingHints(renderHints);
		graphics.setColor(Color.BLACK);
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData(); // link the image's pixel data to the pixels array
	}
}