package hellomisterme.gimbal.entities.mob;

import hellomisterme.gimbal.entities.Mover;
import hellomisterme.gimbal.graphics.LightweightAnimation;

import java.awt.Rectangle;

/**
 * Describes a basic Mob, with an animation and a hitbox.
 * 
 * @since 11-22-12
 * @author David Aaron Suddjian
 */
public abstract class Mob extends Mover implements Physical {

	public static final int DEFAULT_ANIM_SPEED = 5;
	protected int animationSpeed = DEFAULT_ANIM_SPEED; // how many ticks between animation frames
	protected int animationTimer = 0; // how many ticks since the last frame update

	/** This Player's hitbox. The x and y variables are relative to the Player's position. */
	public Rectangle hitbox;

	/**
	 * If it's time to change frames, do it. Will not work if the image is not an animation.
	 */
	public void animate() {
		if (animationTimer % animationSpeed == 0) {
			((LightweightAnimation) image).next();
		}
		animationTimer++;
	}

	/**
	 * If this Mob is outside the world bounds, sets the x and y positions to be exactly at the edge of the world bounds
	 */
	public void correctOOB() {
		if (x >= bounds.getWidth() - image.getWidth()) { // right
			setPos(bounds.getWidth() - image.getWidth() - 1, y);
		} else if (x < 0) { // left
			setPos(0, y);
		}
		if (y >= bounds.getHeight() - image.getHeight()) { // bottom
			setPos(x, bounds.getHeight() - image.getHeight() - 1);
		} else if (y < 0) { // top
			setPos(x, 0);
		}
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle((int) x + hitbox.x, (int) y + hitbox.y, hitbox.width, hitbox.height);
	}
}
