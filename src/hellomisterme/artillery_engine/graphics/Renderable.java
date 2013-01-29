package hellomisterme.artillery_engine.graphics;

import java.awt.Graphics2D;


/**
 * Renderable objects can render themselves onto a Graphics object.
 * 
 * @since 1-4-13
 * @author David Aaron Suddjian
 */
public interface Renderable {

	public void render(Render r, Graphics2D g);
}
