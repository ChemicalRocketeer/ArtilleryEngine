package possiblydavid.gimbal.world;

import java.util.ArrayList;
import java.util.List;

import possiblydavid.gimbal.Err;
import possiblydavid.gimbal.Tick;
import possiblydavid.gimbal.entities.Entity;

/**
 * A superclass to describe a world.
 * 
 * World keeps track of entities and anything else in the game. 
 * 
 * TODO initialize Lists with starting values for efficiency
 * 
 * @author David Aaron Suddjian
 */
public abstract class World {

	private int width = 256, height = 256;
	private List<Entity> entities = new ArrayList<Entity>();
	private List<Tick> tickers = new ArrayList<Tick>();

	public void tick() {
		
	}

	public void callTick() {
		// tick all the registered tickers
		for (Tick tock : tickers) {
			tock.tick();
		}
	}

	/**
	 * Adds an Entity to the World, allowing it to be displayed.
	 * @param e
	 */
	public void add(Entity e) {
		if (e == null) {
			Err.error(0);
			return;
		} else {
			entities.add(e);
		}
	}
	
	public void addTicker(Tick t) {
		if (t != null) {
			tickers.add(t);
		} else {
			Err.error(1);
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
