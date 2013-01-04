package hellomisterme.gimbal.graphics.text;

import hellomisterme.gimbal.Err;
import hellomisterme.gimbal.entities.Entity;
import hellomisterme.gimbal.graphics.Render;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

/**
 * Text is a simple graphical representation of text saved in an image file.
 * 
 * An object can render its own text simply by passing the Render, String, and coordinates to the render method instead of creating an instance.
 * 
 * @since 1/1/2013
 * @author David Aaron Suddjian
 */
public class Text extends Entity {

	private static String fontPath = "defaultFont.png";
	private static int charWidth = 24;
	private static Character[] characters = new Character[95];

	private int x;
	private int y;
	public String text;
	
	public Text() {
		setup();
	}

	public void setup() {
		try {
			BufferedImage src = ImageIO.read(new File(fontPath)); // read image file from disk
			for (int i = 0; i < characters.length; i++) {
				characters[i] = new Character(src, i * charWidth);
			}
		} catch (Exception e) {
			Err.error("Can't setup Text!");
			e.printStackTrace();
		}
	}

	/**
	 * Renders a provided string at a provided location
	 * 
	 * @param render
	 *            the Render object to use
	 * @param message
	 *            the string to render
	 */
	public static void render(Render render, String message, int x, int y) {

	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public void setPos(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public class Character {

		private int[] image;
		
		/**
		 * Reads the image associated with this character, starting at the given horizontal position 
		 * 
		 * @param src the source image
		 * @param position the horizontal pixel column to read first
		 */
		public Character(BufferedImage src, int position) {
			
		}
	}
}
