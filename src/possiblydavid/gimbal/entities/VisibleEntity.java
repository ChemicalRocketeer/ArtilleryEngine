package possiblydavid.gimbal.entities;

import possiblydavid.gimbal.graphics.LightweightImage;

/**
 * A VisibleEntity contains a LightweightImage and x and y coordinates.
 * Subclasses can be displayed.
 * 
 * @author David Aaron Suddjian
 */
public class VisibleEntity extends Entity {

	protected int x, y;
	protected LightweightImage image;

	public LightweightImage getImage() {
		return image;
	}
}
