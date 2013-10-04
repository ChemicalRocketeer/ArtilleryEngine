package hellomisterme.artillery_engine.components;

import hellomisterme.util.Transform;
import hellomisterme.util.Vector;

public abstract class IngameComponent extends Component {
	
	public Transform transform = new Transform();
	
	/**
	 * @return a clone of the global position. Don't worry about changing values.
	 */
	public Vector globalPosition() {
		if (entity == null)
			return transform.position.clone();
		Vector pos = entity.transform.position.clone();
		pos.add(transform.position);
		return pos;
	}
	
	public Vector globalScale() {
		if (entity == null)
			return transform.scale.clone();
		Vector scale = entity.transform.scale.clone();
		scale.mul(transform.scale);
		return scale;
	}
	
	public double globalRotation() {
		if (entity == null)
			return transform.rotation;
		return entity.transform.rotation + transform.rotation;
	}
	
	public Transform globalTransform() {
		if (entity == null)
			return transform.clone();
		Transform trans = entity.transform.clone();
		trans.add(transform);
		return trans;
	}
}
