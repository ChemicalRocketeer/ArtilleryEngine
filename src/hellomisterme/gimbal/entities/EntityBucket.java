package hellomisterme.gimbal.entities;

import java.util.List;

/**
 * Entities need an outside object they can get data from. An object implementing EntityBucket fulfills this role.
 * 
 * @since 11-5-12
 * @author David Aaron Suddjian
 */
public interface EntityBucket {
	
	public void addEntity(Entity e);
	public void removeEntity(Entity e);
	public int getWidth();
	public int getHeight();
	public List<Entity> getEntities();
}
