package hellomisterme.artillery_engine.behaviors;

import hellomisterme.artillery_engine.Entity;

public abstract class Behavior {

	public static final String out = null;

	public abstract boolean compatibleWith(Entity e);

	public abstract boolean contains(Entity e);

	public abstract void addEntity(Entity e);

	public abstract void removeEntity(Entity e);

	public abstract void run();
}
