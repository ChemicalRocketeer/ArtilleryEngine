package hellomisterme.artillery_engine.behaviors;

import hellomisterme.artillery_engine.Entity;
import hellomisterme.artillery_engine.components.physics.FreeBody;
import hellomisterme.artillery_engine.util.Vector;

import java.util.HashSet;
import java.util.Set;

public class Gravity extends Behavior {

	private Set<FreeBody> pullers = new HashSet<>();
	private Set<FreeBody> pullees = new HashSet<>();

	@Override
	public void run() {
		for (FreeBody pulled : pullees) {
			for (FreeBody pulling : pullers) {
				if (pulling != pulled) {
					if (pulling.mass != 0 && pulled.mass != 0) {
						Vector gravity = new Vector(0, 0);
						Vector pulledPos = pulled.entity.globalPosition();
						Vector pullingPos = pulling.entity.globalPosition();
						double xDist = pullingPos.x - pulledPos.x;
						double yDist = pullingPos.y - pulledPos.y;
						gravity.x = xDist;
						gravity.y = yDist;
						// law of gravitation: F = (m1 * m2) / (distance * distance) normally you would also use the
						// Gravitational constant but that doesn't matter here because units are arbitrary
						gravity.setMagnitude(pulled.mass * pulling.mass / (xDist * xDist + yDist * yDist));
						pulled.applyForce(gravity);
					}
				}
			}
		}
	}

	@Override
	public boolean compatibleWith(Entity e) {
		FreeBody fb = e.getFreeBody();
		return fb != null && (fb.pulledByGravity || fb.pullsWithGravity);
	}

	@Override
	public void addEntity(Entity e) {
		FreeBody fb = e.getFreeBody();
		if (fb != null) {
			if (fb.pullsWithGravity) {
				pullers.add(fb);
			}
			if (fb.pulledByGravity) {
				pullees.add(fb);
			}
		}
	}

	@Override
	public void removeEntity(Entity e) {
		FreeBody fb = e.getFreeBody();
		if (fb != null) {
			if (fb.pullsWithGravity)
				pullers.remove(fb);
			if (fb.pulledByGravity)
				pullees.remove(fb);
		}
	}

	@Override
	public boolean contains(Entity e) {
		FreeBody fb = e.getFreeBody();
		return pullers.contains(fb) || pullees.contains(fb);
	}
}
