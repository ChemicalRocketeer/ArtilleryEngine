package hellomisterme.artillery_engine.components.images;

import hellomisterme.artillery_engine.Game;
import hellomisterme.artillery_engine.rendering.PixelData;
import hellomisterme.util.Vector2;

public class BasicImage {

	private PixelData pixeldata;
	public Vector2 position;
	public boolean visible = true;

	public BasicImage() {
		this(null, new Vector2());
	}

	public BasicImage(String path) {
		this(Game.getSpriteSheet().getPixelData(path, true), new Vector2());
	}

	public BasicImage(PixelData pixeldata, Vector2 pos) {
		this.pixeldata = pixeldata;
		this.position = pos;
	}

	public void setImage(PixelData pd) {
		pixeldata = pd;
	}

	public int[] getPixels() {
		return pixeldata.getPixels();
	}

	public int getWidth() {
		return pixeldata.getWidth();
	}

	public int getHeight() {
		return pixeldata.getHeight();
	}
}
