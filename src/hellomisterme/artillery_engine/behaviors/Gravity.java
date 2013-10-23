package hellomisterme.artillery_engine.behaviors;

import hellomisterme.artillery_engine.Entity;
import hellomisterme.artillery_engine.components.physics.FreeBody;
import hellomisterme.artillery_engine.util.Vector;

import java.util.List;

public class Gravity extends System {
	
	@Override
	public void run(List<Entity> entities) {
		for (Entity e1 : entities) {
			FreeBody pulled = e1.getFreeBody();
			if (pulled != null && pulled.pulledByGravity) {
				for (Entity e2 : entities) {
					if (e2 != e1) {
						FreeBody pulling = e2.getFreeBody();
						if (pulling != null && pulling != pulled && pulling.pullsWithGravity) {
							Vector gravity = new Vector(0, 0);

							if (pulling.mass != 0 && pulled.mass != 0) {
								Vector pos = e1.globalPosition();
								Vector otherpos = e2.globalPosition();
								double xDist = otherpos.x - pos.x;
								double yDist = otherpos.y - pos.y;
								gravity.x = xDist;
								gravity.y = yDist;
								// law of gravitation: F = (m1 * m2) / (distance * distance) normally you would also use the
								// Gravitational constant but that doesn't matter here because units are arbitrary
								gravity.setMagnitude(pulled.mass * pulling.mass / (xDist * xDist + yDist * yDist));
							}
							pulled.applyForce(gravity);
						}
					}
				}
			}
		}
	}
}
