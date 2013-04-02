package hellomisterme.artillery_engine.components;

import hellomisterme.util.Transform;
import hellomisterme.util.Vector2;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public abstract class IngameComponent extends Component {

	public Transform transform = new Transform();

	/**
	 * @return a clone of the global position. Don't worry about changing values.
	 */
	public Vector2 globalPosition() {
		Vector2 pos = entity.globalPosition();
		pos.add(transform.position);
		return pos;
	}

	public Vector2 globalScale() {
		Vector2 scale = entity.globalScale();
		scale.mul(transform.scale);
		return scale;
	}

	public double globalRotation() {
		return entity.globalRotation() + transform.rotation;
	}

	public Transform globalTransform() {
		Transform trans = entity.globalTransform();
		trans.add(transform);
		return trans;
	}

	@Override
	public void write(DataOutputStream out) {
		transform.write(out);
	}

	@Override
	public void read(DataInputStream in) {
		transform.read(in);
	}

	@Override
	public void writeOncePerClass(DataOutputStream out) {
		transform.writeOncePerClass(out);
	}

	@Override
	public void readOncePerClass(DataInputStream in) {
		transform.readOncePerClass(in);
	}
}
