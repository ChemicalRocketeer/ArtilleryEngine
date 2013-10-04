package hellomisterme.artillery_engine.behaviors;

import hellomisterme.artillery_engine.Entity;
import hellomisterme.artillery_engine.components.Component;
import hellomisterme.artillery_engine.components.physics.FreeBody;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Gravity implements Behavior {
	
	private List<FreeBody> pullingBodies = new ArrayList<FreeBody>();
	private List<FreeBody> pulledBodies = new ArrayList<FreeBody>();
	
	public Gravity() {
	}
	
	@Override
	public void tick() {
		for (FreeBody body : pulledBodies) {
			body.applyForce(body.calculateGravity(pullingBodies));
		}
	}
	
	@Override
	public void addEntity(Entity e) {
		Collection<Component> comps = e.getComponents(FreeBody.class);
		if (comps.size() > 0) {
			for (Component comp : comps) {
				FreeBody body = (FreeBody) comp;
				if (body.pulledByGravity)
					pulledBodies.add(body);
				if (body.pullsWithGravity)
					pullingBodies.add(body);
			}
		}
	}
	
	@Override
	public void removeEntity(Entity e) {
		Collection<Component> comps = e.getComponents(FreeBody.class);
		if (comps.size() > 0) {
			for (Component comp : comps) {
				FreeBody body = (FreeBody) comp;
				pulledBodies.remove(body);
				pullingBodies.remove(body);
			}
		}
	}
}
