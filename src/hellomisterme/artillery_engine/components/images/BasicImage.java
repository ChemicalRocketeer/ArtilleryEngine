package hellomisterme.artillery_engine.components.images;

import hellomisterme.artillery_engine.Game;
import hellomisterme.artillery_engine.components.Component;
import hellomisterme.artillery_engine.rendering.PixelData;
import hellomisterme.artillery_engine.util.Vector;

public class BasicImage extends Component {
	
	private PixelData pixeldata;
	public Vector position;
	public boolean visible = true;
	
	public BasicImage() {
		this(null, new Vector());
	}
	
	public BasicImage(String path) {
		this(Game.getSpriteSheet().getPixelData(path, true), new Vector());
	}
	
	public BasicImage(PixelData pixeldata, Vector pos) {
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
