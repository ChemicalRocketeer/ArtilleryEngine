package hellomisterme.artillery_engine.components;

import hellomisterme.util.Transform;
import hellomisterme.util.Vector2;

public abstract class IngameComponent extends Component {

	public Transform transform = new Transform();

	/**
	 * @return a clone of the global position. Don't worry about changing values.
	 */
	public Vector2 globalPosition() {
		Vector2 pos = entity.transform.position.clone();
		pos.add(transform.position);
		return pos;
	}

	public Vector2 globalScale() {
		Vector2 scale = entity.transform.scale.clone();
		scale.mul(transform.scale);
		return scale;
	}

	public double globalRotation() {
		return entity.transform.rotation + transform.rotation;
	}

	public Transform globalTransform() {
		Transform trans = entity.transform.clone();
		trans.add(transform);
		return trans;
	}
}
