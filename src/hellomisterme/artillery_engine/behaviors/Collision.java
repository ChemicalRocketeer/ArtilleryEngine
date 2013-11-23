package hellomisterme.artillery_engine.behaviors;

import hellomisterme.artillery_engine.Entity;
import hellomisterme.artillery_engine.components.physics.CollisionResult;
import hellomisterme.artillery_engine.components.physics.FreeBody;
import hellomisterme.artillery_engine.geometry.Circle;
import hellomisterme.artillery_engine.util.Vector;

public class Collision extends Behavior {

	/** A Holder holds information about an entity for efficient data access */
	private class Holder {
		FreeBody body;
		Circle circle;
		Holder next;

		public Holder(FreeBody body, Circle circle, Holder next) {
			this.body = body;
			this.circle = circle;
		}

		public boolean equals(Holder other) {
			return other != null && other.circle == circle && other.body == body;
		}
	}

	// private List<Holder> holders = new LinkedList<>();
	// I implement my own LinkedList structure here for performance because java's LinkedList
	// does not support cloning ListIterators
	private Holder firstHolder = null;

	@Override
	public void run() {
		// check every entity against every other entity
		Holder holder = firstHolder;
		while (holder != null) {
			if (holder.body.collisionOn) {
				Holder holder2 = holder.next;
				while (holder2 != null) {
					if (holder2.body.collisionOn) {
						handleCollisions(holder.body, holder.circle, holder2.body, holder2.circle);
					}
					holder2 = holder2.next;
				}
			}
			holder = holder.next;
		}
	}

	public static void handleCollisions(FreeBody aBody, Circle aCircle, FreeBody bBody, Circle bCircle) {
		// preliminary AABB check because it's cheap and will usually be sufficient
		if (aCircle.getAABB().intersects(bCircle.getAABB())) {
			CollisionResult collision = aCircle.getCollisionResult(bCircle);
			// check if they are already colliding
			if (collision.collision) {
				// move both bodies away from each other
				double totalMass = aBody.mass + bBody.mass;
				double aMassFraction = aBody.mass / totalMass;
				double bMassFraction = bBody.mass / totalMass;
				aBody.entity.transform.position.add(new Vector(collision.correction).mul(bMassFraction));
				collision.correction.mul(-aMassFraction); // correction now points towards the collision
				bBody.entity.transform.position.add(collision.correction);

				// apply an equal and opposite force to both bodies
				Vector correctionUnit = collision.correction.clone(); // correction as a unit vector
				correctionUnit.normalize();
				// double iMag = aProjected.mag() + bProjected.mag();
				double iMag = Math.abs(Vector.dot(aBody.getMomentum(), correctionUnit)) + Math.abs(Vector.dot(bBody.getMomentum(), correctionUnit));
				Vector impulse = Vector.fromAngle(collision.correction.angle(), iMag);
				// impulse.mul((aBody.mass + bBody.mass));
				bBody.applyForce(impulse);
				impulse.negate(); // impulse points towards a
				aBody.applyForce(impulse);
			}
		}
	}

	@Override
	public boolean compatibleWith(Entity e) {
		return compatibleWith(e.getFreeBody(), (Circle) e.getComponent(Circle.class));
	}

	private boolean compatibleWith(FreeBody fb, Circle c) {
		return fb != null && c != null;
	}

	@Override
	public void addEntity(Entity e) {
		FreeBody fb = e.getFreeBody();
		Circle c = (Circle) e.getComponent(Circle.class);
		if (compatibleWith(fb, c)) {
			Holder holder = new Holder(fb, c, firstHolder);
			// prevent duplicates
			if (!contains(holder)) {
				holder.next = firstHolder;
				firstHolder = holder;
			}
		}
	}

	@Override
	public void removeEntity(Entity e) {
		// generate a holder to represent this entity
		Holder testHolder = new Holder(e.getFreeBody(), e.getComponent(Circle.class), null);

		// eliminate any bodies at the beginning that match
		while (firstHolder.equals(testHolder)) {
			firstHolder = firstHolder.next;
		}
		if (firstHolder != null) {
			// search the rest of the nodes
			Holder holder = firstHolder.next;
			Holder previous = firstHolder;
			while (holder != null) {
				if (testHolder.equals(holder)) {
					previous.next = holder.next;
				} else {
					previous = holder;
				}
				holder = holder.next;
			}
		}
	}

	@Override
	public boolean contains(Entity e) {
		return contains(new Holder(e.getFreeBody(), (Circle) e.getComponent(Circle.class), null));
	}

	private boolean contains(Holder holder) {
		Holder iterator = firstHolder;
		while (iterator != null) {
			if (iterator.equals(holder))
				return true;
			iterator = iterator.next;
		}
		return false;
	}
}
