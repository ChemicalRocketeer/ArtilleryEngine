package hellomisterme.artillery_engine.components;

import hellomisterme.artillery_engine.Tick;
import hellomisterme.artillery_engine.io.Keyboard;
import hellomisterme.artillery_engine.io.Keyboard.Controls;
import hellomisterme.artillery_engine.rendering.Render;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * @since 2013-3-3
 * @author David Aaron Suddjian
 */
public class Camera extends IngameComponent implements Tick {
	
	public boolean followWithPosition = true;
	public boolean followWithRotation = false;
	public boolean followWithScale = false;
	
	public Camera() {
		
	}
	
	public Camera(boolean pos, boolean rotation, boolean scale) {
		followWithPosition = pos;
		followWithRotation = rotation;
		followWithScale = scale;
	}
	
	@Override
	public void tick() {
		if (Keyboard.pressed(Controls.LEFT))
			transform.position.x -= 1.5;
		if (Keyboard.pressed(Controls.RIGHT))
			transform.position.x += 1.5;
		if (Keyboard.pressed(Controls.UP))
			transform.position.y -= 1.5;
		if (Keyboard.pressed(Controls.DOWN))
			transform.position.y += 1.5;
	}
	
	public void render(Render render) {
		Graphics2D g = render.screen.image.createGraphics();
		g.setColor(Color.cyan);
		g.fillOval(50, 50, 5, 5);
	}
}
