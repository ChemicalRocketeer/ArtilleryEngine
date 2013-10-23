package hellomisterme.artillery_engine.geometry;

import hellomisterme.artillery_engine.components.physics.CollisionResult;

public interface Hitbox {
	
	public CollisionResult getCollisionResult(Circle other);
	
	public boolean collides(Circle other);

	public AABB getAABB();
}
