package hellomisterme.gimbal.graphics;

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
public class DevInfo implements Renderable {

	public int x = 10, y = 20;
	public int fontSize = 18;
	public Font font;

	public long totalMem, usedMem;
	public int fps = 0, avg = 0, tps = 0, sec = 0;

	private static final int MB = 1048576; // bytes in a megabyte

	public DevInfo() {
		font = new Font("Courier New", Font.PLAIN, fontSize);
	}

	public DevInfo(Graphics g) {
		this();
		setup(g);
	}

	@Override
	public void render(Graphics g, Render r) {
		g.setFont(font);
		g.setColor(new Color(0, 0, 0, 0xB0));
		render(g, 0, 1);
		g.setColor(new Color(0, 0xBB, 0));
		render(g, 0, 0);
	}

	private void render(Graphics g, int xOff, int yOff) {
		// turn on antialiasing
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		int currentLine = 0; // used to tell how far down each info line should render
		g.drawString("memory usage: " + usedMem / MB + " MB used of " + totalMem / MB + " total", x + xOff, y + yOff + currentLine); // render the memory info
		currentLine += fontSize * 2;
		g.drawString("FPS:     " + fps, x + xOff, y + currentLine + yOff); // render fps
		currentLine += fontSize;
		g.drawString("average: " + avg, x + xOff, y + currentLine + yOff); // render avg
		currentLine += fontSize;
		g.drawString("TPS:     " + tps, x + xOff, y + currentLine + yOff); // render tps
		currentLine += fontSize;
		g.drawString("seconds: " + sec, x + xOff, y + currentLine + yOff); // render sec
	}

	/**
	 * Gets the Graphics object ready for use with this DevInfo. If this method is not called, there will be extra load time the first time render() is called. (unless something else has already used
	 * the same font this class is using)
	 * 
	 * @param g
	 */
	public void setup(Graphics g) {
		g.setFont(font);
		g.drawString(" ", x, y);
	}
}
