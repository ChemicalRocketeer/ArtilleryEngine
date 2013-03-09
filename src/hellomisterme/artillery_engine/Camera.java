package hellomisterme.artillery_engine;

import hellomisterme.artillery_engine.io.Savable;
import hellomisterme.util.Vector2D;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @since 2013-3-3
 * @author David Aaron Suddjian
 */
public class Camera implements Savable, Tick {

	private double x = 0, y = 0;

	private int scale = 1;

	public Camera(int width, int scale) {
		this.scale = scale;
		x = Game.getWorld().player.getX();
		y = Game.getWorld().player.getY();
	}

	@Override
	public void tick() {
		double targetX = Game.getWorld().player.getX();
		double targetY = Game.getWorld().player.getY();

		Vector2D m = new Vector2D(targetX - x, targetY - y);
		m.setMagnitude(m.mag() / 2);
		x += m.x;
		y += m.y;
	}

	@Override
	public void save(DataOutputStream out) {
		try {
			out.writeDouble(x);
			out.writeDouble(y);
		} catch (IOException e) {
			Err.error("Camera save error!");
			e.printStackTrace();
		}
	}

	@Override
	public void load(DataInputStream in, String version) {
		try {
			x = in.readDouble();
			y = in.readDouble();
		} catch (IOException e) {
			Err.error("Camera load error!");
			e.printStackTrace();
		}
	}

	public int getScale() {
		return scale;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public void setPos(double x, double y) {
		this.x = x;
		this.y = y;
	}
}
