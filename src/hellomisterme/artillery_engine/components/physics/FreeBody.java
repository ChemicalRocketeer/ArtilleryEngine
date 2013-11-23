package hellomisterme.artillery_engine.components.physics;

import hellomisterme.artillery_engine.Tick;
import hellomisterme.artillery_engine.components.Component;
import hellomisterme.artillery_engine.rendering.Render;
import hellomisterme.artillery_engine.rendering.Renderable;
import hellomisterme.artillery_engine.util.MathUtils;
import hellomisterme.artillery_engine.util.Vector;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * FreeBody allows an Entity to react to physics, with forces acting on it realistically. It also provides various flags for how the Entity should behave.
 * 
 * @since 10-18-12
 * @author David Aaron Suddjian
 */
public class FreeBody extends Component implements Tick, Renderable {
	
	// public Vector acceleration = new Vector(0, 0);
	public Vector velocity = new Vector(0, 0);
	public double mass = 1.0;
	public double spin = 0.0;
	
	public boolean collisionOn = true;
	public boolean pullsWithGravity = true;
	public boolean pulledByGravity = true;
	
	public static FreeBody create(Vector velocity, double mass, double spin) {
		FreeBody body = new FreeBody();
		body.velocity = velocity;
		body.mass = mass;
		body.spin = spin;
		return body;
	}
	
	public static FreeBody create(Vector velocity, double mass, double spin, boolean pullsWithGravity, boolean pulledByGravity) {
		FreeBody body = new FreeBody();
		body.velocity = velocity;
		body.mass = mass;
		body.spin = spin;
		body.pullsWithGravity = pullsWithGravity;
		body.pulledByGravity = pulledByGravity;
		return body;
	}
	
	@Override
	public void tick() {
		entity.transform.position.add(velocity);
		entity.transform.rotation += spin;
	}
	
	public void applyForce(Vector force) {
		if (mass != 0.0) velocity.add(force.DIV(mass));
	}
	
	/**
	 * Applies a force applied at a non-center position
	 * 
	 * @param force
	 * @param position the location (relative to entity) of the application of force
	 */
	public void applyForce(Vector force, Vector position) {
		if (mass != 0.0) {
			Vector f = Vector.projection(force, position);
			double t = force.SUB(f).mag();
			velocity.add(f.DIV(mass));
			spin += t * position.mag();
		}
	}
	
	public Vector getMomentum() {
		return velocity.MUL(mass);
	}
	
	/**
	 * @return the momentum (including angular momentum) at a given point from the center
	 */
	public Vector getMomentumAt(Vector point) {
		double dist = point.mag();
		Vector momentum = point.rightNormal();
		momentum.setMagnitude(spin * dist);
		momentum.add(getMomentum());
		return momentum;
	}
	
	/**
	 * Calculates the gravitational force from a given FreeBody.
	 * 
	 * @param body1 the FreeBody to gravitate towards
	 */
	public Vector calculateGravity(FreeBody body) {
		Vector gravity = new Vector(0, 0);
		if (body.mass != 0.0) {
			// This is just the distance formula without the square root, because to apply gravity you have to square the distance, and that cancels out the sqare root operation
			Vector pos = entity.globalPosition();
			Vector otherpos = body.entity.globalPosition();
			double xDist = otherpos.x - pos.x;
			double yDist = otherpos.y - pos.y;
			gravity.x = xDist;
			gravity.y = yDist;
			// law of gravitation: F = (m1 * m2) / (distance * distance) normally you would also use the Gravitational constant but that doesn't matter here because units are arbitrary
			gravity.setMagnitude(mass * body.mass / (xDist * xDist + yDist * yDist));
		}
		return gravity;
	}
	
	public boolean isMutuallyExclusive() {
		return true;
	}
	
	@Override
	public void render(Render render) {
	}
	
	@Override
	public void devmodeRender(Render render) {
		Graphics2D g = render.getCameraGraphics();
		g.setColor(Color.magenta);
		Vector momentum = getMomentum();
		Vector pos = entity.globalPosition();
		momentum.MUL(8).render(g, pos);
		render.alignGraphicsToCamera(g);
		g.setColor(Color.WHITE);
		int roundedX = MathUtils.round(pos.x);
		int roundedY = MathUtils.round(pos.y);
		//g.drawString("" + Math.toDegrees(velocity.angle()), roundedX, roundedY);
		//g.drawString("" + velocity.mag(), roundedX, roundedY + 20);
		g.drawString("" + pos.x, roundedX, roundedY + 40);
		g.drawString("" + pos.y, roundedX, roundedY + 60);
		g.drawString("" + momentum.mag(), roundedX, roundedY + 80);
		g.dispose();
	}
}
