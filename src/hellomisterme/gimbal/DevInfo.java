package hellomisterme.gimbal;

import hellomisterme.gimbal.world.World;

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
	public int fontSize = 22;

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

	public DevInfo(Graphics g) {
		setup(g);
	}

	/**
	 * Renders the dev info onto the Graphics object by first rendering a darker shadow and then the default text color on top of that.
	 */
	public void render(Graphics2D g2) {
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON); // antialiasing
		g2.setFont(new Font("Courier New", Font.BOLD, fontSize));
		g2.setColor(new Color(0, 0, 0, 0xB0)); // render the darker shadow part
		render(g2, 0, 1);
		g2.setColor(new Color(0, 0xBB, 0)); // render the primary color
		render(g2, 0, 0);
	}

	private void render(Graphics g, int xOff, int yOff) {
		World world = Game.getWorld();
		int currentLine = 0; // used to tell how far down each info line should render
		g.drawString("memory usage: " + usedMem / MB + " MB used of " + maxMem / MB + " total", xLocation + xOff, yLocation + yOff + currentLine); // render the memory info
		currentLine += fontSize * 2;
		g.drawString("    FPS: " + fps, xLocation + xOff, yLocation + currentLine + yOff); // fps
		currentLine += fontSize;
		g.drawString("average: " + avg, xLocation + xOff, yLocation + currentLine + yOff); // average fps
		currentLine += fontSize;
		g.drawString("    TPS: " + tps, xLocation + xOff, yLocation + currentLine + yOff); // ticks per second
		currentLine += fontSize;
		g.drawString("   time: " + sec + " seconds", xLocation + xOff, yLocation + currentLine + yOff); // seconds
		currentLine += fontSize * 2;
		g.drawString("x: " + world.player.getExactX(), xLocation + xOff, yLocation + currentLine + yOff); // player's x coordinate
		currentLine += fontSize;
		g.drawString("y: " + world.player.getExactY(), xLocation + xOff, yLocation + currentLine + yOff); // player's x coordinate
		currentLine += fontSize * 2;
		g.drawString("e: " + world.getEntities().size(), xLocation + xOff, yLocation + currentLine + yOff); // how many entities in the world
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
