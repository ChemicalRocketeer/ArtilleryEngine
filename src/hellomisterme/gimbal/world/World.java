package hellomisterme.gimbal.world;

import hellomisterme.gimbal.Err;
import hellomisterme.gimbal.Tick;
import hellomisterme.gimbal.entities.Entity;
import hellomisterme.gimbal.entities.EntityHolder;

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
public abstract class World implements EntityHolder {

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
	 * Adds an Entity to the World, allowing it to be displayed. If the Entity implements Tick, also adds it to tickables
	 * 
	 * @param e the Entity to add
	 */
	public void add(Entity e) {
		if (e == null) {
			System.out.println(Err.error("Trying to add null Entity to World!"));
		} else if (!entities.contains(e)) {
			entities.add(e);
			if (e instanceof Tick) {
				addTickable((Tick) e);
			}
		} else {
			System.out.println("Trying to add an Entity that is already in World...");
		}
	}

	/**
	 * Adds an object implementing Tick to the World, thereby putting it in the tick cycle
	 * 
	 * @param t the object to add
	 */
	public void addTickable(Tick t) {
		if (t == null) {
			System.out.println(Err.error("Trying to add null Tick to World!"));
		} else if (!tickables.contains(t)) {
			tickables.add(t);
		} else {
			System.out.println("Trying to add a Tick that is already in World...");
		}
	}
	
	/**
	 * Removes an Entity from the World. If the Entity implements Tick, also removes it from tickables
	 * 
	 * @param e the Entity to remove
	 */
	public void remove(Entity e) {
		if (e == null) {
			System.out.println(Err.error("Trying to remove null Entity from World!"));
		} else if (entities.contains(e)) {
			entities.remove(e);
			if ( e instanceof Tick) {
				removeTickable((Tick) e);
			}
		} else {
			System.out.println("Trying to remove an Entity that isn't in World...");
		}
	}
	
	/**
	 * Removes an object implementing Tick from the World, removing it from the tick cycle
	 * 
	 * @param t the object to remove
	 */
	public void removeTickable(Tick t) {
		if (t == null) {
			System.out.println(Err.error("Trying to remove null Tick from World!"));
		} else if (tickables.contains(t)) {
			tickables.add(t);
		} else {
			System.out.println("Trying to remove a Tick that isn't in World...");
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
