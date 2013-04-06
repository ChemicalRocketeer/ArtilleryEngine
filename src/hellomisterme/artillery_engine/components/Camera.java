package hellomisterme.artillery_engine.components;

import hellomisterme.artillery_engine.Game;
import hellomisterme.artillery_engine.Tick;
import hellomisterme.util.Vector2;

import java.awt.geom.AffineTransform;

/**
 * @since 2013-3-3
 * @author David Aaron Suddjian
 */
public class Camera extends IngameComponent implements Tick {

	public boolean followWithPosition = true;
	public boolean followWithRotation = false;
	public boolean followWithScale = true;

	public AffineTransform view = new AffineTransform();

	public Camera() {

	}

	public Camera(boolean pos, boolean rotation, boolean scale) {
		followWithPosition = pos;
		followWithRotation = rotation;
		followWithScale = scale;
	}

	@Override
	public void tick() {
		AffineTransform newView = new AffineTransform();
		Vector2 pos = globalPosition();
		Vector2 scale = globalScale();
		if (followWithScale) {
			newView.scale(scale.x, scale.y);
		}
		if (followWithPosition) {
			newView.translate(Game.getDisplayWidth() / scale.x * 0.5 - pos.x, Game.getDisplayHeight() / scale.y * 0.5 - pos.y);
		}
		if (followWithRotation) {
			newView.rotate(-globalRotation(), pos.x, pos.y);
		}
		view = newView;
	}
}
