package hellomisterme.gimbal.world;

import hellomisterme.gimbal.Err;
import hellomisterme.gimbal.Tick;
import hellomisterme.gimbal.entities.Entity;

import java.util.ArrayList;
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
	private List<Entity> entities = new ArrayList<Entity>();
	private List<Tick> tickers = new ArrayList<Tick>();

	public void tick() {

	}

	public final void callTick() {
		// tick all the registered tickers
		for (Tick tock : tickers) {
			if (tock == null) {
				Err.error(4);
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
			Err.error(0);
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
			Err.error(1);
		} else if (!tickers.contains(t)) {
			tickers.add(t);
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
