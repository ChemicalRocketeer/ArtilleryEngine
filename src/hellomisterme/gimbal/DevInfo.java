package hellomisterme.gimbal;

import hellomisterme.gimbal.entities.mob.Player;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

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
	 * The font size that will be used when rendering
	 */
	public int fontSize = 18;

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
	
	/**
	 * The player that this DevInfo is watching
	 */
	public Player player;

	private static final int MB = 1048576; // bytes in a megabyte

	public DevInfo(Graphics g, Player p) {
		setup(g);
		player = p;
	}

	/**
	 * Renders the dev info onto the Graphics object by first rendering a light background text, then a darker shadow and then the default text color on top of that.
	 */
	public void render(Graphics g) {
		g.setFont(new Font("Courier New", Font.PLAIN, fontSize));
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setColor(new Color(0xFF, 0xFF, 0xFF, 0xAA)); // render the light part
		render(g, 1, 1);
		g.setColor(new Color(0, 0, 0, 0xB0)); // render the darker shadow part
		render(g, 0, 1);
		g.setColor(new Color(0, 0xBB, 0)); // render the primary color
		render(g, 0, 0);
	}

	private void render(Graphics g, int xOff, int yOff) {
		// turn on antialiasing
		int currentLine = 0; // used to tell how far down each info line should render
		g.drawString("memory usage: " + usedMem / MB + " MB used of " + maxMem / MB + " total", xLocation + xOff, yLocation + yOff + currentLine); // render the memory info
		currentLine += fontSize * 2;
		g.drawString("    FPS: " + fps, xLocation + xOff, yLocation + currentLine + yOff); // render fps
		currentLine += fontSize;
		g.drawString("average: " + avg, xLocation + xOff, yLocation + currentLine + yOff); // render avg
		currentLine += fontSize;
		g.drawString("    TPS: " + tps, xLocation + xOff, yLocation + currentLine + yOff); // render tps
		currentLine += fontSize;
		g.drawString("seconds: " + sec, xLocation + xOff, yLocation + currentLine + yOff); // render sec
		currentLine += fontSize * 2;
		g.drawString("x: " + player.getExactX(), xLocation + xOff, yLocation + currentLine + yOff); // render player's x coordinate
		currentLine += fontSize;
		g.drawString("y: " + player.getExactY(), xLocation + xOff, yLocation + currentLine + yOff); // render player's x coordinate
	}

	/**
	 * Prepares Graphics object for use with this DevInfo. If this method is not called, there will be extra load time the first time render() is called. (unless something else has already used
	 * the same font this class is using)
	 * 
	 * @param g
	 */
	public void setup(Graphics g) {
		g.setFont(new Font("Courier New", Font.PLAIN, fontSize));
		g.drawString(" ", xLocation, yLocation);
	}
}
