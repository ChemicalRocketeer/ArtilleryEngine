package hellomisterme.artillery_engine.entities.mob;

import hellomisterme.artillery_engine.Err;
import hellomisterme.artillery_engine.Tick;
import hellomisterme.artillery_engine.entities.Mass;
import hellomisterme.artillery_engine.entities.Mover;
import hellomisterme.artillery_engine.graphics.AnimatedSprite;

import java.awt.Rectangle;
import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * Describes a basic Mob, with an animation and a hitbox.
 * 
 * @since 11-22-12
 * @author David Aaron Suddjian
 */
public abstract class Mob extends Mover implements Physical, Mass, Tick {
	
	protected AnimatedSprite animation;
	public static final int DEFAULT_ANIM_SPEED = 5;
	protected int animationSpeed = DEFAULT_ANIM_SPEED; // how many ticks between animation frames
	protected int animationTimer = 0; // how many ticks since the last frame update

	/** This Mob's hitbox. The xLocation and yLocation variables are relative to the Mob's position. */
	protected Rectangle hitbox;

	protected double mass = 0;
	
	/**
	 * If it's time to change frames, do it. Will not work if the image is not an animation.
	 */
	public void animate() {
		if (animationTimer % animationSpeed == 0) {
			animation.next();
		}
		animationTimer++;
	}

	/**
	 * Calculates the gravitational force from a given Mover and adds it to the movement vector. Does not change values for the given Mover, only for this one.
	 * 
	 * @param body the Mover to gravitate towards
	 */
	public void gravitate(Mass body) {
		// This is just the distance formula without the square root, because to apply gravity you have to square the distance, and that cancels out the sqare root operation
		double xDist = body.getExactX() - getExactX(), yDist = body.getExactY() - y;
		Vector2D gravity = new Vector2D(xDist, yDist);
		// law of gravitation: F = (m1 * m2) / (distance * distance)  normally you would also use the Gravitational constant but that doesn't matter here because units are arbitrary
		gravity.setMagnitude((mass * body.getMass()) / (xDist * xDist + yDist * yDist));
		movement.add(gravity);
	}
	
	public double getMass() {
		return mass;
	}

	/**
	 * Writes this Mob's data in order:
	 * 
	 * <ul>
	 * <li>animationTimer int</li>
	 * <li>animation.frame int</li>
	 * </ul>
	 */
	@Override
	public void save(DataOutputStream out) {
		super.save(out);
		try {
			out.writeInt(animationTimer);
			out.writeInt(animation.getFrame());
		} catch (Exception e) {
			Err.error("Player can't save data!");
			e.printStackTrace();
		}
	}

	/**
	 * Loads a Mob's saved data.
	 * 
	 * @see #save(DataOutputStream)
	 */
	@Override
	public void load(DataInputStream in, String version) {
		super.load(in, version);
		try {
			animationTimer = in.readInt();
			animation.setFrame(in.readInt());
		} catch (Exception e) {
			Err.error("Player can't load data!");
			e.printStackTrace();
		}
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle((int) getExactX() + hitbox.x, (int) getExactY() + hitbox.y, hitbox.width, hitbox.height);
	}
}
