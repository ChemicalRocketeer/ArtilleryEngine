package hellomisterme.artillery_engine.components.physics;

import hellomisterme.artillery_engine.components.IngameComponent;
import hellomisterme.util.Transform;
import hellomisterme.util.Vector2;

import java.awt.geom.Rectangle2D;

/**
 * A collision system
 * 
 * @since 11-22-12
 * @author David Aaron Suddjian
 */
public abstract class Collider extends IngameComponent {

	/**
	 * @return A bounding Rectangle set to the correct global pos, i.e. a Rectangle at its actual pos
	 */
	public abstract Rectangle2D getBounds();

	/**
	 * This is a generic collision detection method. Built in classes will implement this by calling one of the specific collision methods.
	 * 
	 * @param other
	 *        the object to check
	 * @param x1
	 *        on-screen x coordinate of this object
	 * @param y1
	 *        on-screen y coordinate of this object
	 * @param x2
	 *        on-screen x coordinate of the other object
	 * @param y2
	 *        on-screen y coordinate of the other object
	 * @return true if collision detected, else false
	 */
	public abstract boolean collides(Collider other);

	public abstract boolean collides(Box other);

	public abstract boolean collides(Circle other);

	public static class Box extends Collider {

		public Vector2 dimensions;

		public Box(double x, double y, double w, double h) {
			transform.position.x = x;
			transform.position.y = y;
			dimensions = new Vector2(w, h);
		}

		@Override
		public boolean collides(Collider other) {
			return other.collides(this);
		}

		@Override
		public boolean collides(Box other) {
			Vector2 pos = other.globalPosition();
			return collides(pos.x, pos.y, other.dimensions.x, other.dimensions.y);
		}

		public boolean collides(double x, double y, double w, double h) {
			if (dimensions.x < 0 || dimensions.y < 0) return false;
			Vector2 pos = globalPosition();
			double x0 = pos.x;
			double y0 = pos.y;
			return x + w > x0 && y + h > y0 && x < x0 + dimensions.x && y < y0 + dimensions.y;
		}

		@Override
		public boolean collides(Circle other) {
			return other.collides(this);
		}

		@Override
		public Rectangle2D getBounds() {
			return new Rectangle2D.Double(transform.position.x, transform.position.y, dimensions.x, dimensions.y);
		}
	}

	public static class Circle extends Collider {

		public double x, y, radius;

		public Circle(double x, double y, double r) {
			this.x = x;
			this.y = y;
			radius = r;
		}

		@Override
		public boolean collides(Collider other) {
			return other.collides(this);
		}

		@Override
		public boolean collides(Box other) {
			System.out.println("circle colliding with box");
			return false;
		}

		@Override
		public boolean collides(Circle other) {
			double xDist = other.x - x;
			double yDist = other.y - y;
			double totalRadius = other.radius + radius;
			return xDist * xDist + yDist * yDist <= totalRadius * totalRadius;
		}

		public boolean collides(double x, double y) {
			Vector2 pos = globalPosition();
			double xDist = x - pos.x;
			double yDist = y - pos.y;
			return xDist * xDist + yDist * yDist <= radius * radius;
		}

		@Override
		public Rectangle2D getBounds() {
			return new Rectangle2D.Double(x, y, radius * 2, radius * 2);
		}
	}

	public static class Line extends Collider {

		public Vector2 dimensions;

		public Line(Transform t, Vector2 dimensions) {
			this.transform = t;
			this.dimensions = dimensions;
		}

		public Line(double x, double y, double w, double h) {
			transform.position.x = x;
			transform.position.y = y;
			dimensions = new Vector2(w, h);
		}

		public Line(double x, double y, Vector2 dimensions) {
			transform.position.x = x;
			transform.position.y = y;
			this.dimensions = dimensions;
		}

		@Override
		public Rectangle2D getBounds() {
			return new Rectangle2D.Double(transform.position.x, transform.position.y, dimensions.x, dimensions.y);
		}

		@Override
		public boolean collides(Collider other) {
			return other.collides(this);
		}

		@Override
		public boolean collides(Box other) {
			return false;
		}

		@Override
		public boolean collides(Circle other) {
			Vector2 pos = globalPosition();
			if (dimensions.x == 0 && dimensions.y == 0) {
				return other.collides(pos.x, pos.y);
			}
			double x2 = dimensions.x + pos.x;
			double y2 = dimensions.y + pos.y;
			Vector2 pos2 = other.globalPosition();
			if (dimensions.x == 0) {
				// prevent divide by zero from horizontal/vertical lines, special case logic
				if (dimensions.y >= 0) { // height is positive
					if (pos2.y <= pos.y) {
						return other.collides(pos.x, pos.y);
					} else if (pos2.y >= y2) {

					}
				} else {
					if (pos2.y >= pos.y) {

					} else if (pos2.y <= y2) {

					}
				}
			} else if (dimensions.y == 0) {

			} else {
				double m = (pos.y - pos2.y) / (pos.x - pos2.x);
				double m2 = -1 / m;

			}
			return false;
		}
	}
}
