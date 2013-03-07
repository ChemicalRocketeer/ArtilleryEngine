package hellomisterme.artillery_engine.entities.mob;

import hellomisterme.artillery_engine.graphics.Render;

import java.awt.Graphics2D;

public class Planet extends Mob {
	
	public Planet(double mass, double x, double y) {
		getWorld().addBody(this);
		getWorld().addEntity(this);
		this.mass = mass;
		this.x = x;
		this.y = y;
	}
	
	public void tick() {
		gravitate(getWorld().getBodies());
	}

	public void render(Render r, Graphics2D g) {
		g.drawOval((int) (x - mass / 100), (int) (y - mass / 100), (int) mass / 50, (int) mass / 50);
	}
}
