package hellomisterme.gimbal.entities.mob;

import hellomisterme.gimbal.entities.Mover;
import hellomisterme.gimbal.graphics.GimbalImage;
import hellomisterme.gimbal.graphics.LightweightAnimation;
import hellomisterme.gimbal.input.KeyInput;

import java.io.DataInputStream;

/**
 * The Player class. This is the player. Yessiree Bob.
 * 
 * @since 10-23-12
 * @author David Aaron Suddjian
 */
public class Player extends Mover {

	private double speed = 2.0;

	public static final double MAX_HEALTH = 10;
	private double health = MAX_HEALTH;
	private int DAMAGE_IMMUNITY_TIME = 30;
	private int damageImmunityTimer = 0;

	public Player() {
		image = new LightweightAnimation("graphics/sprites/player");
	}

	public void tick() {
		if (health <= 0) {
			die();
		}

		((LightweightAnimation) image).next();
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
		if (exactX >= bucket.getWidth() - image.getWidth()) { // right
			setPos(bucket.getWidth() - image.getWidth(), exactY);
			damage = true;
		} else if (exactX < 0) { // left
			setPos(0, exactY);
			damage = true;
		}
		if (exactY >= bucket.getHeight() - image.getHeight()) { // bottom
			setPos(exactX, bucket.getHeight() - image.getHeight());
			damage = true;
		} else if (exactY < 0) { // top
			setPos(exactX, 0);
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
		if (damageImmunityTimer % 5 >= 0 && damageImmunityTimer % 5 < 3) {
			return super.getImage();
		} else {
			return null;
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
