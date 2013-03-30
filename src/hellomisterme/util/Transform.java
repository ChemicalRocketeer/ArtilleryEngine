package hellomisterme.util;

import hellomisterme.artillery_engine.Err;
import hellomisterme.artillery_engine.io.Savable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Transform implements Savable {
	public Vector2 position = new Vector2(0, 0);
	public double rotation = 0.0;
	public Vector2 scale = new Vector2(1, 1);

	public Transform() {
		// everything is already set to the default!
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

	public void add(Transform other) {
		position.add(other.position);
		rotation += other.rotation;
		scale.add(other.scale);
	}

	public Transform ADD(Transform other) {
		return new Transform(position.ADD(other.position), rotation + other.rotation, scale.ADD(other.scale));
	}

	public void sub(Transform other) {
		position.sub(other.position);
		rotation -= other.rotation;
		scale.sub(other.scale);
	}

	public Transform SUB(Transform other) {
		return new Transform(position.SUB(other.position), rotation - other.rotation, scale.SUB(other.scale));
	}

	public void forward(double amount) {
		position.add(Vector2.fromAngle(rotation, amount));
	}

	@Override
	public Transform clone() {
		return new Transform(position, rotation, scale);
	}

	@Override
	public void write(DataOutputStream out) {
		position.write(out);
		scale.write(out);
		try {
			out.writeDouble(rotation);
		} catch (IOException e) {
			Err.error("Can't save transform data!", e);
			e.printStackTrace();
		}
	}

	@Override
	public void read(DataInputStream in) {
		position.read(in);
		scale.read(in);
		try {
			rotation = in.readDouble();
		} catch (IOException e) {
			Err.error("Can't read transform data!", e);
		}
	}

	@Override
	public void writeOncePerClass(DataOutputStream out) {
	}

	@Override
	public void readOncePerClass(DataInputStream in) {
	}
}
