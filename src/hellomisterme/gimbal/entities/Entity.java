package hellomisterme.gimbal.entities;

import hellomisterme.gimbal.Err;
import hellomisterme.gimbal.graphics.GimbalImage;
import hellomisterme.gimbal.graphics.Render;
import hellomisterme.gimbal.graphics.Renderable;
import hellomisterme.gimbal.io.Savable;
import hellomisterme.gimbal.world.World;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * An Entity is an object in the game.
 * 
 * @since 10-16-12
 * @author David Aaron Suddjian
 */
public abstract class Entity implements Savable, Renderable {

	protected GimbalImage image;
	public Rectangle bounds;

	/**
	 * This method will be called when the entity is added to a World object. By default it does nothing but it can be overridden.
	 * 
	 * @param w
	 *            the World that this Entity has been added to
	 */
	public void addedToWorld(World w) {

	}

	/**
	 * Saves this Entity's x and y coordinates, then if the LightweightImage isn't null, writes a true and calls the LightweightImage's save() method. Else writes a false.
	 */
	public void save(DataOutputStream out) {
		try {
			out.writeInt(getX());
			out.writeInt(getY());
			saveData(out);
			if (image != null) {
				out.writeBoolean(true);
				image.save(out);
			} else {
				out.writeBoolean(false);
			}
		} catch (IOException e) {
			Err.error("Can't save Entity data!");
			e.printStackTrace();
		}
	}

	/**
	 * Saves this Entity's data.
	 */
	public void saveData(DataOutputStream out) {

	}

	/**
	 * Calls loadData(), then if it reads a true, calls loadImage().
	 */
	public void load(DataInputStream in) {
		try {
			setPos(in.readInt(), in.readInt());
			loadData(in);
			if (in.readBoolean()) {
				loadImage(in);
			}
		} catch (IOException e) {
			Err.error("Can't read Entity data!");
			e.printStackTrace();
		}
	}

	/**
	 * Loads this Entity's saved data.
	 */
	public void loadData(DataInputStream in) {

	}

	/**
	 * Loads saved image data. Can be overridden to load other types of images.
	 */
	public void loadImage(DataInputStream in) {
		image.load(in);
	}

	public void render(Graphics g, Render render) {
		if (getImage() != null)
			render.render(getImage(), getX(), getY());
	}

	/**
	 * Returns the LightweightImage. Can return null.
	 * 
	 * @return the LightweightImage associated with this Entity
	 */
	public GimbalImage getImage() {
		if (image == null) {
			System.out.println("Returning null LightweightImage from Entity"); // TODO remove/change
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
}
