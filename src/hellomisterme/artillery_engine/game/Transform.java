package hellomisterme.artillery_engine.game;

import hellomisterme.artillery_engine.Err;
import hellomisterme.artillery_engine.io.Savable;
import hellomisterme.util.Vector2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Transform implements Savable {
	public Vector2 position = new Vector2(0, 0);
	public double rotation = 0.0;
	public Vector2 scale = new Vector2(1, 1);

	public Transform() {
		// everything is already the default!
	}

	public Transform(Vector2 position, double rotation, Vector2 scale) {
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
	}

	public Transform(double posX, double posY, double rotation, double scaleX, double scaleY) {
		position = new Vector2(posX, posY);
		this.rotation = rotation;
		scale = new Vector2(scaleX, scaleY);
	}

	public void forward(double amount) {
		position.add(Vector2.fromAngle(rotation, amount));
	}

	@Override
	public void write(DataOutputStream out) {
		try {
			out.writeDouble(position.x);
			out.writeDouble(position.y);
			out.writeDouble(rotation);
			out.writeDouble(scale.x);
			out.writeDouble(scale.y);
		} catch (IOException e) {
			Err.error("Can't save transform data!");
			e.printStackTrace();
		}
	}

	@Override
	public void read(DataInputStream in) {
		try {
			position.read(in);
			rotation = in.readDouble();
			scale.read(in);
		} catch (IOException e) {
			Err.error("Can't read transform data!");
			e.printStackTrace();
		}
	}

	@Override
	public void writeStatic(DataOutputStream out) {
	}

	@Override
	public void readStatic(DataInputStream in) {
	}
}
