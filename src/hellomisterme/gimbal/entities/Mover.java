package hellomisterme.gimbal.entities;

import hellomisterme.gimbal.Err;
import hellomisterme.gimbal.Tick;

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

	public void save(DataOutputStream out) {
		try {
			out.writeDouble(x);
			out.writeDouble(y);
		} catch (IOException e) {
			Err.error("Can't save Mover data!");
			e.printStackTrace();
		}
	}
	
	public void load(DataInputStream in, int version) {
		try {
			setPos(in.readDouble(), in.readDouble());
		} catch (IOException e) {
			Err.error("Can't read Mover data!");
			e.printStackTrace();
		}
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
}
