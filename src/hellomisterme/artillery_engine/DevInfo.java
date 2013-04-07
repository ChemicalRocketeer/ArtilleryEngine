package hellomisterme.artillery_engine;

import hellomisterme.artillery_engine.components.physics.FreeBody;
import hellomisterme.artillery_engine.rendering.Render;
import hellomisterme.artillery_engine.rendering.Renderable;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

/**
 * DevInfo displays the current dev info when rendered.
 * 
 * @since 1-9-12
 * @author David Aaron Suddjian
 */
public class DevInfo implements Renderable {

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
	@Override
	public void render(Render render) {
		render.setCameraMode(Render.GUI_OVERLAY_MODE);
		Font savedFont = render.graphics.getFont();
		Color savedColor = render.graphics.getColor();
		render.graphics.setFont(font);
		render.graphics.setColor(new Color(0, 0, 0, 0xB0)); // render the darker shadow part
		render(render.graphics, 0, 1);
		render.graphics.setColor(new Color(0, 0xBB, 0)); // render the primary color
		render(render.graphics, 0, 0);
		render.graphics.setFont(savedFont);
		render.graphics.setColor(savedColor);
	}

	private void render(Graphics g, int xOff, int yOff) {
		int x = xLocation + xOff;
		int y = yLocation + yOff;
		World world = Game.getWorld();
		Entity player = world.getEntity("player");
		int lineLocation = 0; // used to tell how far down each info line should render
		g.drawString("memory usage: " + usedMem / MB + " MB used of " + maxMem / MB + " total", x, y + lineLocation); // render the memory info
		lineLocation += fontSize * 2;
		g.drawString("FPS: " + fps, x, y + lineLocation); // fps
		lineLocation += fontSize * 2;
		if (player != null) {
			g.drawString("x: " + player.transform.position.x, x, y + lineLocation); // player's x coordinate
			lineLocation += fontSize;
			g.drawString("y: " + player.transform.position.y, x, y + lineLocation); // player's x coordinate
			lineLocation += fontSize * 2;
			FreeBody freebody = player.getFreeBody();
			g.drawString("v: " + freebody.velocity.mag(), x, y + lineLocation); // player's velocity
			lineLocation += fontSize;
			g.drawString("r: " + player.transform.rotation, x, y + lineLocation); // player's rotation
			lineLocation += fontSize;
			g.drawString("s: " + freebody.spin, x, y + lineLocation); // player's angular momentum
			lineLocation += fontSize * 2;
		}
		g.drawString("e: " + world.entityCount(), xLocation + xOff, yLocation + lineLocation + yOff); // how many entities in the world
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
