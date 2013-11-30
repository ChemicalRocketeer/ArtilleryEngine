package hellomisterme.artillery_engine.rendering;

import hellomisterme.artillery_engine.Game;

public class Texture {

	private int textureIndex;
	private int xOffset, yOffset;

	public Texture(String path, int xOffset, int yOffset) {
		textureIndex = Game.getSpriteSheet().getIndex(path, true);
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}

	public Texture(String path) {
		this(path, 0, 0);
	}

	public PixelData getImage() {
		return Game.getSpriteSheet().getPixelData(textureIndex);
	}

	public int getXOffset() {
		return xOffset;
	}

	public int getYOffset() {
		return yOffset;
	}
}
