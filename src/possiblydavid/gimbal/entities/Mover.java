package possiblydavid.gimbal.entities;

import possiblydavid.gimbal.Tick;

/**
 * A Mover has the ability to move to different positions. It moves "smoothly" with the use of doubles for storage of position data instead of ints.
 * 
 * The delta variables store the change in movement and can be used to manipulate a Mover's position over a period of time.
 * 
 * @since 10-18-12
 * @author David Aaron Suddjian
 */
public abstract class Mover extends Entity implements Tick {

	protected double exactX = 0, exactY = 0;

	public double getExactX() {
		return exactX;
	}

	public double getExactY() {
		return exactY;
	}

	public void setPos(double x, double y) {
		super.setPos((int) x, (int) y);
		exactX = x;
		exactY = y;
	}
	
	public void setPos(int x, int y) {
		super.setPos(x, y);
		setPos((double) x, (double) y); // have to cast or you get stack-overflow  >_<
	}
}
