package hellomisterme.artillery_engine.behaviors;

import hellomisterme.artillery_engine.Entity;
import hellomisterme.artillery_engine.components.physics.FreeBody;
import hellomisterme.artillery_engine.util.Vector;

import java.util.List;

public class UniversalGravity extends System {

	private Vector gravity;

	public UniversalGravity() {
		gravity = new Vector(0, 1);
	}

	public UniversalGravity(Vector gravity) {
		this.gravity = gravity;
	}

	@Override
	public void run(List<Entity> entities) {
		for (Entity e : entities) {
			FreeBody fb = e.getFreeBody();
			if (fb != null && fb.pulledByGravity) {
				fb.velocity.add(gravity);
			}
		}
	}
}
