package hellomisterme.artillery_engine.entities.mob;

import hellomisterme.artillery_engine.Err;
import hellomisterme.artillery_engine.entities.Entity;
import hellomisterme.artillery_engine.graphics.AnimatedSprite;
import hellomisterme.artillery_engine.graphics.BasicImage;
import hellomisterme.artillery_engine.graphics.Sprite;
import hellomisterme.artillery_engine.io.Keyboard;
import hellomisterme.artillery_engine.world.World;

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

	private double movementSpeed = 0.1;

	private Sprite damaged;

	public static final double MAX_HEALTH = 10.0;
	private double health = MAX_HEALTH;
	private static final int DAMAGE_IMMUNITY_TIME = 30; // how long player will be immune after being damaged
	private int damageImmunityTimer = 0;

	private boolean dead = false;

	public Player() {
		animation = new AnimatedSprite("graphics/sprites/player");
		image = animation;
		damaged = new Sprite("graphics/sprites/player_damaged.png");
		hitbox = new Rectangle(10, 60, 50, 20);
		world = getWorld();
		movement = new Vector2D(0, 0);
	}

	@Override
	public void tick() {
		boolean damage = false; // can be changed to tell if the player is damaged or not

		if (health <= 0) {
			die();
		}

		animate();

		handleMovement();
		move();

		correctOOB();

		if (collides()) {
			damage = true;
		}

		// take damage if elegible
		if (damageImmunityTimer > 0) {
			damageImmunityTimer--;
			if (damageImmunityTimer % 10 >= 0 && damageImmunityTimer % 10 < 5) {
				image = animation;
			} else {
				image = damaged;
			}
		} else if (damage) {
			takeDamage(1.0);
		}
	}

	private void handleMovement() {
		if (Keyboard.pressed(Keyboard.up)) {
			movement.add(new Vector2D(0, -movementSpeed));
		}
		if (Keyboard.pressed(Keyboard.down)) {
			movement.add(new Vector2D(0, movementSpeed));
		}
		if (Keyboard.pressed(Keyboard.left)) {
			movement.add(new Vector2D(-movementSpeed, 0));
		}
		if (Keyboard.pressed(Keyboard.right)) {
			movement.add(new Vector2D(movementSpeed, 0));
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
	public BasicImage getImage() {
		if (damageImmunityTimer % 10 >= 0 && damageImmunityTimer % 10 < 5) {
			return super.getImage();
		} else {
			return damaged;
		}
	}

	/**
	 * Writes this Player's data in order:
	 * 
	 * <ul>
	 * <li>animationTimer int</li>
	 * <li>health double</li>
	 * <li>damageImmunityTimer int</li>
	 * <li>animation.frame int</li>
	 * </ul>
	 */
	@Override
	public void save(DataOutputStream out) {
		super.save(out);
		try {
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
	public void load(DataInputStream in, String version) {
		super.load(in, version);
		try {
			health = in.readDouble();
			damageImmunityTimer = in.readInt();
		} catch (Exception e) {
			Err.error("Player can't load data!");
			e.printStackTrace();
		}
	}
}
