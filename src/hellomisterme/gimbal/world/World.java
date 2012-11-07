package hellomisterme.gimbal.world;

import hellomisterme.gimbal.Err;
import hellomisterme.gimbal.Tick;
import hellomisterme.gimbal.entities.Entity;
import hellomisterme.gimbal.entities.EntityBucket;
import hellomisterme.gimbal.io.Savable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * An abstract class to describe a world. World keeps track of anything in the game.
 * 
 * @since 10-23-12
 * @author David Aaron Suddjian
 */
public abstract class World implements EntityBucket, Tick, Savable {

	private String name = "New Game";
	private int width = 256, height = 256;
	// If any of these lists is changed, remember to change instantiations in constructors and other methods like load!
	// TODO initialize Lists with starting values for efficiency
	private List<Entity> entities = new LinkedList<Entity>();
	private List<Tick> tickables = new LinkedList<Tick>();
	private List<Savable> savables = new LinkedList<Savable>();

	/**
	 * Writes this World's data to wherever the DataOutputStream goes. This World's data includes the data of all its registered savables.
	 * 
	 * TODO add switch statements with number codes for classes instead of saving/reading class names
	 * 
	 * @param out
	 *            the DataOutputStream used to save data
	 */
	public void save(DataOutputStream out) {
		try {
			// the first int saved is width, second is height
			out.writeInt(width);
			out.writeInt(height);
			// save each Savable's data
			for (Savable s : savables) {
				//write a boolean so we can tell there is another object when reading
				out.writeBoolean(true);
				out.writeUTF(s.getClass().getName());
				s.save(out);
			}
			// write false so we know when to stop when reading
			out.writeBoolean(false);
		} catch (IOException e) {
			Err.error("World can't write data!");
		}
	}

	/**
	 * Loads saved World data from a DataInputStream
	 * 
	 * @param in
	 *            the DataInputStream to use to load data from
	 */
	public void load(DataInputStream in) {
		try {
			// reset all lists. We're starting this world over from scratch.
			entities = new LinkedList<Entity>();
			tickables = new LinkedList<Tick>();
			savables = new LinkedList<Savable>();
			// the first int read is width, second is height
			width = in.readInt();
			height = in.readInt();
			// while there are true booleans there is another object to read data from
			while (in.readBoolean()) {
				// whatever we're reading must be savable because it saved this data
				String name = in.readUTF();
				Savable thing = (Savable) Class.forName(name).newInstance();
				thing.load(in);
				add(thing);
			}
			// when the false boolean is reached, we are at the end of this World's data
		} catch (Exception e) {
			Err.error("World can't load saved data! Please send the world save in a bug report.");
			e.printStackTrace();
		}
	}

	/**
	 * Calls this World's tick() method, and the tick() methods of all registered Tick objects
	 */
	public final void callTick() {
		// tick all the registered Tick objects
		tick();
		for (Tick tock : tickables) {
			if (tock == null) {
				Err.error("Can't call a null Tick!");
			} else {
				tock.tick();
			}
		}
	}
	
	public void tick() {
		
	}

	/**
	 * Adds an Object to this World as appropriate
	 * 
	 * TODO replace all add/remove calls with specific calls (for speed). add() and remove() are for ease of development purposes. In final versions their use should be avoided.
	 * 
	 * @param o
	 *            the Object to add
	 */
	public void add(Object o) {
		if (o == null) {
			Err.error("Trying to add null Object to World!");
		} else {
			if (o instanceof Entity) {
				addEntity((Entity) o);
			}
			if (o instanceof Tick) {
				addTickable((Tick) o);
			}
			if (o instanceof Savable) {
				addSavable((Savable) o);
			}
		}
	}

	/**
	 * Removes every appropriate reference to an Object from this World
	 * 
	 * @param o
	 *            the Object to remove
	 */
	public void remove(Object o) {
		if (o == null) {
			Err.error("Trying to remove null Object from World!");
		} else {
			if (o instanceof Entity) {
				removeEntity((Entity) o);
			}
			if (o instanceof Tick) {
				removeTickable((Tick) o);
			}
			if (o instanceof Savable) {
				removeSavable((Savable) o);
			}
		}
	}

	/**
	 * Adds an Entity to the World's Entity list, allowing it to be displayed
	 * 
	 * @param e
	 *            the Entity to add
	 */
	public void addEntity(Entity e) {
		if (e == null) {
			Err.error("Trying to add null Entity to World!");
		} else if (!entities.contains(e)) {
			entities.add(e);
			e.setEntityBucket(this);
		} else {
			System.out.println("Trying to add an Entity that is already in World...");
		}
	}

	/**
	 * Removes an Entity from the World's Entity list
	 * 
	 * @param e
	 *            the Entity to remove
	 */
	public void removeEntity(Entity e) {
		if (e == null) {
			Err.error("Trying to remove null Entity from World!");
		} else if (entities.contains(e)) {
			entities.remove(e);
		} else {
			System.out.println("Trying to remove an Entity that isn't in World...");
		}
	}

	/**
	 * Adds an object implementing Tick to the World, thereby putting it in the tick cycle
	 * 
	 * @param t
	 *            the object to add
	 */
	public void addTickable(Tick t) {
		if (t == null) {
			Err.error("Trying to add null Tick to World!");
		} else if (!tickables.contains(t)) {
			tickables.add(t);
		} else {
			System.out.println("Trying to add a Tick that is already in World...");
		}
	}

	/**
	 * Removes an object implementing Tick from the World's Tick list, removing it from the tick cycle
	 * 
	 * @param t
	 *            the object to remove
	 */
	public void removeTickable(Tick t) {
		if (t == null) {
			Err.error("Trying to remove null Tick from World!");
		} else if (tickables.contains(t)) {
			tickables.add(t);
		} else {
			System.out.println("Trying to remove a Tick that isn't in World...");
		}
	}

	/**
	 * Adds a Savable object to this World's list of Savables
	 * 
	 * @param s
	 *            the Savable object to add
	 */
	public void addSavable(Savable s) {
		if (s == null) {
			Err.error("Trying to add null Savable to World!");
		} else if (!savables.contains(s)) {
			savables.add(s);
		} else {
			System.out.println("Trying to add a Savable already in World...");
		}
	}

	/**
	 * Removes a Savable object from the World's list of Savables
	 * 
	 * @param t
	 *            the Savable object to remove
	 */
	public void removeSavable(Savable s) {
		if (s == null) {
			Err.error("Trying to remove null Savable from World!");
		} else if (savables.contains(s)) {
			savables.remove(s);
		} else {
			System.out.println("Trying to remove a Savable that isn't in World...");
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Entity> getEntities() {
		return entities;
	}
}
