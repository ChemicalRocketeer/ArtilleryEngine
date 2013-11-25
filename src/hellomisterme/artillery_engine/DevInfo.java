package hellomisterme.artillery_engine;

import hellomisterme.artillery_engine.components.physics.FreeBody;
import hellomisterme.artillery_engine.rendering.Render;
import hellomisterme.artillery_engine.rendering.Renderable;
import hellomisterme.artillery_engine.util.Vector;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

/**
 * DevInfo displays the current dev info when rendered.
 * 
 * @since 1-9-12
 * @author David Aaron Suddjian
 */
public class DevInfo implements Renderable {
	
	/** The on-screen coordinates at which the info will be displayed */
	public int xLocation = 10, yLocation = 20;
	
	/** Game status variable */
	public int fps = 0, avg = 0, tps = 0, sec = 0;
	public long usedMemory, totalMemory;
	
	public static int fontSize = 22;
	public static Font font = new Font("Courier New", Font.BOLD, fontSize);
	
	@Override
	public void render(Render render) {
	}
	
	@Override
	public void devmodeRender(Render render) {
		Graphics2D g = render.screen.getGraphics();
		g.setFont(font);
		g.setColor(new Color(0, 0, 0, 0xB0)); // render the darker shadow part
		render(g, 0, 1);
		g.setColor(new Color(0, 0xBB, 0)); // render the primary color
		render(g, 0, 0);
		g.dispose();
		Vector camPos = render.getCameraView().position;
		render.drawArrow(camPos, new Vector(camPos).add(new Vector(0, 50)), Color.RED);
		render.drawArrow(camPos, new Vector(camPos).add(new Vector(50, 0)), Color.BLUE);
	}
	
	private void render(Graphics2D g, int xOff, int yOff) {
		int x = xLocation + xOff;
		int y = yLocation + yOff;
		
		int lineLocation = 0; // used to tell how far down each info line should render
		g.drawString("memory usage: " + usedMemory + " MB used of " + totalMemory + " total", x, y + lineLocation); // render the memory info
		lineLocation += fontSize * 2;
		g.drawString("FPS: " + fps, x, y + lineLocation); // fps
		lineLocation += fontSize * 2;
		
		World world = Game.currentInstance().getWorld();
		Entity player = world.getPlayer();
		if (player != null) {
			g.drawString("x: " + player.transform.position.x, x, y + lineLocation); // player's x coordinate
			lineLocation += fontSize;
			g.drawString("y: " + player.transform.position.y, x, y + lineLocation); // player's x coordinate
			lineLocation += fontSize * 2;
			FreeBody freebody = player.getFreeBody();
			g.drawString("v: " + freebody.velocity.magnitude(), x, y + lineLocation); // player's velocity
			lineLocation += fontSize;
			g.drawString("r: " + player.transform.rotation, x, y + lineLocation); // player's rotation
			lineLocation += fontSize;
			g.drawString("s: " + freebody.spin, x, y + lineLocation); // player's spin
			lineLocation += fontSize * 2;
		}
		g.drawString("e: " + world.entityCount(), xLocation + xOff, yLocation + lineLocation + yOff); // how many entities in the world
	}
}
