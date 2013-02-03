package hellomisterme.artillery_engine.entities;

import hellomisterme.artillery_engine.Err;
import hellomisterme.artillery_engine.Tick;
import hellomisterme.artillery_engine.entities.mob.Vector2D;
import hellomisterme.artillery_engine.world.World;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * A Mover has the ability to move to different positions. It moves "smoothly" with the use of doubles for storage of position data instead of ints.
 * 
 * The delta variables store the change in movement and can be used to manipulate a Mover's position over a period of time.
 * 
 * @since 10-18-12
 * @author David Aaron Suddjian
 */
public abstract class Mover extends Entity implements Tick {

	protected double x = 0, y = 0;
	protected Vector2D movement = new Vector2D(0, 0);

	public void save(DataOutputStream out) {
		try {
			out.writeDouble(x);
			out.writeDouble(y);
		} catch (IOException e) {
			Err.error("Can't save Mover data!");
			e.printStackTrace();
		}
	}

	public void load(DataInputStream in, String version) {
		try {
			setPos(in.readDouble(), in.readDouble());
		} catch (IOException e) {
			Err.error("Can't read Mover data!");
			e.printStackTrace();
		}
	}

	/**
	 * Moves this Mover along its movement vector.
	 */
	public void move() {
		x += movement.getXLength();
		y += movement.getYLength();
	}

	public double getExactX() {
		return x;
	}

	public double getExactY() {
		return y;
	}

	@Override
	public int getX() {
		return (int) x;
	}

	@Override
	public int getY() {
		return (int) y;
	}

	public void setPos(double x, double y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public void setPos(int x, int y) {
		setPos((double) x, (double) y);
	}
	
	public void setMovement(Vector2D m) {
		movement = m;
	}
	
	public Vector2D getMovement() {
		return movement;
	}
	
	/**
	 * If this Mover is outside the world bounds, sets the x and y positions to be exactly at the edge of the world bounds.
	 */
	public void correctOOB() {
		World w = getWorld();
		if (x >= w.getWidth() - image.getWidth()) { // right
			setPos(w.getWidth() - image.getWidth() - 1, y);
		} else if (getExactX() < 0) { // left
			setPos(0, getExactY());
		}
		if (y >= w.getHeight() - image.getHeight()) { // bottom
			setPos(x, w.getHeight() - image.getHeight() - 1);
		} else if (y < 0) { // top
			setPos(x, 0);
		}
	}
}
