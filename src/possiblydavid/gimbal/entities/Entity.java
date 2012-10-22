package possiblydavid.gimbal.entities;

import possiblydavid.gimbal.graphics.LightweightImage;

/**
 * An Entity contains a LightweightImage and x and y coordinates. Subclasses can be rendered.
 * 
 * @author David Aaron Suddjian
 */
public abstract class Entity {

	protected int x = 0, y = 0;
	protected LightweightImage image;
	
	public Entity() {
		image = new LightweightImage();
	}

	public LightweightImage getImage() {
		return image;
	}
	
	public void setImage(LightweightImage img) {
		image = img;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setPos(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
