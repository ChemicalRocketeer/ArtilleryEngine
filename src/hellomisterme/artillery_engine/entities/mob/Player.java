package hellomisterme.artillery_engine.entities.mob;

import hellomisterme.artillery_engine.Err;
import hellomisterme.artillery_engine.graphics.Sprite;
import hellomisterme.artillery_engine.io.Keyboard;
import hellomisterme.util.Vector2D;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * The Player class. This is the player. Yessiree Bob.
 * 
 * @since 10-23-12
 * @author David Aaron Suddjian
 */
public class Player extends Mob {

	private double movementSpeed = 0.1;

	public static final double MAX_HEALTH = 10.0;
	private double health = MAX_HEALTH;
	private static final int DAMAGE_IMMUNITY_TIME = 30; // how long player will be immune after being damaged
	private int damageImmunityTimer = 0;

	private boolean dead = false;

	public Player() {
		setImage(new Sprite("graphics/sprites/ship.png"));
		setVelocity(new Vector2D(0, 0));
		mass = 500;
	}

	@Override
	public void tick() {

		if (health <= 0) {
			die();
		}

		handleMovement();
		move();
	}

	private void handleMovement() {
		if (Keyboard.Controls.UP.pressed()) {
			velocity.add(new Vector2D(0, -movementSpeed));
		}
		if (Keyboard.Controls.DOWN.pressed()) {
			velocity.add(new Vector2D(0, movementSpeed));
		}
		if (Keyboard.Controls.LEFT.pressed()) {
			velocity.add(new Vector2D(-movementSpeed, 0));
		}
		if (Keyboard.Controls.RIGHT.pressed()) {
			velocity.add(new Vector2D(movementSpeed, 0));
		}
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

	/**
	 * Writes this Player's data.
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
	 * Loads a Player's saved data.
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
