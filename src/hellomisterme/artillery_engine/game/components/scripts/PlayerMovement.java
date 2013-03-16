package hellomisterme.artillery_engine.game.components.scripts;

import hellomisterme.artillery_engine.game.components.PhysicsForces;
import hellomisterme.artillery_engine.io.Keyboard;
import hellomisterme.util.Vector2;

public class PlayerMovement extends Script {

	private PhysicsForces forces;
	private double rotation = Math.PI / 2;
	private double angularMomentum = 0;
	private double rotationSpeed = 0.0005;

	private double enginePower = 0.05;
	private double throttle = 0;
	private static final double MAX_THROTTLE = 1;
	private double throttleRate = 0.01;

	public PlayerMovement() {
		require(PhysicsForces.class);
	}

	@Override
	public void init() {
		forces = (PhysicsForces) entity.getComponent(PhysicsForces.class);
	}

	@Override
	public void tick() {
		System.out.println("tick");
		// if (Keyboard.Controls.UP.pressed()) velocity.add(new Vector2D(0, -enginePower));
		// if (Keyboard.Controls.DOWN.pressed()) velocity.add(new Vector2D(0, enginePower));
		// if (Keyboard.Controls.LEFT.pressed()) velocity.add(new Vector2D(-enginePower, 0));
		// if (Keyboard.Controls.RIGHT.pressed()) velocity.add(new Vector2D(enginePower, 0));

		if (Keyboard.Controls.ROTLEFT.pressed()) angularMomentum -= rotationSpeed;
		if (Keyboard.Controls.ROTRIGHT.pressed()) angularMomentum += rotationSpeed;
		rotation += angularMomentum;

		if (Keyboard.Controls.THROTTLEUP.pressed()) throttle += throttleRate;
		if (Keyboard.Controls.THROTTLEDOWN.pressed()) throttle -= throttleRate;
		// if (Keyboard.Controls.THROTTLECUT.pressed()) throttle = 0;
		if (throttle < 0) {
			throttle = 0;
		} else if (throttle > MAX_THROTTLE) {
			throttle = MAX_THROTTLE;
		}

		rotation += angularMomentum;
		if (throttle != 0) forces.applyForce(Vector2.fromAngle(rotation - Math.PI / 2, throttle * enginePower));
	}

}
