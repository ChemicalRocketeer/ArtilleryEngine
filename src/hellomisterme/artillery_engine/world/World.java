package hellomisterme.artillery_engine.world;

import hellomisterme.artillery_engine.Err;
import hellomisterme.artillery_engine.Tick;
import hellomisterme.artillery_engine.entities.Entity;
import hellomisterme.artillery_engine.entities.mob.Player;
import hellomisterme.artillery_engine.graphics.Render;
import hellomisterme.artillery_engine.graphics.Renderable;
import hellomisterme.artillery_engine.io.Savable;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * An abstract class to describe a world. World keeps track of anything in the game.
 * 
 * @since 10-23-12
 * @author David Aaron Suddjian
 */
public abstract class World implements Tick, Savable, Renderable {

	private String name = "New Game";
	private Rectangle bounds = new Rectangle(256, 256);

	private List<Entity> entities = new ArrayList<Entity>();
	private List<Tick> tickables = new ArrayList<Tick>();
	private List<Savable> savables = new ArrayList<Savable>();

	/**
	 * The ultimate controlling dude. The overarching overlord. The final cheese. The player.
	 */
	public Player player;

	/**
	 * Renders the world
	 */
	public void render(Render r, Graphics2D g) {
		for (Entity e : entities) {
			e.render(r, g);
		}
	}

	/**
	 * Writes this World's data to wherever the DataOutputStream goes. This World's data includes the data of all its registered savables.
	 * 
	 * TODO add switch statements with number codes for classes instead of saving/reading class names
	 * 
	 * @param out
	 *        the DataOutputStream used to save data
	 */
	public void save(DataOutputStream out) {
		try {
			// the first int saved is width, second is height
			out.writeInt(bounds.width);
			out.writeInt(bounds.height);

			out.writeInt(savables.size()); // write how many savables there are
		} catch (IOException e) {
			Err.error("World can't write data!");
		}

		// save all the savable data
		for (Savable s : savables) {
			try {
				out.writeUTF(s.getClass().getName()); // save the class name so we'll know what class it is later TODO: use switch statements or something, I dunno
			} catch (IOException e) {
				Err.error("Can't save class name " + s.getClass().getName() + "!");
				e.printStackTrace();
			}
			s.save(out);
		}
	}

	/**
	 * Loads saved World data from a DataInputStream
	 * 
	 * @param in
	 *        the DataInputStream to use to load data from
	 */
	public void load(DataInputStream in, String version) {
		// clear all lists. We're starting this world over from scratch.
		entities.clear();
		tickables.clear();
		savables.clear();
		try {
			// the first int read is width, second is height
			bounds.width = in.readInt();
			bounds.height = in.readInt();

			for (int i = in.readInt(); i > 0; i--) {
				// whatever we're reading must be savable because it saved its data
				Savable s = (Savable) Class.forName(in.readUTF()).newInstance(); // create an object based on the class name read
				add(s);
				s.load(in, version);
				if (s instanceof Player) player = (Player) s;
			}
		} catch (Exception e) {
			Err.error("World can't load saved data! Please send the world save in a bug report.");
			e.printStackTrace();
		}
	}

	/**
	 * Calls the tick methods of all registered Tick objects
	 */
	public final void callTick() {
		// tick all the registered Tick objects
		for (Tick tock : tickables) {
			tock.tick();
		}
	}

	public void tick() {

	}

	/**
	 * Adds an Object to this World as appropriate
	 * 
	 * TODO replace all add/remove calls with specific calls. add() and remove() are for ease of development purposes. In final versions their use should be avoided.
	 * 
	 * @param o
	 *        the Object to add
	 */
	public void add(Object o) {
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

	/**
	 * Removes every appropriate reference to an Object from this World
	 * 
	 * @param o
	 *        the Object to remove
	 */
	public void remove(Object o) {
		if (o == null) {
			Err.error("Trying to remove null Object from World!"); // TODO remove
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
	 * Adds an Entity to the World's Entity list, allowing it to be displayed. Also sets the Entity's world variable to this.
	 * 
	 * @param e
	 *        the Entity to add
	 */
	public void addEntity(Entity e) {
		if (e == null) {
			Err.error("Trying to add null Entity to World!"); // TODO remove
		} else if (!entities.contains(e)) {
			entities.add(e);
			e.addToWorld(this);
		} else {
			System.out.println("Trying to add an Entity that is already in World..."); // TODO use in debug mode
		}
	}

	/**
	 * Removes an Entity from the World's Entity list
	 * 
	 * @param e
	 *        the Entity to remove
	 */
	public void removeEntity(Entity e) {
		if (e == null) {
			Err.error("Trying to remove null Entity from World!");
		} else if (entities.contains(e)) {
			entities.remove(e);
		} else {
			System.out.println("Trying to remove an Entity that isn't in World..."); // TODO use in debug mode
		}
	}

	/**
	 * Adds an object implementing Tick to the World, thereby putting it in the tick cycle
	 * 
	 * @param t
	 *        the object to add
	 */
	public void addTickable(Tick t) {
		if (t == null) {
			Err.error("Trying to add null Tick to World!");
		} else if (!tickables.contains(t)) {
			tickables.add(t);
		} else {
			System.out.println("Trying to add a Tick that is already in World..."); // TODO use in debug mode
		}
	}

	/**
	 * Removes an object implementing Tick from the World's Tick list, removing it from the tick cycle
	 * 
	 * @param t
	 *        the object to remove
	 */
	public void removeTickable(Tick t) {
		if (t == null) {
			Err.error("Trying to remove null Tick from World!");
		} else if (tickables.contains(t)) {
			tickables.add(t);
		} else {
			System.out.println("Trying to remove a Tick that isn't in World..."); // TODO use in debug mode
		}
	}

	/**
	 * Adds a Savable object to this World's list of Savables
	 * 
	 * @param s
	 *        the Savable object to add
	 */
	public void addSavable(Savable s) {
		if (s == null) {
			Err.error("Trying to add null Savable to World!");
		} else if (!savables.contains(s)) {
			savables.add(s);
		} else {
			System.out.println("Trying to add a Savable already in World..."); // TODO use in debug mode
		}
	}

	/**
	 * Removes a Savable object from the World's list of Savables
	 * 
	 * @param t
	 *        the Savable object to remove
	 */
	public void removeSavable(Savable s) {
		if (s == null) {
			Err.error("Trying to remove null Savable from World!");
		} else if (savables.contains(s)) {
			savables.remove(s);
		} else {
			System.out.println("Trying to remove a Savable that isn't in World..."); // TODO use in debug mode
		}
	}

	/**
	 * Makes a player
	 */
	public void makePlayer() {
		player = new Player();
		add(player);
	}

	/**
	 * @return the width of the world
	 */
	public int getWidth() {
		return bounds.width;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(int width) {
		bounds.width = width;
	}

	/**
	 * @return the height of the world
	 */
	public int getHeight() {
		return bounds.height;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(int height) {
		bounds.height = height;
	}

	/**
	 * @return this world's name, can be used for whatever a name is useful for
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the string that will be considered this world's name from now on
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return all known Enitities
	 */
	public List<Entity> getEntities() {
		return entities;
	}
}
