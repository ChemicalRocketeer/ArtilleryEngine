package hellomisterme.artillery_engine.components.scripts;

import hellomisterme.artillery_engine.components.physics.FreeBody;
import hellomisterme.artillery_engine.io.Keyboard;
import hellomisterme.util.Vector2;

public class PlayerMovement extends Script {

	private FreeBody freeBody;

	private double angularMomentum = 0;
	private double rotationSpeed = 0.0005;

	private double engineBasePower = 0.05;
	private double throttle = 0;
	public static final double MAX_THROTTLE = 1;
	private double throttleRate = 0.01;

	public PlayerMovement() {
	}

	@Override
	public void init() {
		require(FreeBody.class);
		freeBody = (FreeBody) entity.getComponent(FreeBody.class);
	}

	@Override
	public void tick() {
		if (Keyboard.Controls.ROTLEFT.pressed()) angularMomentum -= rotationSpeed;
		if (Keyboard.Controls.ROTRIGHT.pressed()) angularMomentum += rotationSpeed;

		if (Keyboard.Controls.THROTTLEUP.pressed()) throttle += throttleRate;
		if (Keyboard.Controls.THROTTLEDOWN.pressed()) throttle -= throttleRate;
		if (Keyboard.Controls.THROTTLECUT.pressed()) throttle = 0;
		if (throttle < 0) {
			throttle = 0;
		} else if (throttle > MAX_THROTTLE) {
			throttle = MAX_THROTTLE;
		}

		entity.transform.rotation += angularMomentum;
		if (throttle != 0) freeBody.applyForce(Vector2.fromAngle(entity.transform.rotation - Math.PI / 2, throttle * engineBasePower));
	}

}
