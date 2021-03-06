package hellomisterme.artillery_engine.util;


public class Transform {
	public Vector position = new Vector(0, 0);
	public double rotation = 0.0;
	public Vector scale = new Vector(1, 1);
	
	public Transform() {
		// everything is already set to the default!
	}
	
	public Transform(Vector position, double rotation, Vector scale) {
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
	}
	
	public Transform(double posX, double posY, double rotation, double scaleX, double scaleY) {
		position = new Vector(posX, posY);
		this.rotation = rotation;
		scale = new Vector(scaleX, scaleY);
	}
	
	public void add(Transform other) {
		position.add(other.position);
		position.rotate(other.rotation);
		position.mul(other.scale);
		rotation += other.rotation;
		scale.mul(other.scale);
	}
	
	public Transform ADD(Transform other) {
		Vector pos = new Vector(position).add(other.position);
		pos.rotate(other.rotation);
		pos.mul(other.scale);
		return new Transform(pos, rotation + other.rotation, new Vector(scale).mul(other.scale));
	}
	
	public void sub(Transform other) {
		position.sub(other.position);
		position.rotate(-other.rotation);
		position.div(other.scale);
		rotation -= other.rotation;
		scale.div(other.scale);
	}
	
	public Transform SUB(Transform other) {
		Vector pos = new Vector(position).sub(other.position);
		pos.rotate(-other.rotation);
		pos.div(other.scale);
		double rot = rotation - other.rotation;
		return new Transform(pos, rot, new Vector(scale).div(other.scale));
	}
	
	/**
	 * Adds a vector of angle <code>rotation</code> and magnitude <code>amount</code> to the position vector.
	 * 
	 * Visualize it as if the rotation determines what direction "forward" is, and the amount determines the amount to move forward.
	 * 
	 * @param amount the amount to move forward
	 */
	public void forward(double amount) {
		position.add(Vector.fromAngle(rotation, amount));
	}
	
	/**
	 * Creates a vector of angle <code>rotation</code> and magnitude <code>amount</code>, effectively making a vector pointed "forward" relative to this transformation.
	 * 
	 * Visualize it as if the rotation determines what direction "forward" is, and the amount determines the amount to move forward.
	 * 
	 * @param amount the amount to move forward
	 * @return a new vector in the forward direction (as defined by this Transform) with the given amount as magnitude
	 */
	public Vector FORWARD(double amount) {
		return Vector.fromAngle(rotation, amount);
	}
	
	/**
	 * Applies this transformation to the given vector
	 * 
	 * @param vector the vector to be transformed
	 */
	public void apply(Vector vector) {
		vector.add(position);
		vector.rotate(rotation);
		vector.mul(scale);
	}
	
	/**
	 * Applies this transformation to the given vector. Does not change the given vector.
	 * 
	 * @param vector the vector to use for calculation
	 * @return a new vector with this transformation applied
	 */
	public Vector APPLY(Vector vector) {
		Vector v = vector.clone();
		apply(v);
		return v;
	}
	
	@Override
	public Transform clone() {
		return new Transform(position.clone(), rotation, scale.clone());
	}
}
