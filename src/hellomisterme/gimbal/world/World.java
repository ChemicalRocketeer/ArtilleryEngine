package hellomisterme.gimbal.world;

import hellomisterme.gimbal.Err;
import hellomisterme.gimbal.Tick;
import hellomisterme.gimbal.entities.Entity;

import java.util.LinkedList;
import java.util.List;

/**
 * A superclass to describe a world.
 * 
 * World keeps track of entities and anything else in the game.
 * 
 * TODO initialize Lists with starting values for efficiency
 * 
 * @since 10-23-12
 * @author David Aaron Suddjian
 */
public abstract class World {

	private int width = 256, height = 256;
	private List<Entity> entities = new LinkedList<Entity>();
	private List<Tick> tickables = new LinkedList<Tick>();

	public void tick() {

	}

	public final void callTick() {
		// tick all the registered Tick objects
		for (Tick tock : tickables) {
			if (tock == null) {
				System.out.println(Err.error("Can't call a null Tick!"));
			} else {
				tock.tick();
			}
		}
	}

	/**
	 * Adds an Entity to the World, allowing it to be displayed.
	 * 
	 * @param e
	 */
	public void add(Entity e) {
		if (e == null) {
			System.out.println(Err.error("Trying to add null Entity to World!"));
			return;
		} else if (!entities.contains(e)) {
			entities.add(e);
			if (e instanceof Tick) {
				addTicker((Tick) e);
			}
		}
	}

	/**
	 * Adds an object implementing Tick to the World, thereby putting it in the tick cycle
	 * 
	 * @param t the object to add
	 */
	public void addTicker(Tick t) {
		if (t == null) {
			System.out.println(Err.error("Trying to add null Tick to World!"));
		} else if (!tickables.contains(t)) {
			tickables.add(t);
		}
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public List<Entity> getEntities() {
		return entities;
	}
}
