package hellomisterme.artillery_engine.entities;

import hellomisterme.artillery_engine.Err;
import hellomisterme.artillery_engine.Tick;
import hellomisterme.artillery_engine.graphics.BasicImage;
import hellomisterme.artillery_engine.io.Savable;
import hellomisterme.artillery_engine.world.World;
import hellomisterme.util.Vector2D;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collection;

/**
 * A Mover has the ability to move to different positions. It moves "smoothly" with the use of doubles for storage of position data instead of ints.
 * 
 * The delta variables store the change in velocity and can be used to manipulate a Mover's position over a period of time.
 * 
 * @since 10-18-12
 * @author David Aaron Suddjian
 */
public abstract class Mover extends Entity implements Mass, Tick, Savable {

	protected double x = 0, y = 0;
	protected Vector2D velocity = new Vector2D(0, 0);
	protected double mass = 0;
	
	protected Mover() {
		super();
		getWorld().addBody(this);
		getWorld().addSavable(this);
		getWorld().addTickable(this);
	}

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
	
	public void tick() {
		
	}

	/**
	 * Moves this Mover along its velocity vector.
	 */
	public void move() {
		x += velocity.x;
		y += velocity.y;
	}

	@Override
	public double getX() {
		return x;
	}

	@Override
	public double getY() {
		return y;
	}
	
	public int getIntX() {
		return (int) x;
	}
	
	public int getIntY() {
		return (int) y;
	}

	@Override
	public void setPos(double x, double y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public void setPos(int x, int y) {
		setPos((double) x, (double) y);
	}
	
	public void setVelocity(Vector2D v) {
		velocity = v;
	}
	
	public Vector2D getVelocity() {
		return velocity;
	}
	
	public double getMass() {
		return mass;
	}

	/**
	 * Calculates the gravitational force from a given Mass and adds it to the velocity vector. Does not change values for the given Mover, only for this one.
	 * 
	 * @param body the Mover to gravitate towards
	 */
	public void gravitate(Mass body) {
		// This is just the distance formula without the square root, because to apply gravity you have to square the distance, and that cancels out the sqare root operation
		double xDist = body.getX() - x, yDist = body.getY() - y;
		Vector2D gravity = new Vector2D(xDist, yDist);
		// law of gravitation: F = (m1 * m2) / (distance * distance)  normally you would also use the Gravitational constant but that doesn't matter here because units are arbitrary
		gravity.setMagnitude((mass * body.getMass()) / (xDist * xDist + yDist * yDist));
		velocity.add(gravity);
	}
	
	public void gravitate(Collection<Mass> bodies) {
		for (Mass body : bodies) {
			if (body != this) {
				gravitate(body);
			}
		}
	}
	
	/**
	 * If this Mover is outside the world bounds, sets the x and y positions to be exactly at the edge of the world bounds.
	 */
	public void correctOOB() {
		World w = getWorld();
		BasicImage img = getImage();
		if (x >= w.getWidth() - img.getWidth()) { // right
			x = w.getWidth() - img.getWidth() - 1;
		} else if (x < 0) { // left
			x = 0;
		}
		if (y >= w.getHeight() - img.getHeight()) { // bottom
			y = w.getHeight() - img.getHeight() - 1;
		} else if (y < 0) { // top
			y = 0;
		}
	}
}
