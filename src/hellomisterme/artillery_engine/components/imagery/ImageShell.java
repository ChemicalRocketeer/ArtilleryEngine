package hellomisterme.artillery_engine.components.imagery;

import hellomisterme.artillery_engine.components.IngameComponent;
import hellomisterme.artillery_engine.rendering.Render;
import hellomisterme.artillery_engine.rendering.Renderable;

/**
 * A class dealing with image data.
 * 
 * @since 11-12-12
 * @author David Aaron Suddjian
 */
public abstract class ImageShell extends IngameComponent implements Renderable {

	public boolean visible = true;

	@Override
	public void render(Render render) {
		if (visible) {
			render.render(this, globalPosition(), Render.FOLLOW_CAMERA_MODE);
		}
	}

	/**
	 * Returns all the pixels of this ImageShell as ints.
	 * 
	 * @return an int[] of all the pixels
	 */
	public abstract int[] getPixels();

	/**
	 * Returns the width value of this ImageShell.
	 * 
	 * @return the width value
	 */
	public abstract int getWidth();

	/**
	 * Returns the height value of this ImageShell.
	 * 
	 * @return the height value
	 */
	public abstract int getHeight();
}
