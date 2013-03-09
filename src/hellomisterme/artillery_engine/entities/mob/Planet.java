package hellomisterme.artillery_engine.entities.mob;

import java.awt.Color;

import hellomisterme.artillery_engine.graphics.Render;

public class Planet extends Mob {

	public Planet(double mass, double x, double y) {
		getWorld().addBody(this);
		getWorld().addEntity(this);
		this.mass = mass;
		this.x = x;
		this.y = y;
	}

	@Override
	public void tick() {
		gravitate(getWorld().getBodies());
	}

	@Override
	public void render(Render r) {
		Color savedColor = r.graphics.getColor();
		r.graphics.setColor(Color.WHITE);
		r.graphics.fillOval((int) (x - mass / 1000), (int) (y - mass / 1000), (int) mass / 500, (int) mass / 500);
		r.graphics.setColor(savedColor);
	}
}
