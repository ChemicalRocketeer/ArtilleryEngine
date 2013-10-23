package hellomisterme.artillery_engine.components;

import hellomisterme.artillery_engine.util.Transform;
import hellomisterme.artillery_engine.util.Vector;

public abstract class IngameComponent extends Component {
	
	public Transform transform = new Transform();
	
	/**
	 * @return a clone of the global position. Don't worry about changing values.
	 */
	public Vector globalPosition() {
		if (entity == null)
			return transform.position.clone();
		Vector pos = entity.globalPosition();
		pos.add(transform.position);
		return pos;
	}
	
	public Vector globalScale() {
		if (entity == null)
			return transform.scale.clone();
		Vector scale = entity.globalScale();
		scale.mul(transform.scale);
		return scale;
	}
	
	public double globalRotation() {
		if (entity == null)
			return transform.rotation;
		return entity.globalRotation() + transform.rotation;
	}
	
	public Transform globalTransform() {
		if (entity == null)
			return transform.clone();
		Transform trans = entity.globalTransform();
		trans.add(transform);
		return trans;
	}
}
