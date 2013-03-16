package hellomisterme.artillery_engine.game.components.scripts;

import hellomisterme.artillery_engine.game.Entity;
import hellomisterme.artillery_engine.game.components.Mass;
import hellomisterme.artillery_engine.game.components.PhysicsForces;
import hellomisterme.artillery_engine.graphics.Render;
import hellomisterme.artillery_engine.graphics.Renderable;

import java.awt.Color;

public class Planet extends Script implements Renderable {

	Mass mass;
	PhysicsForces forces;

	public Planet() {
		require(PhysicsForces.class);
	}

	@Override
	public void init() {
		mass = (Mass) entity.getComponent(Mass.class);

	}

	@Override
	public void tick() {
		PhysicsForces physics = (PhysicsForces) entity.getComponent(PhysicsForces.class);
		physics.applyForce(physics.gravity(Entity.getWorld().getBodies()));
	}

	@Override
	public void render(Render r) {
		Color savedColor = r.graphics.getColor();
		r.graphics.setColor(Color.WHITE);
		int m = (int) mass.mass;
		r.graphics.fillOval((int) (entity.transform.position.x - mass.mass / 1000), (int) (entity.transform.position.y - mass.mass / 1000), m / 500, m / 500);
		r.graphics.setColor(savedColor);
	}
}
