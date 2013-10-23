package hellomisterme.artillery_engine.components.physics;

import hellomisterme.artillery_engine.util.Vector;

public class CollisionResult {

	public boolean collision = false;
	/** The vector needed to undo the collision */
	public Vector correction = new Vector();
}
