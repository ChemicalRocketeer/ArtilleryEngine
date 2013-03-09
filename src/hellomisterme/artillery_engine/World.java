package hellomisterme.artillery_engine;

import hellomisterme.artillery_engine.entities.Entity;
import hellomisterme.artillery_engine.entities.Mass;
import hellomisterme.artillery_engine.entities.mob.Baddie;
import hellomisterme.artillery_engine.entities.mob.Planet;
import hellomisterme.artillery_engine.entities.mob.Player;
import hellomisterme.artillery_engine.graphics.Render;
import hellomisterme.artillery_engine.graphics.Renderable;
import hellomisterme.artillery_engine.io.Keyboard;
import hellomisterme.artillery_engine.io.Savable;

import java.awt.Dimension;
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
public class World implements Tick, Savable, Renderable {

	private String name = "New Game";
	private Dimension bounds;

	private List<Entity> entities;
	private List<Tick> tickables;
	private List<Savable> savables;
	private List<Mass> bodies;

	/** The ultimate controlling dude. The overarching overlord. The final cheese. The player. */
	public Player player;

	private boolean baddieOrdered = false;

	public World(int w, int h) {
		bounds = new Dimension(w, h);
		entities = new ArrayList<Entity>();
		tickables = new ArrayList<Tick>();
		savables = new ArrayList<Savable>();
		bodies = new ArrayList<Mass>();
	}

	public World(DataInputStream in, String version) {
		load(in, version);
	}

	public void init() {
		player = new Player();
		new Planet(10000, bounds.width / 2, bounds.height / 2);
	}

	@Override
	public void tick() {
		// tick all the registered Tick objects
		player.tick();
		for (Tick tock : tickables) {
			tock.tick();
		}

		// if the addbaddie key is pressed
		if (Keyboard.Controls.ADDBADDIE.pressed()) {
			if (baddieOrdered == false) { // if the key was up before
				new Baddie(Game.RAND.nextInt(getWidth()), Game.RAND.nextInt(getHeight()));
				baddieOrdered = true; // remember that the key was pressed
			}
		} else { // key not pressed
			baddieOrdered = false;
		}
	}

	/**
	 * Renders the world
	 */
	@Override
	public void render(Render r) {
		for (Entity e : entities) {
			e.render(r);
		}
		player.render(r);
	}

	/**
	 * Writes this World's data to wherever the DataOutputStream goes. This World's data includes the data of all its registered savables.
	 * 
	 * TODO add switch statements with number codes for classes instead of saving/reading class names
	 * 
	 * @param out
	 *        the DataOutputStream used to save data
	 */
	@Override
	public void save(DataOutputStream out) {
		try {
			out.writeUTF(name);
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
		player.save(out);
	}

	/**
	 * Loads saved World data from a DataInputStream
	 * 
	 * @param in
	 *        the DataInputStream to use to load data from
	 */
	@Override
	public void load(DataInputStream in, String version) {
		// clear all lists. We're starting this world over from scratch.
		entities.clear();
		tickables.clear();
		savables.clear();
		try {
			name = in.readUTF();
			// the first int read is width, second is height
			bounds.width = in.readInt();
			bounds.height = in.readInt();

			for (int i = in.readInt(); i > 0; i--) {
				// whatever we're reading must be savable because it saved its data
				Savable s = (Savable) Class.forName(in.readUTF()).newInstance(); // create an object based on the class name read
				s.load(in, version);
			}
		} catch (Exception e) {
			Err.error("World can't load saved data! Please send the world save in a bug report.");
			e.printStackTrace();
		}
		player.load(in, version);
	}

	public void addEntity(Entity e) {
		if (e == null) {
			Err.error("Trying to add null Entity to World!"); // TODO remove
		} else if (!entities.contains(e)) {
			entities.add(e);
		} else {
			System.out.println("Trying to add an Entity that is already in World..."); // TODO use in debug mode
		}
	}

	public void removeEntity(Entity e) {
		if (e == null) {
			Err.error("Trying to remove null Entity from World!");
		} else if (entities.contains(e)) {
			entities.remove(e);
		} else {
			System.out.println("Trying to remove an Entity that isn't in World..."); // TODO use in debug mode
		}
	}

	public void addTickable(Tick t) {
		if (t == null) {
			Err.error("Trying to add null Tick to World!");
		} else if (!tickables.contains(t)) {
			tickables.add(t);
		} else {
			System.out.println("Trying to add a Tick that is already in World..."); // TODO use in debug mode
		}
	}

	public void removeTickable(Tick t) {
		if (t == null) {
			Err.error("Trying to remove null Tick from World!");
		} else if (tickables.contains(t)) {
			tickables.add(t);
		} else {
			System.out.println("Trying to remove a Tick that isn't in World..."); // TODO use in debug mode
		}
	}

	public void addSavable(Savable s) {
		if (s == null) {
			Err.error("Trying to add null Savable to World!");
		} else if (!savables.contains(s)) {
			savables.add(s);
		} else {
			System.out.println("Trying to add a Savable already in World..."); // TODO use in debug mode
		}
	}

	public void removeSavable(Savable s) {
		if (s == null) {
			Err.error("Trying to remove null Savable from World!");
		} else if (savables.contains(s)) {
			savables.remove(s);
		} else {
			System.out.println("Trying to remove a Savable that isn't in World..."); // TODO use in debug mode
		}
	}

	public void addBody(Mass m) {
		if (m == null) {
			Err.error("Trying to add null Mass to World!");
		} else if (!bodies.contains(m)) {
			bodies.add(m);
		} else {
			System.out.println("Trying to add a Mass object already in World..."); // TODO use in debug mode
		}
	}

	public void removeBody(Mass m) {
		if (m == null) {
			Err.error("Trying to remove null Mass object from World!");
		} else if (bodies.contains(m)) {
			bodies.remove(m);
		} else {
			System.out.println("Trying to remove a Mass object that isn't in World..."); // TODO use in debug mode
		}
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

	/**
	 * @return all known Enitities
	 */
	public List<Tick> getTickables() {
		return tickables;
	}

	/**
	 * @return all known Enitities
	 */
	public List<Savable> getSavables() {
		return savables;
	}

	/**
	 * @return all known Enitities
	 */
	public List<Mass> getBodies() {
		return bodies;
	}
}
