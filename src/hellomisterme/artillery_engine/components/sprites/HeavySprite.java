package hellomisterme.artillery_engine.components.sprites;

import hellomisterme.artillery_engine.Err;
import hellomisterme.artillery_engine.graphics.Render;
import hellomisterme.util.Vector2;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class HeavySprite extends BasicImage {

	private BufferedImage sprite;

	public HeavySprite(String imageFile) {
		try {
			sprite = ImageIO.read(new File(imageFile));
		} catch (IOException e) {
			Err.error("Can't read heavy sprite!", e);
		}
	}

	@Override
	public void render(Render render) {
		Vector2 center = entity.globalPosition();
		Vector2 scale = entity.globalScale();
		scale.mul(transform.scale);
		render.render(sprite, center.ADD(transform.position), center, entity.transform.rotation, scale);
	}

	@Override
	public int[] getPixels() {
		return sprite.getRGB(0, 0, sprite.getWidth(), sprite.getHeight(), null, 0, sprite.getWidth());
	}

	@Override
	public int getWidth() {
		return sprite.getWidth();
	}

	@Override
	public int getHeight() {
		return sprite.getHeight();
	}

}
