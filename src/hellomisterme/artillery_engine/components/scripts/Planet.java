package hellomisterme.artillery_engine.components.scripts;

import hellomisterme.artillery_engine.components.physics.FreeBody;
import hellomisterme.artillery_engine.graphics.Render;
import hellomisterme.artillery_engine.graphics.Renderable;

import java.awt.Color;

public class Planet extends Script implements Renderable {

	FreeBody freeBody;
	Color color = Color.GREEN;

	public Planet() {
	}

	@Override
	public void init() {
		require(FreeBody.class);
		freeBody = entity.getFreeBody();
	}

	@Override
	public void render(Render r) {
		Color savedColor = r.graphics.getColor();
		r.graphics.setColor(color);
		double width = freeBody.mass * entity.transform.scale.x;
		double height = freeBody.mass * entity.transform.scale.y;
		r.graphics.fillOval((int) (entity.transform.position.x - width * 0.5), (int) (entity.transform.position.y - height * 0.5), (int) width, (int) height);
		r.graphics.setColor(savedColor);
	}
}
