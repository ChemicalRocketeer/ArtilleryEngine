package hellomisterme.artillery_engine.rendering;


/**
 * Renderable objects can render themselves using a Render object.
 * 
 * @since 1-4-13
 * @author David Aaron Suddjian
 */
public interface Renderable {
	
	public void render(Render render);
	
	/** Called after render if devmode is enabled */
	public void devmodeRender(Render render);
}
