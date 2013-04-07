package hellomisterme.artillery_engine.components.imagery;

import hellomisterme.artillery_engine.Err;
import hellomisterme.artillery_engine.rendering.Render;
import hellomisterme.util.Vector2;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ArtImage extends ImageShell {

	private BufferedImage sprite;

	public ArtImage(String imageFile) {
		try {
			sprite = ImageIO.read(new File(imageFile));
		} catch (IOException e) {
			Err.error("Can't read heavy sprite!", e);
		}
	}

	@Override
	public void render(Render render) {
		if (visible) {
			Vector2 scale = globalScale();
			double rotation = globalRotation();
			Vector2 center = entity.globalPosition();
			// img, center, offset, rotation, scale
			render.render(sprite, center, transform.position, rotation, scale, Render.INGAME_CAMERA);
		}
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
