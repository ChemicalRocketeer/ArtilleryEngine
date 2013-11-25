package hellomisterme.artillery_engine.components;

import hellomisterme.artillery_engine.Tick;
import hellomisterme.artillery_engine.io.Keyboard;
import hellomisterme.artillery_engine.io.Keyboard.Control;
import hellomisterme.artillery_engine.util.Transform;
import hellomisterme.artillery_engine.util.Vector;

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
	public Transform globalTransform() {
		Vector pos;
		if (followWithPosition)
			pos = super.globalPosition();
		else
			pos = transform.position;
		double rot;
		if (followWithRotation)
			rot = super.globalRotation();
		else
			rot = transform.rotation;
		Vector scale;
		if (followWithScale)
			scale = super.globalScale();
		else
			scale = transform.scale;
		return new Transform(pos, rot, scale);
	}
	
	@Override
	public void tick() {
		if (Keyboard.pressed(Control.LEFT))
			transform.position.x -= 5;
		if (Keyboard.pressed(Control.RIGHT))
			transform.position.x += 5;
		if (Keyboard.pressed(Control.UP))
			transform.position.y -= 5;
		if (Keyboard.pressed(Control.DOWN))
			transform.position.y += 5;
		if (Keyboard.pressed(Control.CAMROTLEFT))
			transform.rotation -= 0.005;
		if (Keyboard.pressed(Control.CAMROTRIGHT))
			transform.rotation += 0.005;
		// transform.rotation += 0.05;
	}
}
