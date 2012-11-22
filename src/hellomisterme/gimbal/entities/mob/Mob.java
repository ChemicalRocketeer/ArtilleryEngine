package hellomisterme.gimbal.entities.mob;

import java.awt.Rectangle;

import hellomisterme.gimbal.entities.Mover;
import hellomisterme.gimbal.graphics.LightweightAnimation;

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
	 * If the 
	 */
	public void animate() {
		if (animationTimer == animationSpeed) {
			((LightweightAnimation) image).next();
			animationTimer = 0;
		} else {
			animationTimer++;
		}
	}

	/**
	 * If this Mob is outside the bucket bounds, sets the x and y positions to be exactly at the edge of the bucket bounds
	 */
	public void correctOOB() {
		if (x >= bucket.getWidth() - image.getWidth()) { // right
			setPos(bucket.getWidth() - image.getWidth() - 1, y);
		} else if (x < 0) { // left
			setPos(0, y);
		}
		if (y >= bucket.getHeight() - image.getHeight()) { // bottom
			setPos(x, bucket.getHeight() - image.getHeight() - 1);
		} else if (y < 0) { // top
			setPos(x, 0);
		}
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle((int) x + hitbox.x, (int) y + hitbox.y, hitbox.width, hitbox.height);
	}
}
