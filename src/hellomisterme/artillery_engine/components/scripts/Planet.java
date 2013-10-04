package hellomisterme.artillery_engine.components.scripts;

import hellomisterme.artillery_engine.components.physics.FreeBody;
import hellomisterme.artillery_engine.rendering.Render;
import hellomisterme.artillery_engine.rendering.Renderable;

import java.awt.Color;
import java.awt.Graphics2D;

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
	public void tick() {
		// entity.transform.position.add(new Vector(0.1, 0.1));
	}

	@Override
	public void render(Render render) {
		double size = freeBody.mass;
		Graphics2D graphics = render.getCameraGraphics();
		graphics.setColor(color);
		graphics.fillOval((int) (entity.transform.position.x - size * 0.5), (int) (entity.transform.position.y - size * 0.5), (int) size, (int) size);
		graphics.dispose();
	}
}
