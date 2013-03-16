package hellomisterme.artillery_engine.game.components;

import hellomisterme.artillery_engine.Tick;
import hellomisterme.util.Vector2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Collection;

/**
 * A Mover has the ability to move to different positions. It moves "smoothly" with the use of doubles for storage of position data instead of ints.
 * 
 * The delta variables store the change in velocity and can be used to manipulate a Mover's position over a period of time.
 * 
 * @since 10-18-12
 * @author David Aaron Suddjian
 */
public class PhysicsForces extends Component implements Tick {

	public Vector2 velocity = new Vector2(0, 0);

	public PhysicsForces() {
		this(new Vector2(0, 0));
	}

	public PhysicsForces(Vector2 velocity) {
		this.velocity = velocity;
		require(Mass.class);
	}

	@Override
	public void tick() {
		entity.transform.position.add(velocity);
	}

	/**
	 * Calculates the gravitational force from a given Mass and adds it to the velocity vector. Does not change values for the given Mover, only for this one.
	 * 
	 * @param body the Mover to gravitate towards
	 */
	public Vector2 gravity(Mass body) {
		// This is just the distance formula without the square root, because to apply gravity you have to square the distance, and that cancels out the sqare root operation
		double xDist = body.entity.transform.position.x - entity.transform.position.x;
		double yDist = body.entity.transform.position.y - entity.transform.position.y;
		Vector2 gravity = new Vector2(xDist, yDist);
		// law of gravitation: F = (m1 * m2) / (distance * distance)  normally you would also use the Gravitational constant but that doesn't matter here because units are arbitrary
		gravity.setMagnitude(((Mass) entity.getComponent(Mass.class)).mass * body.mass / (xDist * xDist + yDist * yDist));
		return gravity;
	}

	public Vector2 gravity(Collection<Mass> bodies) {
		Vector2 gravity = new Vector2(0, 0);
		for (Mass body : bodies) {
			if (body.entity != entity) {
				gravity.add(gravity(body));
			}
		}
		return gravity;
	}

	public void applyForce(Vector2 force) {
		velocity.add(force);
	}

	@Override
	public void writeStatic(DataOutputStream out) {
	}

	@Override
	public void read(DataInputStream in) {
	}

	@Override
	public void readStatic(DataInputStream in) {
	}

	@Override
	public void write(DataOutputStream out) {
	}
}
