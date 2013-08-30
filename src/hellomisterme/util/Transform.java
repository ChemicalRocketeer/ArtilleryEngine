package hellomisterme.util;


public class Transform {
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
		scale.mul(other.scale);
	}

	public Transform ADD(Transform other) {
		return new Transform(position.ADD(other.position), rotation + other.rotation, scale.MUL(other.scale));
	}

	public void sub(Transform other) {
		position.sub(other.position);
		rotation -= other.rotation;
		scale.div(other.scale);
	}

	public Transform SUB(Transform other) {
		return new Transform(position.SUB(other.position), rotation - other.rotation, scale.DIV(other.scale));
	}

	/**
	 * Adds a vector of angle <code>rotation</code> and magnitude <code>amount</code> to the position vector.
	 * 
	 * Visualize it as if the rotation determines what direction "forward" is, and the amount determines the amount to move forward.
	 * 
	 * @param amount the amount to move forward
	 */
	public void forward(double amount) {
		position.add(Vector2.fromAngle(rotation, amount));
	}

	/**
	 * Creates a vector of angle <code>rotation</code> and magnitude <code>amount</code>, effectively making a vector pointed "forward" relative to this transformation.
	 * 
	 * Visualize it as if the rotation determines what direction "forward" is, and the amount determines the amount to move forward.
	 * 
	 * @param amount the amount to move forward
	 * @return a new vector in the forward direction (as defined by this Transform) with the given amount as magnitude
	 */
	public Vector2 FORWARD(double amount) {
		return Vector2.fromAngle(rotation, amount);
	}

	/**
	 * Applies this transformation to the given vector in scale, rotation, position order
	 * 
	 * @param vector the vector to be transformed
	 */
	public void apply(Vector2 vector) {
		vector.mul(scale);
		vector.rotate(rotation);
		vector.add(position);
	}

	/**
	 * Calculates the result of the given vector with this transformation applied in scale, rotation, position order. Does not change the given vector.
	 * 
	 * @param vector the vector to use for calculation
	 * @return a new vector with this transformation applied
	 */
	public Vector2 APPLY(Vector2 vector) {
		Vector2 vect = vector.MUL(scale);
		vect.rotate(rotation);
		vect.add(position);
		return vect;
	}

	@Override
	public Transform clone() {
		return new Transform(position.clone(), rotation, scale.clone());
	}
}
