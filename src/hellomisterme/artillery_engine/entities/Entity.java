package hellomisterme.artillery_engine.entities;

import hellomisterme.artillery_engine.Game;
import hellomisterme.artillery_engine.graphics.BasicImage;
import hellomisterme.artillery_engine.graphics.Render;
import hellomisterme.artillery_engine.graphics.Renderable;
import hellomisterme.artillery_engine.world.World;

import java.awt.Graphics2D;

/**
 * An Entity is an object in the World.
 * 
 * @since 10-16-12
 * @author David Aaron Suddjian
 */
public abstract class Entity implements Renderable {

	private BasicImage image;

	protected Entity() {
		getWorld().addEntity(this);
	}

	/**
	 * Renders this Entity's image onto the given objects
	 */
	public void render(Render render, Graphics2D g) {
		render.render(getImage(), getIntX(), getIntY());
	}

	/**
	 * Returns the BasicImage. Can return null.
	 * 
	 * @return the BasicImage associated with this Entity
	 */
	public BasicImage getImage() {
		return image;
	}

	protected void setImage(BasicImage img) {
		image = img;
	}

	public abstract double getX();

	public abstract double getY();

	public abstract int getIntX();

	public abstract int getIntY();

	public abstract void setPos(int x, int y);

	public abstract void setPos(double x, double y);

	public static World getWorld() {
		return Game.getWorld();
	}
}
