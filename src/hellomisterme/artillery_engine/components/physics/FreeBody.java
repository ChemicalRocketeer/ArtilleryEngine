package hellomisterme.artillery_engine.components.physics;

import hellomisterme.artillery_engine.Tick;
import hellomisterme.artillery_engine.components.Component;
import hellomisterme.util.Vector2;

import java.util.Collection;

/**
 * FreeBody do collision detection
 * 
 * @since 10-18-12
 * @author David Aaron Suddjian
 */
public class FreeBody extends Component implements Tick {

	public Vector2 velocity = new Vector2(0, 0);
	public double mass = 1.0;
	public double spin = 0.0;

	public static FreeBody create(Vector2 velocity, double mass, double spin) {
		FreeBody body = new FreeBody();
		body.velocity = velocity;
		body.mass = mass;
		body.spin = spin;
		return body;
	}

	@Override
	public void tick() {
		applyForce(gravity(getWorld().freebodies));
		entity.transform.position.add(velocity);
		entity.transform.rotation += spin;
	}

	public void applyForce(Vector2 force) {
		if (mass != 0.0) velocity.add(force.DIV(mass));
	}

	public Vector2 gravity(Collection<FreeBody> bodies) {
		Vector2 gravity = new Vector2(0, 0);
		for (FreeBody body : bodies) {
			if (body != this) {
				gravity.add(gravity(body));
			}
		}
		return gravity;
	}

	/**
	 * Calculates the gravitational force from a given FreeBody.
	 * 
	 * @param body the FreeBody to gravitate towards
	 */
	public Vector2 gravity(FreeBody body) {
		double bodyMass = body.mass;
		Vector2 gravity = new Vector2(0, 0);
		if (bodyMass != 0.0) {
			// This is just the distance formula without the square root, because to apply gravity you have to square the distance, and that cancels out the sqare root operation
			double xDist = body.entity.transform.position.x - entity.transform.position.x;
			double yDist = body.entity.transform.position.y - entity.transform.position.y;
			gravity.x = xDist;
			gravity.y = yDist;
			// law of gravitation: F = (m1 * m2) / (distance * distance) normally you would also use the Gravitational constant but that doesn't matter here because units are arbitrary
			gravity.setMagnitude(mass * body.mass / (xDist * xDist + yDist * yDist));
		}
		return gravity;
	}
}
