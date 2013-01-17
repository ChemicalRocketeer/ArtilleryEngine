package hellomisterme.gimbal.entities;

import hellomisterme.gimbal.Game;
import hellomisterme.gimbal.graphics.GimbalImage;
import hellomisterme.gimbal.graphics.Render;
import hellomisterme.gimbal.graphics.Renderable;
import hellomisterme.gimbal.io.Savable;
import hellomisterme.gimbal.world.World;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * An Entity is an object in a World.
 * 
 * @since 10-16-12
 * @author David Aaron Suddjian
 */
public abstract class Entity implements Savable, Renderable {

	protected GimbalImage image;

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
	public void load(DataInputStream in, int version) {
		try {
			setPos(in.readInt(), in.readInt());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Renders this Entity's image onto the given objects
	 */
	public void render(Render render) {
		if (getImage() != null)
			render.render(getImage(), getX(), getY());
	}

	/**
	 * Returns the Sprite. Can return null.
	 * 
	 * @return the Sprite associated with this Entity
	 */
	public GimbalImage getImage() {
		if (image == null) {
			System.out.println("Returning null Sprite from Entity"); // TODO remove/change
		}
		return image;
	}

	protected void setImage(GimbalImage img) {
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
