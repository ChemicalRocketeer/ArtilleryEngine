package hellomisterme.artillery_engine.geometry;

import hellomisterme.artillery_engine.components.IngameComponent;
import hellomisterme.artillery_engine.components.physics.CollisionResult;
import hellomisterme.artillery_engine.rendering.Render;
import hellomisterme.artillery_engine.rendering.Renderable;
import hellomisterme.artillery_engine.util.MathUtils;
import hellomisterme.artillery_engine.util.Vector;

import java.awt.Color;
import java.awt.Graphics2D;

public class Circle extends IngameComponent implements Hitbox, Renderable {
	
	public static Color renderingColor = Color.CYAN;
	
	private double radius;
	
	public Circle(double radius) {
		this.radius = Math.abs(radius);
	}
	
	/**
	 * @return a new Circle with the given area
	 */
	public static Circle fromArea(double area) {
		return new Circle(Math.sqrt(area / Math.PI));
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
	
	@Override
	public CollisionResult getCollisionResult(Circle other) {
		CollisionResult result = new CollisionResult();
		result.correction = other.globalPosition();
		result.correction.sub(globalPosition());
		double totalRadius = other.radius + radius;
		double distance = result.correction.mag();
		result.correction.setMagnitude(distance - totalRadius, distance);
		result.collision = distance < totalRadius;
		return result;
	}
	
	@Override
	public boolean collides(Circle other) {
		Vector data = other.globalPosition().SUB(globalPosition());
		double totalRadius = other.radius + radius;
		double distance = data.mag2();
		return distance < totalRadius * totalRadius;
	}
	
	@Override
	public AABB getAABB() {
		double diameter = radius * 2;
		Vector pos = globalPosition();
		return new AABB(new Vector(pos.x - radius, pos.y - radius), diameter, diameter);
	}

	public void draw(Graphics2D g, Vector pos) {
		Color c = g.getColor();
		g.setColor(renderingColor);
		double x = radius;
		double y = radius;
		g.translate(pos.x - x, pos.y - y);
		g.drawOval(0, 0, MathUtils.round(x * 2), MathUtils.round(y * 2));
		g.setColor(c);
	}

	@Override
	public void render(Render render) {
		Graphics2D g = render.getCameraGraphics();
		draw(g, entity.globalPosition());
		g.dispose();
	}

	@Override
	public void devmodeRender(Render render) {
	}
}
