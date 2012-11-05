package hellomisterme.gimbal.entities;

public interface EntityHolder {
	public void add(Entity e);
	public void remove(Entity e);
	public int getWidth();
	public int getHeight();
}
