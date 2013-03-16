package hellomisterme.artillery_engine;

import hellomisterme.artillery_engine.game.Entity;
import hellomisterme.artillery_engine.graphics.Render;
import hellomisterme.util.Vector2;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

/**
 * DevInfo displays the current dev info when rendered.
 * 
 * @since 1-9-12
 * @author David Aaron Suddjian
 */
public class DevInfo {

	/**
	 * The on-screen coordinates at which the info will be displayed
	 */
	public int xLocation = 10, yLocation = 20;

	/**
	 * Total memory available
	 */
	public long maxMem = 0;

	/**
	 * Memory currently being used by the program
	 */
	public long usedMem = 0;

	/**
	 * Game status variable
	 */
	public int fps = 0, avg = 0, tps = 0, sec = 0;

	private static final int MB = 1048576; // bytes in a megabyte

	public static int fontSize = 22;
	public static Font font = new Font("Courier New", Font.BOLD, fontSize);

	/**
	 * Renders the dev info onto the Graphics object by first rendering a darker shadow and then the default text color on top of that.
	 */
	public void render(Render r) {
		Font savedFont = r.graphics.getFont();
		Color savedColor = r.graphics.getColor();
		r.graphics.setFont(font);
		r.graphics.setColor(new Color(0, 0, 0, 0xB0)); // render the darker shadow part
		render(r.graphics, 0, 1);
		r.graphics.setColor(new Color(0, 0xBB, 0)); // render the primary color
		render(r.graphics, 0, 0);
		r.graphics.setFont(savedFont);
		r.graphics.setColor(savedColor);
	}

	private void render(Graphics g, int xOff, int yOff) {
		World world = Game.getWorld();
		Entity player = world.getEntity("player");
		int currentLine = 0; // used to tell how far down each info line should render
		g.drawString("memory usage: " + usedMem / MB + " MB used of " + maxMem / MB + " total", xLocation + xOff, yLocation + yOff + currentLine); // render the memory info
		currentLine += fontSize * 2;
		g.drawString("FPS: " + fps, xLocation + xOff, yLocation + currentLine + yOff); // fps
		currentLine += fontSize * 2;
		if (player != null) {
			g.drawString("x: " + player.transform.position.x, xLocation + xOff, yLocation + currentLine + yOff); // player's x coordinate
			currentLine += fontSize;
			g.drawString("y: " + player.transform.position.y, xLocation + xOff, yLocation + currentLine + yOff); // player's x coordinate
			currentLine += fontSize;
			Vector2 velocity = player.getForces().velocity;
			g.drawString("v: " + velocity.mag(), xLocation + xOff, yLocation + currentLine + yOff); // player's velocity
			currentLine += fontSize;
			g.drawString("r: " + velocity.angle(), xLocation + xOff, yLocation + currentLine + yOff); // player's rotation
			currentLine += fontSize * 2;
		}
		g.drawString("e: " + world.entityCount(), xLocation + xOff, yLocation + currentLine + yOff); // how many entities in the world
	}

	/**
	 * Prepares Graphics object for use with this DevInfo. If this method is not called, there will be extra load time the first time render() is called. (unless something else has already used
	 * the same font this class is using)
	 * 
	 * @param g
	 */
	public static void setup(Graphics g) {
		g.setFont(font);
		g.drawString(" ", 0, 0);
	}
}
