package hellomisterme.artillery_engine;

import hellomisterme.artillery_engine.components.Camera;
import hellomisterme.artillery_engine.components.Component;
import hellomisterme.artillery_engine.components.imagery.ArtImage;
import hellomisterme.artillery_engine.components.physics.FreeBody;
import hellomisterme.artillery_engine.components.scripts.Planet;
import hellomisterme.artillery_engine.components.scripts.PlayerMovement;
import hellomisterme.artillery_engine.io.Keyboard;
import hellomisterme.artillery_engine.io.Savable;
import hellomisterme.artillery_engine.rendering.Render;
import hellomisterme.artillery_engine.rendering.Renderable;
import hellomisterme.util.Vector2;

import java.awt.Dimension;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * World keeps track of objects in the game.
 * 
 * @since 10-23-12
 * @author David Aaron Suddjian
 */
public class World implements Tick, Savable, Renderable {

	private String name = "New Game";
	private Dimension bounds;

	private Map<String, Entity> entities = new Hashtable<String, Entity>();
	public List<FreeBody> freebodies = new LinkedList<FreeBody>();
	public List<Renderable> images = new LinkedList<Renderable>();

	private boolean baddieOrdered = false;

	public Camera activeCamera = new Camera();

	public World(int w, int h) {
		bounds = new Dimension(w, h);
	}

	public World(DataInputStream in) {
		read(in);
	}

	public void init() {
		ArtImage playerImage = new ArtImage("graphics/sprites/player.png");
		playerImage.transform.rotation = Math.PI * 0.5;
		playerImage.transform.position = new Vector2(playerImage.getWidth() * -0.5, playerImage.getHeight() * -0.5);
		playerImage.transform.scale = new Vector2(1, 1);
		Camera camera = new Camera(true, true, true);
		// camera.transform.scale = new Vector2(0.5, 0.5);
		camera.transform.rotation = Math.PI * 0.5;
		activeCamera = camera;
		Entity player = new Entity(new Component[] { new PlayerMovement(), new FreeBody(), playerImage, camera });
		player.transform.position = new Vector2(getWidth() * 0.2, getHeight() * 0.2);
		player.transform.rotation = -Math.PI * 0.5;
		addEntity("player", player);

		FreeBody planetBody = new FreeBody(new Vector2(0, 0), 1000, 0);
		Entity planet = new Entity(new Component[] { new Planet(), planetBody });
		planet.transform.position = new Vector2(getWidth() * 0.8, getHeight() * 0.8);
		addEntity("planet", planet);
	}

	@Override
	public void tick() {
		// tick all the registered Tick objects
		try {
			for (Map.Entry<String, Entity> e : entities.entrySet()) {
				e.getValue().tick();
			}
		} catch (NullPointerException e) {
			Err.error("Null Pointer in a tick() method!");
			e.printStackTrace();
		}

		// if the addbaddie key is pressed
		if (Keyboard.Controls.ADDBADDIE.pressed()) {
			if (baddieOrdered == false) { // if the key was up before
				// add baddie here
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
	public void render(Render render) {
		for (Map.Entry<String, Entity> e : entities.entrySet()) {
			e.getValue().render(render);
		}
	}

	public Entity getEntity(String key) {
		return entities.get(key);
	}

	public void addEntity(String key, Entity e) {
		entities.put(key, e);
		FreeBody body = e.getFreeBody();
		if (body != null) freebodies.add(body);
	}

	public void removeEntity(String key) {
		freebodies.remove(entities.get(key).getFreeBody());
		entities.remove(key);
	}

	public void removeEntity(Entity e) {
		freebodies.remove(e);
		entities.values().remove(e.getFreeBody());
	}

	public int entityCount() {
		return entities.size();
	}

	public int getWidth() {
		return bounds.width;
	}

	public void setWidth(int width) {
		bounds.width = width;
	}

	public int getHeight() {
		return bounds.height;
	}

	public void setHeight(int height) {
		bounds.height = height;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void write(DataOutputStream out) {
		try {
			out.writeUTF(name);
			// the first int saved is width, second is height
			out.writeInt(bounds.width);
			out.writeInt(bounds.height);
			out.writeInt(entities.size()); // write how many savables there are
		} catch (IOException e) {
			Err.error("World can't write data!");
		}

		// save all the entity data
		for (Map.Entry<String, Entity> entry : entities.entrySet()) {
			try {
				out.writeUTF(entry.getKey()); // write the key, then write the entity data
			} catch (IOException e) {
				Err.error("Can't save entity name " + entry.getKey() + "!");
				e.printStackTrace();
			}
			entry.getValue().write(out);
		}
	}

	@Override
	public void read(DataInputStream in) {
		entities.clear();
		freebodies.clear();
		try {
			name = in.readUTF();
			bounds.width = in.readInt();
			bounds.height = in.readInt();

			for (int i = in.readInt(); i > 0; i--) {
				String key = in.readUTF(); // read the key, then read the entity
				Entity e = new Entity();
				e.read(in);
				addEntity(key, e);
			}
		} catch (Exception e) {
			Err.error("World can't load saved data! Please send the world save in a bug report, or something. Make yourself useful.");
			e.printStackTrace();
		}
	}

	@Override
	public void writeOncePerClass(DataOutputStream out) {
	}

	@Override
	public void readOncePerClass(DataInputStream in) {
	}
}
