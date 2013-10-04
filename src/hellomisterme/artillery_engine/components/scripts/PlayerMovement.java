package hellomisterme.artillery_engine.components.scripts;

import hellomisterme.artillery_engine.components.physics.FreeBody;
import hellomisterme.artillery_engine.geometry.Circle;
import hellomisterme.artillery_engine.io.Keyboard;
import hellomisterme.artillery_engine.rendering.Render;
import hellomisterme.artillery_engine.rendering.Renderable;
import hellomisterme.util.Vector;

import java.awt.Color;
import java.awt.Graphics2D;

public class PlayerMovement extends Script implements Renderable {
	
	private FreeBody freeBody;
	
	private double rotationForce = 0.0005;
	private double enginePower = 0.05;
	private double throttle = 0;
	public static final double MAX_THROTTLE = 1;
	private double throttleRate = 0.01;
	
	Circle c1 = new Circle(25.0, new Vector(-100, 3));
	Circle c2 = new Circle(100.0, new Vector(-200, 100));
	Circle c3 = new Circle(16, new Vector(100, 0));
	Vector c1c2 = new Vector();
	Vector c2c3 = new Vector();
	Vector c3c1 = new Vector();
	boolean c1c2b;
	boolean c2c3b;
	boolean c3c1b;
	
	public PlayerMovement() {
	}
	
	@Override
	public void init() {
		require(FreeBody.class);
		freeBody = (FreeBody) entity.getComponent(FreeBody.class);
	}
	
	@Override
	public void tick() {
		if (Keyboard.Controls.ROTLEFT.pressed())
			freeBody.spin -= rotationForce;
		if (Keyboard.Controls.ROTRIGHT.pressed())
			freeBody.spin += rotationForce;
		
		if (Keyboard.Controls.THROTTLEUP.pressed())
			throttle += throttleRate;
		if (Keyboard.Controls.THROTTLEDOWN.pressed())
			throttle -= throttleRate;
		if (Keyboard.Controls.THROTTLECUT.pressed())
			throttle = 0;
		
		if (throttle < 0)
			throttle = 0;
		else if (throttle > MAX_THROTTLE)
			throttle = MAX_THROTTLE;
		
		freeBody.applyForce(entity.transform.FORWARD(throttle * enginePower));
		
		c1.position.add(new Vector(-0.1, 0.1));
		c3.position.add(new Vector(-0.1, 0));
		c1c2b = c1.collision(c2, c1c2);
		c2c3b = c2.collision(c3, c2c3);
		c3c1b = c3.collision(c1, c3c1);
		// c1.position.add(c1c2);
		c2.position.add(c2c3);
		c3.position.add(c3c1);
	}
	
	@Override
	public void render(Render r) {
		Graphics2D g = r.getCameraGraphics();
		g.setColor(Color.RED);
		c1.render(g);
		g.dispose();
		g = r.getCameraGraphics();
		g.setColor(Color.ORANGE);
		c2.render(g);
		g.dispose();
		g = r.getCameraGraphics();
		g.setColor(Color.PINK);
		c3.render(g);
		g.dispose();
		
		if (c1c2b)
			r.drawArrow(c1.position, c1.position.ADD(c1c2), Color.MAGENTA);
		else
			r.drawArrow(c1.position, c1.position.ADD(c1c2), Color.RED);
		
		if (c2c3b)
			r.drawArrow(c2.position, c2.position.ADD(c2c3), Color.MAGENTA);
		else
			r.drawArrow(c2.position, c2.position.ADD(c2c3), Color.ORANGE);
		
		if (c3c1b)
			r.drawArrow(c3.position, c3.position.ADD(c3c1), Color.MAGENTA);
		else
			r.drawArrow(c3.position, c3.position.ADD(c3c1), Color.PINK);
	}
	
}
