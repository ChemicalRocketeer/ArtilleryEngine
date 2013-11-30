package hellomisterme.artillery_engine.rendering;

import hellomisterme.artillery_engine.components.Camera;
import hellomisterme.artillery_engine.geometry.AABB;
import hellomisterme.artillery_engine.util.Transform;
import hellomisterme.artillery_engine.util.Vector;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * Render provides methods to render objects onto the screen using world-space coordinates instead of screen-space.
 * All transformation and position arguments are expected to be given in world-space.
 * No methods in the Render class change values of arguments passed to them unless explicitly stated otherwise.
 * 
 * @since 10-14-12
 * @author David Aaron Suddjian
 */
public class Render {
	
	public Screen screen;
	
	// Used to avoid calculating the camera's global position every time anything is rendered.
	// Must be updated each render cycle.
	private Transform camera = new Transform();
	// a copy of the camera transform, which can be used to quickly set up a Graphics object.
	private AffineTransform graphicsTransform = new AffineTransform();
	
	public Render(Screen screen) {
		this.screen = screen;
	}
	
	public void textureArea(Texture texture, AABB area) {
		Vector areaPos = new Vector(area.left, area.top);
		Vector areaSize = new Vector(area.right, area.bottom).sub(areaPos);
		areaPos = toScreenSpace(areaPos);
		screen.textureArea(texture, new AABB(areaPos, areaSize.x, areaSize.y));
	}

	/**
	 * Draws pixel data at the indicated point
	 * 
	 * @param img the ImageShell to be drawn
	 * @param xPos the xLocation coordinate of the top-left corner of the PixelData
	 * @param yPos the yLocation coordinate of the top-left corner of the PixelData
	 */
	public void render(PixelData img, int xPos, int yPos) {
		Vector pos = toScreenSpace(new Vector(xPos, yPos));
		screen.render(img, (int) pos.x, (int) pos.y);
	}
	
	public void drawArrow(Vector start, Vector end, Color c) {
		screen.drawArrow(toScreenSpace(start), toScreenSpace(end), c);
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
	public void render(BufferedImage img, Transform transform, Vector center) {
		// rotation += camera.transform.rotation;
		// scale.mul(camera.transform.scale);
		screen.render(img, toScreenSpace(transform), center);
	}
	
	/**
	 * Creates a Graphics2D object aligned with the ingame camera.
	 * Objects with custom render methods can simply draw on this graphics object using world-space coordinates.
	 * You should dispose of the Graphics object when you are done.
	 */
	public Graphics2D getCameraGraphics() {
		Graphics2D g = screen.getGraphics();
		alignGraphicsToCamera(g);
		return g;
	}
	
	public void alignGraphicsToCamera(Graphics2D g) {
		g.setTransform(graphicsTransform);
	}
	
	/** Converts a Vector from world space to screen space. Does not change the given vector. */
	public Vector toScreenSpace(Vector point) {
		Vector result = new Vector(point).sub(camera.position);
		result.rotate(-camera.rotation);
		result.add(new Vector(screen.centerX, screen.centerY));
		return result;
	}
	
	public Vector toWorldSpace(Vector point) {
		Vector result = new Vector(point).add(camera.position);
		result.rotate(camera.rotation);
		result.sub(new Vector(screen.centerX, screen.centerY));
		return result;
	}
	/**
	 * Transforms the given Transform from world-space into screen-space
	 * 
	 * @return the transform in screen-space
	 */
	public Transform toScreenSpace(Transform transform) {
		Transform result = transform.SUB(camera);
		result.position.add(new Vector(screen.centerX, screen.centerY));
		return result;
	}
	
	/**
	 * Transforms the given Transform from screen-space into world-space
	 * 
	 * @return the transform in world-space
	 */
	public Transform toWorldSpace(Transform transform) {
		Transform result = transform.ADD(camera);
		result.position.sub(new Vector(screen.centerX, screen.centerY));
		return result;
	}
	
	/**
	 * Gets the transform state of the given camera and uses that to transform anything rendered in the future.
	 * Call this method before every frame.
	 */
	public void setCamera(Camera activeCamera) {
		camera = activeCamera.globalTransform();
		graphicsTransform = new AffineTransform();
		graphicsTransform.translate(screen.centerX, screen.centerY);
		graphicsTransform.rotate(-camera.rotation);
		graphicsTransform.translate(-camera.position.x, -camera.position.y);
		
	}
	
	public Transform getCameraView() {
		return camera.clone();
	}
}