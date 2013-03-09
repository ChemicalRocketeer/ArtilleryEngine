package hellomisterme.artillery_engine.collision;

import java.awt.geom.Rectangle2D;

/**
 * A collision system
 * 
 * @since 11-22-12
 * @author David Aaron Suddjian
 */
public abstract class Collision {

	/**
	 * @return A bounding Rectangle set to the correct global pos, i.e. a Rectangle at its actual pos
	 */
	public abstract Rectangle2D getBounds();

	/**
	 * This is a generic collision detection method. Built in classes will implement this by calling one of the specific collision methods.
	 * 
	 * @param other the object to check
	 * @param x1 on-screen x coordinate of this object
	 * @param y1 on-screen y coordinate of this object
	 * @param x2 on-screen x coordinate of the other object
	 * @param y2 on-screen y coordinate of the other object
	 * @return true if collision detected, else false
	 */
	public abstract boolean collides(Collision other, double x1, double y1, double x2, double y2);

	public abstract boolean collides(Box other, double x1, double y1, double x2, double y2);

	public abstract boolean collides(Circle other, double x1, double y1, double x2, double y2);

	
	public static class Box extends Collision {

		Rectangle2D.Double rect;

		public Box(double x, double y, double w, double h) {
			rect = new Rectangle2D.Double(x, y, w, h);
		}

		@Override
		public boolean collides(Collision other, double x1, double y1, double x2, double y2) {
			return other.collides(this, x2, y2, x1, y1);
		}

		@Override
		public boolean collides(Box other, double x1, double y1, double x2, double y2) {
			System.out.println("box colliding with box");
			return false;
		}

		@Override
		public boolean collides(Circle other, double x1, double y1, double x2, double y2) {
			System.out.println("box colliding with circle");
			return false;
		}

		@Override
		public Rectangle2D getBounds() {
			return rect;
		}
	}
	
	
	public static class Circle extends Collision {

		private double x, y, radius;

		public Circle(double x,double y,double r) {
			this.x = x;
			this.y = y;
			radius = r;
		}

		@Override
		public boolean collides(Collision other, double x1, double y1, double x2, double y2) {
			return other.collides(this, x2, y2, x1, y1);
		}

		@Override
		public boolean collides(Box other, double x1, double y1, double x2, double y2) {
			System.out.println("circle colliding with box");
			return false;
		}

		@Override
		public boolean collides(Circle other, double x1, double y1, double x2, double y2) {
			System.out.println("circle colliding with circle");
			return false;
		}
		
		@Override
		public Rectangle2D getBounds() {
			return new Rectangle2D.Double(x, y, radius * 2, radius * 2);
		}
	}
}
