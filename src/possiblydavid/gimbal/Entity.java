package possiblydavid.gimbal;

import possiblydavid.gimbal.graphics.LightImg;

/**
 * An Entity contains a LightImg and x and y coordinates. Subclasses can be displayed.
 * 
 * @author David Aaron Suddjian
 */
public abstract class Entity {

	protected int x, y;
	protected LightImg image;

	public Entity() {
		image = new LightImg();
		x = 0;
		y = 0;
	}

	public LightImg getImage() {
		return image;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
}
