package hellomisterme.artillery_engine.behaviors;

import hellomisterme.artillery_engine.Entity;
import hellomisterme.artillery_engine.Tick;

public interface Behavior extends Tick {
	
	public void addEntity(Entity e);
	
	public void removeEntity(Entity e);
}
