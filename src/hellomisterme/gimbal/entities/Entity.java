package hellomisterme.gimbal.entities;

import hellomisterme.gimbal.graphics.LightweightImage;
import hellomisterme.gimbal.world.World;

/**
 * An Entity contains a LightweightImage and x and y coordinates. Subclasses can be rendered.
 * 
 * @since 10-16-12
 * @author David Aaron Suddjian
 */
public abstract class Entity {

	private int x = 0, y = 0;
	protected LightweightImage image = new LightweightImage();
	protected World world;

	public void setWorld(World w) {
		world = w;
	}

	public LightweightImage getImage() {
		return image;
	}

	public void setImage(LightweightImage img) {
		image = img;
	}
	
	public void setImage(String path) {
		image.setImage(path);
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
