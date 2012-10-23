package possiblydavid.gimbal.entities;

import possiblydavid.gimbal.Tick;
import possiblydavid.gimbal.world.World;

/**
 * A Mover has the ability to move to different positions. It moves "smoothly" with the use of doubles for storage of position data instead of ints.
 * 
 * The delta variables store the change in movement and can be used to manipulate a Mover's position over a period of time.
 * 
 * @since 10-18-12
 * @author David Aaron Suddjian
 */
public abstract class Mover extends Entity implements Tick {

	protected double deltaX = 0, deltaY = 0;
	protected double exactX = 0, exactY = 0;
	protected World world;

	public void move() {
		exactX += deltaX;
		exactY += deltaY;
	}

	public void setWorld(World w) {
		world = w;
	}

	public double getDeltaX() {
		return deltaX;
	}

	public double getDeltaY() {
		return deltaY;
	}

	public void setDelta(double deltaX, double deltaY) {
		this.deltaX = deltaX;
		this.deltaY = deltaY;
	}

	public double getExactX() {
		return exactX;
	}

	public double getExactY() {
		return exactY;
	}

	public void setExactPos(double x, double y) {
		exactX = x;
		exactY = y;
	}

	@Override
	public int getX() {
		return (int) exactX;
	}

	@Override
	public int getY() {
		return (int) exactY;
	}

	@Override
	public void setPos(int x, int y) {
		exactX = x;
		exactY = y;
	}
}
