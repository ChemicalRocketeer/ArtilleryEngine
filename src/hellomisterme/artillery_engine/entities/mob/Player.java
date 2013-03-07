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

	private double enginePower = 0.1;
	private double throttle = 0;
	private static final double MAX_THROTTLE = 1;
	private double throttleRate = 0.01;

	private boolean dead = false;

	public Player() {
		super();
		setImage(new Sprite("graphics/sprites/ship.png"));
		setVelocity(new Vector2D(0, 0));
		mass = 1;
	}

	@Override
	public void tick() {
		gravitate(getWorld().getBodies());
		handleMovement();
		move();
	}

	private void handleMovement() {
		if (Keyboard.Controls.UP.pressed()) 	velocity.add(new Vector2D(0, -enginePower));
		if (Keyboard.Controls.DOWN.pressed()) 	velocity.add(new Vector2D(0, enginePower));
		if (Keyboard.Controls.LEFT.pressed()) 	velocity.add(new Vector2D(-enginePower, 0));
		if (Keyboard.Controls.RIGHT.pressed()) 	velocity.add(new Vector2D(enginePower, 0));
		if (Keyboard.Controls.THROTTLEUP.pressed()) throttle += throttleRate;
		if (Keyboard.Controls.THROTTLEDOWN.pressed()) throttle -= throttleRate;
		
		if (throttle < 0) {
			throttle = 0;
		} else if (throttle > MAX_THROTTLE) {
			throttle = MAX_THROTTLE;
		}
	}

	/**
	 * Writes this Player's data.
	 */
	@Override
	public void save(DataOutputStream out) {
		super.save(out);
		try {
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
		} catch (Exception e) {
			Err.error("Player can't load data!");
			e.printStackTrace();
		}
	}

	public void die() {
		dead = true;
	}

	public boolean dead() {
		return dead;
	}
}
