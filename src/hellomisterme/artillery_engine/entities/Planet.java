package hellomisterme.artillery_engine.entities;

import hellomisterme.artillery_engine.graphics.Render;

import java.awt.Graphics2D;

public class Planet extends Mover implements Mass {

	private double mass = 0;

	public void render(Render r, Graphics2D g) {

	}

	public double getMass() {
		return mass;
	}
}
