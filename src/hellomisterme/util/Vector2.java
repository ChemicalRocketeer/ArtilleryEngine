package hellomisterme.util;

import hellomisterme.artillery_engine.Err;
import hellomisterme.artillery_engine.graphics.Render;
import hellomisterme.artillery_engine.io.Savable;

import java.awt.BasicStroke;
import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Describes a vector that can be used for velocity or forces or whatever else.
 * 
 * This implementation uses a length along both the X and Y cartesian coordinates, but it can also be thought of using a
 * polar coordinate system, and there are methods which return and set polar coordinates. As far as the programmer is concerned, the only difference between this implementation and one that uses polar
 * coordinate data storage is a small difference in computation time for different methods.
 * 
 * All angle measurements are in radians.
 * 
 * @since 1-27-13
 * @author David Aaron Suddjian
 */
public class Vector2 implements Savable {

	/**
	 * Useful constant to set vector angles
	 */
	public static final double LEFT = Math.PI, DOWN = Math.PI / 2.0, UP = -DOWN, RIGHT = 0.0;

	/**
	 * Useful constants to rotate vector angles. A positive rotation will occur in the clockwise direction. These values can be divided and multiplied for the desired effect.
	 */
	public static final double QUARTER_TURN = Math.PI / 2, EIGHTH_TURN = QUARTER_TURN / 2.0, THIRD_TURN = 2.0 * Math.PI / 3.0, SIXTH_TURN = THIRD_TURN / 2.0;

	public double x, y;

	/**
	 * Creates a Vector2D that has the given length along the X and Y cartesian coordinates.
	 * 
	 * @param x
	 * @param y
	 */
	public Vector2(double xLength, double yLength) {
		this.x = xLength;
		this.y = yLength;
	}

	/**
	 * Creates a new Vector2D using the given angle and length. If getLength() or getAngle() are called later, the returned numbers will be close, but not exactly the same as provided because of
	 * floating-point arithmetic. This method is not as fast as using a constructor.
	 * 
	 * The angle variable is assumed to be in radians, not degrees.
	 * 
	 * @param angle the angle, in radians
	 * @param magnitude the length of the vector
	 * @return a new Vector2D with the given properties
	 */
	public static Vector2 fromAngle(double angle, double magnitude) {
		return new Vector2(magnitude * Math.cos(angle), magnitude * Math.sin(angle));
	}

	/**
	 * Creates a vector running from point A to point B.
	 * 
	 * While Vector2D does not store coordinate variables, the returned vector will have the correct x and y to get from A to B. This method is not as fast as using a constructor.
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
	 * Returns a new Vector2D with the same values as this Vector2D
	 */
	@Override
	public Vector2 clone() {
		return new Vector2(x, y);
	}

	/**
	 * Adds another Vector2D to this one
	 * 
	 * @param other the Vector2D to add
	 */
	public void add(Vector2 other) {
		x += other.x;
		y += other.y;
	}

	/**
	 * Adds another Vector2D to this one
	 * 
	 * @param other the Vector2D to add
	 */
	public Vector2 ADD(Vector2 other) {
		return new Vector2(x + other.x, y + other.y);
	}

	/**
	 * Subtracts another Vector2D from this one
	 * 
	 * @param other the Vector2D to subtract
	 */
	public void sub(Vector2 other) {
		x -= other.x;
		y -= other.y;
	}

	/**
	 * Subtracts another Vector2D from this one
	 * 
	 * @param other the Vector2D to subtract
	 */
	public Vector2 SUB(Vector2 other) {
		return new Vector2(x - other.x, y - other.y);
	}

	/**
	 * Multiplies this Vector2D by the given scalar
	 * 
	 * @param scalar the amount to scale (if 1 no change, if 2 magnitude is doubled, if 0 magnitude is 0)
	 */
	public void mul(double scalar) {
		x *= scalar;
		y *= scalar;
	}

	/**
	 * Multiplies this Vector2D by the given scalar
	 * 
	 * @param scalar the amount to scale (if 1 no change, if 2 magnitude is doubled, if 0 magnitude is 0)
	 */
	public Vector2 MUL(double scalar) {
		return new Vector2(x * scalar, y * scalar);
	}

	/**
	 * Divides this Vector2D by the given scalar
	 * 
	 * @param scalar the amount to scale (if 1 no change, if 2 magnitude is halved, if 0 no change)
	 */
	public void div(double scalar) {
		if (scalar == 0) return;
		x /= scalar;
		y /= scalar;
	}

	/**
	 * Divides this Vector2D by the given scalar
	 * 
	 * @param scalar the amount to scale (if 1 no change, if 2 magnitude is halved, if 0 returns this)
	 */
	public Vector2 DIV(double scalar) {
		if (scalar == 0) return clone();
		return new Vector2(x / scalar, y / scalar);
	}

	public void norm() {
		div(mag());
	}

	/**
	 * Sets the length, or magnitude, of this Vector2D to the given amount.If the current magnitude is zero, does nothing.
	 * 
	 * @param m the new magnitude
	 */
	public void setMagnitude(double m) {
		double mag = mag();
		if (mag != 0.0) {
			mul(m / mag);
		}
	}

	/**
	 * Sets the angle of this Vector2D to the given angle in radians. This angle will only be used to set the underlying data of the vector and will not itself be stored in memory, so a call to the
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
	 * @return the angle of this Vector2D in radians
	 */
	public double angle() {
		// acos will only ever return angles between pi and 0, so angles greater than pi have to be manually adjusted. Fortunately, we know whether y is positive or negative, so that is easy.
		if (y >= 0) {
			return Math.atan(y / x);
		}
		return Math.PI * 2 - Math.atan(y / x);
	}

	/**
	 * @return the magnitude of this Vector2D
	 */
	public double mag() {
		// pythagorean theorem at work, bitches!
		return Math.sqrt(mag2());
	}

	/**
	 * @return the magnitude squared of this Vector2D. This method is faster than mag() because it doesn't perform a sqrt operation.
	 */
	public double mag2() {
		return x * x + y * y;
	}

	/**
	 * Rotates this Vector2D around its origin by the given amount.
	 * 
	 * The angle should be given in radians.The method will still work if it isn't, but you won't get the intended result. It is important to note that if your coordinate system has y increasing from
	 * top to bottom, then the effect will be a clockwise rotation. Trigonometrically, however, the rotation is counter-clockwise.
	 * 
	 * @param amount the amount to rotate
	 */
	public void rotate(double amount) {
		double m = mag();
		amount += angle();
		x = m * Math.cos(amount);
		y = m * Math.sin(amount);
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
	 * @return true if the given Vector2D can be considered "equal" to this one
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
	 * Visualizes this Vector2D on the given Graphics2D object, represented as a line with a red dot at the head. The exaggeration is a scalar multiplier that does not affect the data of this vector,
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
	public void write(DataOutputStream out) {
		try {
			out.writeDouble(x);
			out.writeDouble(y);
		} catch (IOException e) {
			Err.error("Can't write Vector2 data!");
			e.printStackTrace();
		}
	}

	@Override
	public void read(DataInputStream in) {
		try {
			x = in.readDouble();
			y = in.readDouble();
		} catch (IOException e) {
			Err.error("Can't read Vector2 data!");
			e.printStackTrace();
		}
	}

	@Override
	public void writeStatic(DataOutputStream out) {
	}

	@Override
	public void readStatic(DataInputStream in) {
	}
}
