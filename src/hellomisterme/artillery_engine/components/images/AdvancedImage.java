package hellomisterme.artillery_engine.components.images;

import hellomisterme.artillery_engine.Game;
import hellomisterme.artillery_engine.components.IngameComponent;
import hellomisterme.artillery_engine.rendering.Render;
import hellomisterme.artillery_engine.rendering.Renderable;
import hellomisterme.artillery_engine.util.Transform;
import hellomisterme.artillery_engine.util.Vector;

import java.awt.image.BufferedImage;

public class AdvancedImage extends IngameComponent implements Renderable {
	
	private BufferedImage image;
	public boolean visible = true;
	
	public AdvancedImage() {
		this(null, new Transform());
	}
	
	public AdvancedImage(String path) {
		this(Game.getSpriteSheet().getBufferedImage(path, true), new Transform());
	}
	
	public AdvancedImage(BufferedImage image, Transform transform) {
		this.image = image;
		this.transform = transform;
	}
	
	public AdvancedImage(BufferedImage image, Transform transform, boolean visible) {
		this.image = image;
		this.transform = transform;
		this.visible = visible;
	}
	
	public BufferedImage getImage() {
		return image;
	}
	
	public int[] getPixels() {
		return image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
	}
	
	public int getWidth() {
		return image.getWidth();
	}
	
	public int getHeight() {
		return image.getHeight();
	}
	
	@Override
	public void render(Render render) {
		if (visible) {
			double rotation = globalRotation();
			Vector scale = globalScale();
			Vector pos = entity.transform.position.ADD(transform.position);
			render.render(image, new Transform(pos, rotation, scale), transform.position);
		}
	}
	
	@Override
	public void devmodeRender(Render render) {
	}
	
}
