package hellomisterme.artillery_engine.behaviors;

import hellomisterme.artillery_engine.Entity;
import hellomisterme.artillery_engine.components.physics.CollisionResult;
import hellomisterme.artillery_engine.components.physics.FreeBody;
import hellomisterme.artillery_engine.geometry.Circle;
import hellomisterme.artillery_engine.util.Vector;

import java.util.List;

public class Collision extends System {
	
	@Override
	public void run(List<Entity> entities) {
		for (int i = 0; i < entities.size(); i++) {
			FreeBody iBody = (FreeBody) entities.get(i).getComponent(FreeBody.class);
			if (iBody != null && iBody.collisionOn) {
				Circle iCircle = (Circle) entities.get(i).getComponent(Circle.class);
				if (iCircle != null) {
					// we have established that entities[i] is useful, so now check entities[i] against every other usable entity
					// this loop compares each pair of circles instead of each circle individually
					for (int j = i + 1; j < entities.size(); j++) {
						FreeBody jBody = (FreeBody) entities.get(j).getComponent(FreeBody.class);
						if (jBody != null && jBody.collisionOn) {
							Circle jCircle = (Circle) entities.get(j).getComponent(Circle.class);
							if (jCircle != null) {
								// j is a usable entity, now we can finally check collision
								handleCollisions(iBody, iCircle, jBody, jCircle);
							}
						}
					}
				}
			}
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
				aBody.entity.transform.position.add(collision.correction.MUL(bMassFraction));
				collision.correction.mul(-aMassFraction); // correction now points towards the collision
				bBody.entity.transform.position.add(collision.correction);
				
				// apply an equal and opposite force to both bodies
				Vector correctionUnit = collision.correction.clone(); // correction as a unit vector
				correctionUnit.normalize();
				//double iMag = aProjected.mag() + bProjected.mag();
				double iMag = Math.abs(Vector.dot(aBody.getMomentum(), correctionUnit)) + Math.abs(Vector.dot(bBody.getMomentum(), correctionUnit));
				Vector impulse = Vector.fromAngle(collision.correction.angle(), iMag);
				//impulse.mul((aBody.mass + bBody.mass));
				bBody.applyForce(impulse);
				impulse.negate(); // impulse points towards a
				aBody.applyForce(impulse);
			}
		}
	}
}
