package hellomisterme.artillery_engine.util;

import hellomisterme.artillery_engine.rendering.Render;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Describes a vector that can be used for velocity or freeBody or whatever else.
 * 
 * This implementation uses a length along both the X and Y cartesian coordinates, but it can also be thought of using a
 * polar coordinate system, and there are methods which return and set polar coordinates. The only difference between this implementation and one that might use polar
 * coordinate data storage is a small difference in computation time for different methods.
 * 
 * All methods return a Vector if appropriate. This allows for easy calculation chaining, e.g. vect.Add(vect2).Mul(5).div(vect2.mag());
 * Methods that modify a Vector object apply the required operation to the current Vector and then return the current Vector.
 * 
 * All angle measurements are in radians.
 * 
 * @since 1-27-13
 * @author David Aaron Suddjian
 */
public class Vector {

	/**
	 * Useful constant to set vector angles
	 */
	public static final double LEFT = Math.PI, DOWN = Math.PI / 2, UP = -DOWN, RIGHT = 0.0;

	/** Useful constant to rotate vector angles. A positive rotation will occur in the clockwise direction. These values can be divided and multiplied for the desired effect. */
	public static final double QUARTER_TURN = Math.PI / 2, EIGHTH_TURN = QUARTER_TURN / 2, THIRD_TURN = 1.5 * Math.PI, SIXTH_TURN = THIRD_TURN * 0.5;

	public double x, y;

	public Vector() {
		this(0, 0);
	}

	/**
	 * Creates a Vector with the given X and Y length.
	 */
	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Vector(Vector copy) {
		x = copy.x;
		y = copy.y;
	}

	/**
	 * Creates a new Vector using the given angle and length. If getLength() or getAngle() are called later, the returned numbers will be close, but not exactly the same as provided because of
	 * floating-point arithmetic. This method is not as fast as using a constructor.
	 * 
	 * The angle variable is assumed to be in radians, not degrees.
	 * 
	 * @param angle the angle, in radians
	 * @param magnitude the length of the vector
	 * @return a new Vector with the given properties
	 */
	public static Vector fromAngle(double angle, double magnitude) {
		return new Vector(magnitude * Math.cos(angle), magnitude * Math.sin(angle));
	}

	/**
	 * @return the angle of this Vector in radians
	 */
	public double angle() {
		return Math.atan2(y, x);
	}

	/**
	 * @return the magnitude of this Vector
	 */
	public double magnitude() {
		return Math.hypot(x, y);
	}

	/**
	 * @return the magnitude squared of this Vector. This method is faster than mag() because it doesn't perform a sqrt operation.
	 */
	public double mag2() {
		return x * x + y * y;
	}

	/**
	 * @param a the first vector to use to get the dot product
	 * @param b the second vector to use to get the dot product
	 * @return the dot product of two vectors
	 */
	public static double dot(Vector a, Vector b) {
		return a.x * b.x + a.y * b.y;
	}

	/**
	 * Adds another Vector to this one
	 * 
	 * @param other the Vector to add
	 */
	public Vector add(Vector other) {
		x += other.x;
		y += other.y;
		return this;
	}

	/**
	 * Subtracts another Vector from this one
	 * 
	 * @param other the Vector to subtract
	 */
	public Vector sub(Vector other) {
		x -= other.x;
		y -= other.y;
		return this;
	}

	/**
	 * Multiplies this Vector by the given scalar
	 * 
	 * @param scalar the amount to scale (if 1 no change, if 2 magnitude is doubled, if 0 magnitude is 0)
	 */
	public Vector mul(double scalar) {
		x *= scalar;
		y *= scalar;
		return this;
	}

	/**
	 * Divides this Vector by the given scalar
	 * 
	 * @param scalar the amount to scale (if 1 no change, if 2 magnitude is halved, if 0 x and y are set to infinity)
	 */
	public Vector div(double scalar) {
		x /= scalar;
		y /= scalar;
		return this;
	}

	/**
	 * Multiplies this Vector by the given scalar
	 * 
	 * @param scalar the amount to scale (if 1 no change, if 2 magnitude is doubled, if 0 magnitude is 0)
	 */
	public Vector mul(Vector other) {
		x *= other.x;
		y *= other.y;
		return this;
	}

	/**
	 * Divides this Vector by the given scalar
	 * 
	 * @param other the amount to scale (if 1 no change, if 2 magnitude is halved, if 0 magnitude is infinity)
	 */
	public Vector div(Vector other) {
		x /= other.x;
		y /= other.y;
		return this;
	}

	/**
	 * Projects this vector along the given other vector
	 * 
	 * @param other the vector to project along
	 */
	public Vector project(Vector other) {
		double dot = dot(this, other) / dot(other, other);
		x = dot * other.x;
		y = dot * other.y;
		return this;
	}

	/**
	 * Sets the length, or magnitude, of this Vector to the given amount. If the current magnitude is zero, sets x to the value of m.
	 * 
	 * @param m the new magnitude
	 */
	public Vector setMagnitude(double m) {
		return setMagnitude(m, magnitude());
	}

	/**
	 * If you already know the current magnitude of this Vector, you can call this method which should run faster.
	 * 
	 * @param mag the new magnitude
	 * @param currentMag the current magnitude
	 */
	public Vector setMagnitude(double mag, double currentMag) {
		if (currentMag == 0.0)
			x = mag;
		else
			mul(mag / currentMag);
		return this;
	}

	/**
	 * Sets the angle of this Vector to the given angle in radians. This angle will only be used to set the underlying data of the vector and will not itself be stored in memory, so a call to the
	 * getAngle() method will return an equivalent angle, but not necessarily the same number given here. For example, after a call to setAngle(3*pi), getAngle() will return pi.
	 * 
	 * @param angle the new angle, in radians
	 */
	public Vector setAngle(double angle) {
		double m = magnitude();
		x = m * Math.cos(angle);
		y = m * Math.sin(angle);
		return this;
	}

	/**
	 * Rotates this Vector around its origin by the given amount.
	 * 
	 * The angle should be given in radians. It is important to note that if the coordinate system has y increasing from
	 * top to bottom as is typical in Java, then the effect will be a clockwise rotation. In standard maths, however, the rotation is counter-clockwise.
	 * 
	 * @param amount the amount to rotate
	 */
	public Vector rotate(double amount) {
		setAngle(amount + angle());
		return this;
	}

	public Vector rightNormal() {
		y = -y;
		return this;
	}

	public Vector leftNormal() {
		x = -x;
		return this;
	}

	/** Sets this Vector's magnitude to 1. If the current magnitude is 0, sets x to 1 */
	public Vector normalize() {
		if (x == 0 && y == 0) {
			x = 1;
		} else {
			div(magnitude());
		}
		return this;
	}

	public Vector negate() {
		x = -x;
		y = -y;
		return this;
	}

	/**
	 * Returns a new Vector with the same values as this Vector
	 */
	@Override
	public Vector clone() {
		return new Vector(x, y);
	}

	/**
	 * Returns true if the values of the given vector equal those of this one. If the two vectors are not literally the same object, this is unlikely to happen because of floating point arithmetic.
	 * Use approximatelyEquals to detect when 2 vectors are about the same.
	 * 
	 * @param other the vector to compare with this one
	 * @return whether the x and y variables of the two vectors are equal
	 */
	public boolean equals(Vector other) {
		return this.x == other.x && this.y == other.y;
	}

	/**
	 * Returns whether this vector is approximately equal to another using integer arithmetic instead of double precision.
	 * 
	 * @see #approximatelyEquals(Vector, double)
	 * @param other the vector to test
	 * @return true if the given Vector can be considered "equal" to this one
	 */
	public boolean approximatelyEquals(Vector other) {
		return MathUtils.round(this.x) == MathUtils.round(other.x) && MathUtils.round(this.y) == MathUtils.round(other.y);
	}

	/**
	 * Returns true if the given vector's values are within plus or minus the precision value, else returns false.
	 * 
	 * @param other the vector to test
	 * @param precision how close equal is (in both the positive and negative direction)
	 * @return true if the vectors are close enough to be considered equal
	 */
	public boolean approximatelyEquals(Vector other, double precision) {
		return x >= other.x - precision && x <= other.x + precision && y >= other.y - precision && y <= other.y + precision;
	}

	/**
	 * Visualizes this Vector on the given Graphics2D object, represented as an arrow. The exaggeration is a scalar multiplier that does not affect the data of this vector,
	 * only the way it is displayed. For a one-to-one representation of the vector length, use an exaggeration of 1.0
	 * 
	 * @param g the Graphics2D object to draw to
	 * @param exaggeration the amount to exaggerate (the length will be multiplied by this amount)
	 * @param Pos the in-world location of this vector
	 */
	public void draw(Render render, double exaggeration, Vector pos) {
		Vector exag = new Vector(this).mul(exaggeration);
		render.drawArrow(pos, exag.add(pos), Color.MAGENTA);
	}

	public void render(Graphics2D g, Vector pos) {
		// go to pos
		g.translate(pos.x, pos.y);
		g.drawLine(0, 0, MathUtils.round(x), MathUtils.round(y));
		// go to end of arrow
		g.translate(x, y);
		Vector tip = new Vector(-x, -y);
		tip.setMagnitude(5);
		tip.rotate(0.5); // ~30 degrees
		g.drawLine(0, 0, MathUtils.round(tip.x), MathUtils.round(tip.y));
		tip.rotate(-1); // ~-60 degrees
		g.drawLine(0, 0, MathUtils.round(tip.x), MathUtils.round(tip.y));
	}

	@Override
	public String toString() {
		return "x: " + x + " y: " + y;
	}
}
