package hellomisterme.artillery_engine.game.components;

import hellomisterme.artillery_engine.game.Transform;
import hellomisterme.artillery_engine.io.Savable;
import hellomisterme.util.Vector2;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class IngameComponent extends Component implements Savable {

	public Transform transform = new Transform();

	public Vector2 globalPosition() {
		if (transform != null) {
			return transform.position.ADD(entity.transform.position);
		}
		return entity.transform.position;
	}

	public Vector2 localPosition() {
		if (transform != null) {
			return transform.position;
		}
		return new Vector2(0, 0);
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
	public void writeStatic(DataOutputStream out) {
		transform.writeStatic(out);
	}

	@Override
	public void readStatic(DataInputStream in) {
		transform.readStatic(in);
	}
}
