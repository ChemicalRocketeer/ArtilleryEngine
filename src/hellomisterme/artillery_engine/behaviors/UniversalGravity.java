package hellomisterme.artillery_engine.behaviors;

import hellomisterme.artillery_engine.Entity;
import hellomisterme.artillery_engine.components.physics.FreeBody;
import hellomisterme.artillery_engine.util.Vector;

import java.util.HashSet;
import java.util.Set;

public class UniversalGravity extends Behavior {

	private Set<FreeBody> bodies = new HashSet<>();
	private Vector gravity;

	public UniversalGravity() {
		gravity = new Vector(0, 1);
	}

	public UniversalGravity(Vector gravity) {
		this.gravity = gravity;
	}

	@Override
	public void run() {
		for (FreeBody fb : bodies) {
			fb.velocity.add(gravity);
		}
	}

	public boolean compatibleWith(Entity e) {
		return compatibleWith(e.getFreeBody());
	}

	private boolean compatibleWith(FreeBody fb) {
		return fb != null && fb.pulledByGravity;
	}

	/** Adds the given Entity if it is compatible */
	@Override
	public void addEntity(Entity e) {
		FreeBody fb = e.getFreeBody();
		if (compatibleWith(fb))
			bodies.add(fb);
	}

	public void removeEntity(Entity e) {
		bodies.remove(e.getFreeBody());
	}

	@Override
	public boolean contains(Entity e) {
		return bodies.contains(e.getFreeBody());
	}
}
