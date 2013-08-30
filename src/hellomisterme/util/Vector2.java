package hellomisterme.util;

import hellomisterme.artillery_engine.rendering.Render;

import java.awt.BasicStroke;
import java.awt.Color;

/**
 * Describes a vector that can be used for velocity or freeBody or whatever else.
 * 
 * This implementation uses a length along both the X and Y cartesian coordinates, but it can also be thought of using a
 * polar coordinate system, and there are methods which return and set polar coordinates. The only difference between this implementation and one that might use polar
 * coordinate data storage is a small difference in computation time for different methods.
 * 
 * All angle measurements are in radians.
 * 
 * @since 1-27-13
 * @author David Aaron Suddjian
 */
public class Vector2 {

	/**
	 * Useful constant to set vector angles
	 */
	public static final double LEFT = Math.PI, DOWN = Math.PI * 0.5, UP = -DOWN, RIGHT = 0.0;

	/**
	 * Useful constant to rotate vector angles. A positive rotation will occur in the clockwise direction. These values can be divided and multiplied for the desired effect.
	 */
	public static final double QUARTER_TURN = Math.PI * 0.5, EIGHTH_TURN = QUARTER_TURN * 0.5, THIRD_TURN = 1.5 * Math.PI, SIXTH_TURN = THIRD_TURN * 0.5;

	public double x, y;

	public Vector2() {
		this(0, 0);
	}

	/**
	 * Creates a Vector2 that has the given X and Y length.
	 * 
	 * @param x
	 * @param y
	 */
	public Vector2(double xLength, double yLength) {
		this.x = xLength;
		this.y = yLength;
	}

	/**
	 * Creates a new Vector2 using the given angle and length. If getLength() or getAngle() are called later, the returned numbers will be close, but not exactly the same as provided because of
	 * floating-point arithmetic. This method is not as fast as using a constructor.
	 * 
	 * The angle variable is assumed to be in radians, not degrees.
	 * 
	 * @param angle the angle, in radians
	 * @param magnitude the length of the vector
	 * @return a new Vector2 with the given properties
	 */
	public static Vector2 fromAngle(double angle, double magnitude) {
		return new Vector2(magnitude * Math.cos(angle), magnitude * Math.sin(angle));
	}

	/**
	 * Creates a vector running from point A to point B.
	 * 
	 * While Vector2 does not store coordinate variables, the returned vector will have the correct x and y to get from A to B. This method is not as fast as using a constructor.
	 * 
	 * @param xA the X value of point A
	 * @param yA the Y value of point A
	 * @param xB the X value of point B
	 * @param yB the Y value of point B
	 * @return A new Vector running from point A to point B
	 */
	public static Vector2 fromAtoB(double xA, double yA, double xB, double yB) {
		return new Vector2(xB - xA, yB - yA);
	}

	/**
	 * Returns a new Vector2 with the same values as this Vector2
	 */
	@Override
	public Vector2 clone() {
		return new Vector2(x, y);
	}

	/**
	 * Adds another Vector2 to this one
	 * 
	 * @param other the Vector2 to add
	 */
	public void add(Vector2 other) {
		x += other.x;
		y += other.y;
	}

	/**
	 * Adds another Vector2 to this one
	 * 
	 * @param other the Vector2 to add
	 */
	public Vector2 ADD(Vector2 other) {
		return new Vector2(x + other.x, y + other.y);
	}

	/**
	 * Subtracts another Vector2 from this one
	 * 
	 * @param other the Vector2 to subtract
	 */
	public void sub(Vector2 other) {
		x -= other.x;
		y -= other.y;
	}

	/**
	 * Subtracts another Vector2 from this one
	 * 
	 * @param other the Vector2 to subtract
	 */
	public Vector2 SUB(Vector2 other) {
		return new Vector2(x - other.x, y - other.y);
	}

	/**
	 * Multiplies this Vector2 by the given scalar
	 * 
	 * @param scalar the amount to scale (if 1 no change, if 2 magnitude is doubled, if 0 magnitude is 0)
	 */
	public void mul(double scalar) {
		x *= scalar;
		y *= scalar;
	}

	/**
	 * Multiplies this Vector2 by the given scalar
	 * 
	 * @param scalar the amount to scale (if 1 no change, if 2 magnitude is doubled, if 0 magnitude is 0)
	 */
	public Vector2 MUL(double scalar) {
		return new Vector2(x * scalar, y * scalar);
	}

	/**
	 * Divides this Vector2 by the given scalar
	 * 
	 * @param scalar the amount to scale (if 1 no change, if 2 magnitude is halved, if 0 x and y are set to infinity)
	 */
	public void div(double scalar) {
		if (scalar == 0) {
			if (x >= 0) {
				x = Double.POSITIVE_INFINITY;
			} else {
				x = Double.NEGATIVE_INFINITY;
			}
			if (y >= 0) {
				y = Double.POSITIVE_INFINITY;
			} else {
				y = Double.NEGATIVE_INFINITY;
			}
		} else {
			x /= scalar;
			y /= scalar;
		}
	}

	/**
	 * Divides this Vector2 by the given scalar
	 * 
	 * @param scalar the amount to scale (if 1 no change, if 2 magnitude is halved, if 0 x and y are set to infinity)
	 */
	public Vector2 DIV(double scalar) {
		if (scalar == 0) {
			// test condition ? true result : false result
			return new Vector2(x >= 0 ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY, y >= 0 ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY);
		}
		return new Vector2(x / scalar, y / scalar);
	}

	/**
	 * Multiplies this Vector2 by the given scalar
	 * 
	 * @param scalar the amount to scale (if 1 no change, if 2 magnitude is doubled, if 0 magnitude is 0)
	 */
	public void mul(Vector2 other) {
		x *= other.x;
		y *= other.y;
	}

	/**
	 * Multiplies this Vector2 by the given scalar
	 * 
	 * @param scalar the amount to scale (if 1 no change, if 2 magnitude is doubled, if 0 magnitude is 0)
	 */
	public Vector2 MUL(Vector2 other) {
		return new Vector2(x * other.x, y * other.y);
	}

	/**
	 * Divides this Vector2 by the given scalar
	 * 
	 * @param other the amount to scale (if 1 no change, if 2 magnitude is halved, if 0 magnitude is infinity)
	 */
	public void div(Vector2 other) {
		if (other.x == 0) {
			if (x >= 0) {
				x = Double.POSITIVE_INFINITY;
			} else {
				x = Double.NEGATIVE_INFINITY;
			}
		} else {
			x /= other.x;
		}

		if (other.y == 0) {
			if (y >= 0) {
				y = Double.POSITIVE_INFINITY;
			} else {
				y = Double.NEGATIVE_INFINITY;
			}
		} else {
			y /= other.y;
		}
	}

	/**
	 * Divides this Vector2 by the given scalar
	 * 
	 * @param scalar the amount to scale (if 1 no change, if 2 magnitude is halved, if 0 magnitude is infinity)
	 */
	public Vector2 DIV(Vector2 other) {
		double newX;
		double newY;
		if (other.x == 0) {
			if (x >= 0) {
				newX = Double.POSITIVE_INFINITY;
			} else {
				newX = Double.NEGATIVE_INFINITY;
			}
		} else {
			newX = x / other.x;
		}

		if (other.y == 0) {
			if (y >= 0) {
				newY = Double.POSITIVE_INFINITY;
			} else {
				newY = Double.NEGATIVE_INFINITY;
			}
		} else {
			newY = y / other.y;
		}
		return new Vector2(newX, newY);
	}

	/**
	 * @param a the first vector to use to get the dot product
	 * @param b the first vector to use to get the dot product
	 * @return the dot product of two vectors
	 */
	public static double dot(Vector2 a, Vector2 b) {
		return a.x * b.x + a.y * b.y;
	}

	/**
	 * Projects this vector along the given other vector
	 * 
	 * @param other the vector to project along
	 */
	public void project(Vector2 other) {
		double dot = dot(this, other) / dot(other, other);
		x = dot * other.x;
		y = dot * other.y;
	}

	/**
	 * @param a the vector to project
	 * @param b the vector to be projected onto
	 * @return a projection of vector a onto vector b
	 */
	public static Vector2 projection(Vector2 a, Vector2 b) {
		double dot = dot(a, b) / dot(b, b);
		return new Vector2(dot * b.x, dot * b.y);
	}

	public Vector2 rightNormal() {
		return new Vector2(-y, x);
	}

	public Vector2 leftNormal() {
		return new Vector2(y, -x);
	}

	public void normalize() {
		div(mag());
	}

	/**
	 * Sets the length, or magnitude, of this Vector2 to the given amount. If the current magnitude is zero, sets x to the value of m.
	 * 
	 * @param m the new magnitude
	 */
	public void setMagnitude(double m) {
		double mag = mag();
		if (mag == 0.0) {
			x = m; // there is no current angle, so just set x to the m length
		} else {
			mul(m / mag);
		}
	}

	/**
	 * Sets the angle of this Vector2 to the given angle in radians. This angle will only be used to set the underlying data of the vector and will not itself be stored in memory, so a call to the
	 * getAngle() method will return an equivalent angle, but not necessarily the same number given here. For example, after a call to setAngle(3*pi), getAngle() will return pi.
	 * 
	 * @param angle the new angle, in radians
	 */
	public void setAngle(double angle) {
		// magnitude can be thought of as the radius, so to get x and y to the right lengths, you just multiply the sin and cos by m
		double m = mag();
		x = m * Math.cos(angle);
		y = m * Math.sin(angle);
	}

	/**
	 * @return the angle of this Vector2 in radians
	 */
	public double angle() {
		return Math.atan2(y, x);
	}

	/**
	 * @return the magnitude of this Vector2
	 */
	public double mag() {
		// pythagorean theorem at work, bitches!
		return Math.sqrt(mag2());
	}

	/**
	 * @return the magnitude squared of this Vector2. This method is faster than mag() because it doesn't perform a sqrt operation.
	 */
	public double mag2() {
		return x * x + y * y;
	}

	/**
	 * Rotates this Vector2 around its origin by the given amount.
	 * 
	 * The angle should be given in radians. It is important to note that if the coordinate system has y increasing from
	 * top to bottom as is typical in Java, then the effect will be a clockwise rotation. In standard maths, however, the rotation is counter-clockwise.
	 * 
	 * @param amount the amount to rotate
	 */
	public void rotate(double amount) {
		setAngle(amount + angle());
	}

	/**
	 * Returns true if the values of the given vector equal those of this one. If the two vectors are not literally the same object, this is unlikely to happen because of floating point arithmetic.
	 * Use approximatelyEquals to detect when 2 vectors are about the same.
	 * 
	 * @param other the vector to compare with this one
	 * @return whether the x and y variables of the two vectors are equal
	 */
	public boolean equals(Vector2 other) {
		return this.x == other.x && this.y == other.y;
	}

	/**
	 * Returns whether this vector is approximately equal to another using integer arithmetic instead of double precision.
	 * 
	 * @see #approximatelyEquals(Vector2, double)
	 * @param other the vector to test
	 * @return true if the given Vector2 can be considered "equal" to this one
	 */
	public boolean approximatelyEquals(Vector2 other) {
		return (int) this.x == (int) other.x && (int) this.y == (int) other.y;
	}

	/**
	 * Returns true if the given vector's values are within plus or minus the precision value, else returns false.
	 * 
	 * @param other the vector to test
	 * @param precision how close equal is (in both the positive and negative direction)
	 * @return true if the vectors are close enough to be considered equal
	 */
	public boolean approximatelyEquals(Vector2 other, double precision) {
		return x >= other.x - precision && x <= other.x + precision && y >= other.y - precision && y <= other.y + precision;
	}

	/**
	 * Visualizes this Vector2 on the given Graphics2D object, represented as a line with a red dot at the head. The exaggeration is a scalar multiplier that does not affect the data of this vector,
	 * only the way it is displayed. For a one-to-one representation of the vector length, use an exaggeration of 1.0
	 * 
	 * @param g the Graphics2D object to draw to
	 * @param exaggeration the amount to exaggerate (the length will be multiplied by this amount)
	 * @param xPos the x coordinate of the vector origin
	 * @param yPos the y coordinate of the vector origin
	 */
	public void draw(Render r, double exaggeration, int xPos, int yPos) {
		int endX = (int) (x * exaggeration) + xPos;
		int endY = (int) (y * exaggeration) + yPos;
		int dotX = (int) (x * 0.9 * exaggeration) + xPos;
		int dotY = (int) (y * 0.9 * exaggeration) + yPos;
		r.graphics.setColor(Color.BLACK);
		r.graphics.setStroke(new BasicStroke(1f));
		r.graphics.drawLine(xPos, yPos, endX, endY);
		r.graphics.setColor(Color.RED);
		r.graphics.setStroke(new BasicStroke(1.5f));
		r.graphics.drawLine(endX, endY, dotX, dotY);
	}

	@Override
	public String toString() {
		return "x: " + x + " y: " + y;
	}

}
