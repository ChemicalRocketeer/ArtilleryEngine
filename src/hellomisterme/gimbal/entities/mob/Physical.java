package hellomisterme.gimbal.entities.mob;

import java.awt.Rectangle;

/**
 * Physical allows for objects to access each other's bounding boxes.
 * 
 * @since 11-22-12
 * @author David Aaron Suddjian
 */
public interface Physical {

	/**
	 * @return A bounding Rectangle set to the correct global position, i.e. a Rectangle at its actual position
	 */
	public Rectangle getBounds();
	
}
