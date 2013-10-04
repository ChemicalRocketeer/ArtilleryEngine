package hellomisterme.artillery_engine.components.physics;

import hellomisterme.artillery_engine.Tick;
import hellomisterme.artillery_engine.components.Component;
import hellomisterme.util.Vector;

import java.util.Collection;

/**
 * FreeBody do collision detection
 * 
 * @since 10-18-12
 * @author David Aaron Suddjian
 */
public class FreeBody extends Component implements Tick {
	
	public Vector velocity = new Vector(0, 0);
	public double mass = 1.0;
	public double spin = 0.0;
	public boolean pullsWithGravity = true;
	public boolean pulledByGravity = true;
	
	public static FreeBody create(Vector velocity, double mass, double spin) {
		FreeBody body = new FreeBody();
		body.velocity = velocity;
		body.mass = mass;
		body.spin = spin;
		return body;
	}
	
	public static FreeBody create(Vector velocity, double mass, double spin, boolean pullsWithGravity, boolean pulledByGravity) {
		FreeBody body = new FreeBody();
		body.velocity = velocity;
		body.mass = mass;
		body.spin = spin;
		body.pullsWithGravity = pullsWithGravity;
		body.pulledByGravity = pulledByGravity;
		return body;
	}
	
	@Override
	public void tick() {
		entity.transform.position.add(velocity);
		entity.transform.rotation += spin;
	}
	
	public void applyForce(Vector force) {
		if (mass != 0.0) velocity.add(force.DIV(mass));
	}
	
	public Vector calculateGravity(Collection<FreeBody> bodies) {
		Vector gravity = new Vector(0, 0);
		for (FreeBody body : bodies) {
			if (body != this) {
				gravity.add(calculateGravity(body));
			}
		}
		return gravity;
	}
	
	/**
	 * Calculates the gravitational force from a given FreeBody.
	 * 
	 * @param body the FreeBody to gravitate towards
	 */
	public Vector calculateGravity(FreeBody body) {
		double bodyMass = body.mass;
		Vector gravity = new Vector(0, 0);
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
	
	public static boolean isMutuallyExclusive() {
		return true;
	}
}
