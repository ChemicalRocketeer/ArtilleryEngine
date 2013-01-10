package hellomisterme.gimbal.graphics;

import java.awt.Graphics;

/**
 * Renderable objects can render themselves onto a Graphics object.
 * 
 * @since 1-4-13
 * @author David Aaron Suddjian
 */
public interface Renderable {

	public void render(Graphics g, Render r);
}
