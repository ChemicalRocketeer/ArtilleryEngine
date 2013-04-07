package hellomisterme.artillery_engine.components.scripts;

import hellomisterme.artillery_engine.components.physics.FreeBody;
import hellomisterme.artillery_engine.rendering.Render;
import hellomisterme.artillery_engine.rendering.Renderable;

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
	public void render(Render render) {
		Color savedColor = render.graphics.getColor();
		render.graphics.setColor(color);
		double width = freeBody.mass * entity.transform.scale.x;
		double height = freeBody.mass * entity.transform.scale.y;
		render.setCameraMode(Render.FOLLOW_CAMERA_MODE);
		render.graphics.fillOval((int) (entity.transform.position.x - width * 0.5), (int) (entity.transform.position.y - height * 0.5), (int) width, (int) height);
		render.graphics.setColor(savedColor);
	}
}
