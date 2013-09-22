package hellomisterme.artillery_engine.rendering;

import hellomisterme.artillery_engine.components.Camera;
import hellomisterme.util.Transform;
import hellomisterme.util.Vector2;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Render provides methods to render objects onto the screen using world-space coordinates instead of screen-space.
 * All transformation and position arguments are expected to be given in world-space.
 * 
 * @since 10-14-12
 * @author David Aaron Suddjian
 */
public class Render {
	
	/** Flag to tell objects how they should render themselves */
	public boolean simpleRendering = false;
	public Screen screen;
	
	// Used to avoid calculating the camera's global position every time anything is rendered.
	// Must be updated each render cycle.
	private Transform camera = new Transform();
	
	public Render(Screen screen) {
		this.screen = screen;
	}
	
	/**
	 * Renders the given BufferedImage.
	 * 
	 * @param img
	 * @param center
	 * @param position
	 * @param rotation
	 * @param scale
	 */
	public void render(BufferedImage img, Transform transform) {
		// rotation += camera.transform.rotation;
		// scale.mul(camera.transform.scale);
		screen.render(img, toScreenSpace(transform));
	}
	
	/**
	 * Draws pixel data at the indicated point
	 * 
	 * @param img the ImageShell to be drawn
	 * @param xPos the xLocation coordinate of the top-left corner of the PixelData
	 * @param yPos the yLocation coordinate of the top-left corner of the PixelData
	 */
	public void render(PixelData img, int xPos, int yPos) {
		Vector2 pos = toScreenSpace(new Vector2(xPos, yPos));
		screen.render(img, (int) pos.x, (int) pos.y);
	}
	
	public void drawArrow(Vector2 start, Vector2 end, Color c) {
		screen.drawArrow(toScreenSpace(start), toScreenSpace(end), c);
	}
	
	/**
	 * Creates a Graphics2D object aligned with the ingame camera.
	 * Objects with custom render methods can simply draw on this graphics object using their ingame coordinates.
	 */
	public Graphics2D getCameraGraphics() {
		Graphics2D g = screen.image.createGraphics();
		// TODO rotation/scaling support
		g.translate(-camera.position.x + screen.getWidth() * 0.5, -camera.position.y + screen.getHeight() * 0.5);
		return g;
	}
	
	public Vector2 toScreenSpace(Vector2 point) {
		// TODO add rotation/scale support to this and transform version
		Vector2 result = point.SUB(camera.position);
		result.add(new Vector2(screen.getWidth() / 2, screen.getHeight() / 2));
		return result;
	}
	
	public Vector2 toWorldSpace(Vector2 point) {
		Vector2 result = point.ADD(camera.position);
		result.sub(new Vector2(screen.getWidth() / 2, screen.getHeight() / 2));
		return result;
	}
	
	/**
	 * Transforms the given Transform from world-space into screen-space
	 * 
	 * @return the transform in screen-space
	 */
	public Transform toScreenSpace(Transform transform) {
		Transform result = transform.SUB(camera);
		result.position.add(new Vector2(screen.getWidth() / 2, screen.getHeight() / 2));
		return result;
	}
	
	/**
	 * Transforms the given Transform from screen-space into world-space
	 * 
	 * @return the transform in world-space
	 */
	public Transform toWorldSpace(Transform transform) {
		Transform result = transform.ADD(camera);
		result.position.sub(new Vector2(screen.getWidth() / 2, screen.getHeight() / 2));
		return result;
	}
	
	/**
	 * Gets the global transform of the given camera and uses that to transform anything rendered in the future.
	 * Call this method before every frame.
	 */
	public void setCamera(Camera activeCamera) {
		camera = activeCamera.globalTransform();
	}
}