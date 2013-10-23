package hellomisterme.artillery_engine.geometry;

import hellomisterme.artillery_engine.util.Vector;

import java.awt.Graphics2D;

/**
 * An axis-aligned bounding box
 * 
 * @author David Aaron Suddjian
 */
public class AABB {

	public double left, right, top, bottom;

	public AABB(AABB other) {
		left = other.left;
		right = other.right;
		top = other.top;
		bottom = other.bottom;
	}

	public AABB(Vector position, double width, double height) {
		left = position.x;
		right = left + width;
		top = position.y;
		bottom = top + height;
	}

	public AABB(double left, double right, double top, double bottom) {
		this.left = left;
		this.right = right;
		this.top = top;
		this.bottom = bottom;
	}

	public boolean intersects(AABB other) {
		return right > other.left && left < other.right && top < other.bottom && bottom > other.top;
	}
	
	public void render(Graphics2D g) {
		g.drawRect((int) left, (int) top, (int) right - (int) left, (int) bottom - (int) top);
	}
}
