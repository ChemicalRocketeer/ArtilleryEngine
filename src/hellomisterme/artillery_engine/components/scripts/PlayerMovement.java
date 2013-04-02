package hellomisterme.artillery_engine.components.scripts;

import hellomisterme.artillery_engine.components.physics.FreeBody;
import hellomisterme.artillery_engine.io.Keyboard;

public class PlayerMovement extends Script {

	private FreeBody freeBody;

	private double rotationForce = 0.0005;
	private double enginePower = 0.05;
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
		if (Keyboard.Controls.ROTLEFT.pressed()) freeBody.spin -= rotationForce;
		if (Keyboard.Controls.ROTRIGHT.pressed()) freeBody.spin += rotationForce;

		if (Keyboard.Controls.THROTTLEUP.pressed()) throttle += throttleRate;
		if (Keyboard.Controls.THROTTLEDOWN.pressed()) throttle -= throttleRate;
		if (Keyboard.Controls.THROTTLECUT.pressed()) throttle = 0;
		if (throttle < 0) {
			throttle = 0;
		} else if (throttle > MAX_THROTTLE) {
			throttle = MAX_THROTTLE;
		}

		if (throttle != 0) freeBody.applyForce(entity.transform.FORWARD(throttle * enginePower));
	}

}
