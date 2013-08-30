package hellomisterme.artillery_engine.components.images;

import hellomisterme.artillery_engine.Game;
import hellomisterme.artillery_engine.components.IngameComponent;
import hellomisterme.util.Transform;

import java.awt.image.BufferedImage;

public class AdvancedImage extends IngameComponent {

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

	public void setImage(BufferedImage image) {
		this.image = image;
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

}
