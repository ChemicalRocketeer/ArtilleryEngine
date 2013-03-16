package hellomisterme.artillery_engine.game.components.rendering;

import hellomisterme.artillery_engine.game.components.IngameComponent;
import hellomisterme.artillery_engine.graphics.Render;
import hellomisterme.artillery_engine.graphics.Renderable;

/**
 * Methods to interact with image data.
 * 
 * @since 11-12-12
 * @author David Aaron Suddjian
 */
public abstract class BasicImage extends IngameComponent implements Renderable {

	@Override
	public void render(Render render) {
		render.render(this, entity.transform.position);
	}

	/**
	 * Returns all the pixels of this BasicImage as ints.
	 * 
	 * @return an int[] of all the pixels
	 */
	public abstract int[] getPixels();

	/**
	 * Returns the width value of this BasicImage.
	 * 
	 * @return the width value
	 */
	public abstract int getWidth();

	/**
	 * Returns the height value of this BasicImage.
	 * 
	 * @return the height value
	 */
	public abstract int getHeight();
}
