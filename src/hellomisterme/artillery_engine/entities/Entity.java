package hellomisterme.artillery_engine.entities;

import hellomisterme.artillery_engine.Game;
import hellomisterme.artillery_engine.graphics.BasicImage;
import hellomisterme.artillery_engine.graphics.Render;
import hellomisterme.artillery_engine.graphics.Renderable;
import hellomisterme.artillery_engine.io.Savable;
import hellomisterme.artillery_engine.world.World;

import java.awt.Graphics2D;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * An Entity is an object in the World.
 * 
 * @since 10-16-12
 * @author David Aaron Suddjian
 */
public abstract class Entity implements Savable, Renderable {

	protected BasicImage image;

	/**
	 * This method will be called when the entity is added to a World object. By default it does nothing but it can be overridden.
	 * 
	 * @param w
	 *            the World that this Entity has been added to
	 */
	public void addedToWorld(World w) {

	}

	/**
	 * Saves this Entity's personal data.
	 */
	public void save(DataOutputStream out) {
		try {
			out.writeInt(getX());
			out.writeInt(getY());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads this Entity's personal data.
	 */
	public void load(DataInputStream in, String version) {
		try {
			setPos(in.readInt(), in.readInt());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Renders this Entity's image onto the given objects
	 */
	public void render(Render render, Graphics2D g) {
		render.render(getImage(), getX(), getY());
		
	}

	/**
	 * Returns the Sprite. Can return null.
	 * 
	 * @return the Sprite associated with this Entity
	 */
	public BasicImage getImage() {
		return image;
	}

	protected void setImage(BasicImage img) {
		image = img;
	}

	/**
	 * @return the X coordinate of this Entity, increasing from left to right
	 */
	public abstract int getX();

	/**
	 * @return the Y coordinate of this Entity, increasing from top to bottom
	 */
	public abstract int getY();

	public abstract void setPos(int x, int y);
	
	public static World getWorld() {
		return Game.getWorld();
	}
}
