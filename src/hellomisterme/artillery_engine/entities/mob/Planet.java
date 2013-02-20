package hellomisterme.artillery_engine.entities.mob;

import hellomisterme.artillery_engine.graphics.Render;

import java.awt.Graphics2D;

public class Planet extends Mob {

	private double mass = 0;
	
	public Planet(double mass) {
		this.mass = mass;
	}
	
	public void tick() {
		gravitate()
	}

	public void render(Render r, Graphics2D g) {
		g.drawOval((int) x, (int) y, (int) mass, (int) mass);
	}
}
