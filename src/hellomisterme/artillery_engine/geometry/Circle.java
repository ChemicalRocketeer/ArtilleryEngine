package hellomisterme.artillery_engine.geometry;

import hellomisterme.util.Vector;

import java.awt.Graphics2D;

public class Circle {
	
	public Vector position;
	private double radius;
	
	//
	public Circle(double radius, Vector pos) {
		this.radius = Math.abs(radius);
		this.position = pos;
	}
	
	public double getRadius() {
		return radius;
	}
	
	public double getCircumference() {
		return 2 * Math.PI * radius;
	}
	
	public double getArea() {
		return Math.PI * radius * radius;
	}
	
	/**
	 * Calculates whether a collision occurs with another Circle, and the resulting data of the collision.
	 * The <i>collisionData</i> vector should be null if you only want the boolean and will not use the other results,
	 * because the method will be more efficient.
	 * 
	 * @param other the Circle to check for a collision with
	 * @param collisionData a vector that represents the minimum distance
	 *        to move this Circle so that it exactly touches the edge of <i>other</i>
	 * @return whether this Circle is colliding with <i>other</i>
	 */
	public boolean collision(Circle other, Vector collisionData) {
		Vector data = other.position.SUB(position);
		double totalRadius = other.radius + radius;
		double distance = data.mag();
		if (collisionData != null) {
			data.setMagnitude(distance - totalRadius, distance);
			collisionData.x = data.x;
			collisionData.y = data.y;
		}
		return distance <= totalRadius;
	}
	
	public void render(Graphics2D g) {
		g.translate(position.x - radius, position.y - radius);
		g.drawOval(0, 0, (int) radius * 2, (int) radius * 2);
	}
}
