package hellomisterme.gimbal.entities.mob;

import hellomisterme.gimbal.Err;
import hellomisterme.gimbal.Game;
import hellomisterme.gimbal.entities.Entity;
import hellomisterme.gimbal.graphics.GimbalImage;
import hellomisterme.gimbal.graphics.LightweightAnimation;
import hellomisterme.gimbal.io.KeyInput;
import hellomisterme.gimbal.world.World;

import java.awt.Rectangle;
import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * The Player class. This is the player. Yessiree Bob.
 * 
 * @since 10-23-12
 * @author David Aaron Suddjian
 */
public class Player extends Mob {
	
	public World world;

	private double movementSpeed = 2.0;

	public static final double MAX_HEALTH = 10.0;
	private double health = MAX_HEALTH;
	private static final int DAMAGE_IMMUNITY_TIME = 30; // how long player will be immune after being damaged
	private int damageImmunityTimer = 0;

	private boolean dead = false;

	public Player() {
		image = new LightweightAnimation("graphics/sprites/player");
		hitbox = new Rectangle(10, 60, 50, 20);
	}

	public void tick() {
		boolean damage = false; // can be changed to tell if the player is damaged or not

		if (health <= 0) {
			die();
		}

		animate();

		handleMovement();

		correctOOB();

		if (collides()) {
			damage = true;
		}

		// take damage if elegible
		if (damageImmunityTimer > 0) {
			damageImmunityTimer--;
		} else if (damage) {
			takeDamage(1.0);
		}
	}

	private void handleMovement() {
		int xMotion = 0, yMotion = 0;
		// Otherwise, must use more system resources to calculate.
		if (KeyInput.pressed(KeyInput.up)) {
			yMotion -= 1;
		}
		if (KeyInput.pressed(KeyInput.down)) {
			yMotion += 1;
		}
		if (KeyInput.pressed(KeyInput.left)) {
			xMotion -= 1;
		}
		if (KeyInput.pressed(KeyInput.right)) {
			xMotion += 1;
		}
		// Keep player from going faster when moving diagonally
		double diagAdjust = Math.sqrt(2);
		if (xMotion != 0 && yMotion != 0) { // moving diagonally
			setPos(x + (xMotion / diagAdjust) * movementSpeed, y + (yMotion / diagAdjust) * movementSpeed * Game.ISOMETRIC_RATIO);
		} else {
			setPos(x + xMotion * movementSpeed, y + yMotion * movementSpeed * Game.ISOMETRIC_RATIO);
		}
	}

	/**
	 * Checks if the Player collides with any physical Entity in its world
	 * 
	 * @return true if there is a collision, else false
	 */
	private boolean collides() {
		for (Entity e : world.getEntities()) {
			if (e != this && e instanceof Physical && ((Physical) e).getBounds().intersects(getBounds())) { // TODO change implementation so it doesn't use instanceof
				return true;
			}
		}
		return false;
	}

	public void takeDamage(double damage) {
		health -= damage;
		damageImmunityTimer = DAMAGE_IMMUNITY_TIME;
	}

	public void die() {
		dead = true;
	}

	public boolean dead() {
		return dead;
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
