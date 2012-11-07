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

	protected double exactX = 0, exactY = 0;

	public void save(DataOutputStream out) {
		super.save(out);
		try {
			out.writeDouble(exactX);
			out.writeDouble(exactY);
		} catch (IOException e) {
			Err.error("Can't save Mover data!");
			e.printStackTrace();
		}
	}
	
	public void load(DataInputStream in) {
		super.load(in);
		try {
			exactX = in.readDouble();
			exactY = in.readDouble();
		} catch (IOException e) {
			Err.error("Can't read Mover data!");
			e.printStackTrace();
		}
	}
	
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
