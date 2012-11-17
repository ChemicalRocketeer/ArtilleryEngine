package hellomisterme.gimbal.entities.mob;

import hellomisterme.gimbal.Err;
import hellomisterme.gimbal.entities.Mover;
import hellomisterme.gimbal.graphics.GimbalImage;
import hellomisterme.gimbal.graphics.LightweightAnimation;
import hellomisterme.gimbal.input.KeyInput;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * The Player class. This is the player. Yessiree Bob.
 * 
 * @since 10-23-12
 * @author David Aaron Suddjian
 */
public class Player extends Mover {

	private double speed = 2.0;

	public static final int ANIMATION_SPEED = 5; // how many ticks between animation frames
	private int animationTimer = 0; // how many ticks since the last frame update

	public static final double MAX_HEALTH = 10.0;
	private double health = MAX_HEALTH;
	private int DAMAGE_IMMUNITY_TIME = 30; // how long player will be immune after being damaged
	private int damageImmunityTimer = 0;

	public Player() {
		image = new LightweightAnimation("graphics/sprites/player");
	}

	public void tick() {
		if (health <= 0) {
			die();
		}

		if (animationTimer == ANIMATION_SPEED) {
			((LightweightAnimation) image).next();
			animationTimer = 0;
		} else {
			animationTimer++;
		}

		// Move if the right button(s) are held down
		if (KeyInput.pressed(KeyInput.up)) {
			setPos(getExactX(), getExactY() - speed * .5);
		}
		if (KeyInput.pressed(KeyInput.down)) {
			setPos(getExactX(), getExactY() + speed * .5);
		}
		if (KeyInput.pressed(KeyInput.left)) {
			setPos(getExactX() - speed, getExactY());
		}
		if (KeyInput.pressed(KeyInput.right)) {
			setPos(getExactX() + speed, getExactY());
		}

		// correct for out of bounds
		boolean damage = false;
		if (x >= bucket.getWidth() - image.getWidth()) { // right
			setPos(bucket.getWidth() - image.getWidth(), y);
			damage = true;
		} else if (x < 0) { // left
			setPos(0, y);
			damage = true;
		}
		if (y >= bucket.getHeight() - image.getHeight()) { // bottom
			setPos(x, bucket.getHeight() - image.getHeight());
			damage = true;
		} else if (y < 0) { // top
			setPos(x, 0);
			damage = true;
		}

		// take damage
		if (damageImmunityTimer > 0) {
			damageImmunityTimer--;
		} else if (damage) {
			takeDamage(1.0);
		}
	}

	public void takeDamage(double damage) {
		health -= damage;
		damageImmunityTimer = DAMAGE_IMMUNITY_TIME;
	}

	public void die() {
	}

	@Override
	public GimbalImage getImage() {
		if (damageImmunityTimer % 10 >= 0 && damageImmunityTimer % 10 < 5) {
			return super.getImage();
		} else {
			return null;
		}
	}
	
	/**
	 * Writes this Player's data in order:
	 * 
	 * <ul>
	 * <li>animationTimer int</li>
	 * <li>health double</li>
	 * <li>damageImmunityTimer int</li>
	 * </ul>
	 */
	@Override
	public void saveData(DataOutputStream out) {
		try {
			out.writeInt(animationTimer);
			out.writeDouble(health);
			out.writeInt(damageImmunityTimer);
		} catch (Exception e) {
			Err.error("Player can't save data!");
			e.printStackTrace();
		}
	}
	
	/**
	 * Loads a Player's saved data. See saveData().
	 */
	@Override
	public void loadData(DataInputStream in) {
		try {
			animationTimer = in.readInt();
			health = in.readDouble();
			damageImmunityTimer = in.readInt();
		} catch (Exception e) {
			Err.error("Player can't load data!");
			e.printStackTrace();
		}
	}

	/**
	 * Loads saved image data. Can be overridden to load other types of images.
	 */
	@Override
	public void loadImage(DataInputStream in) {
		image = new LightweightAnimation();
		image.load(in);
	}
}
