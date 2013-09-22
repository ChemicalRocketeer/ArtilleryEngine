package hellomisterme.artillery_engine;

import hellomisterme.artillery_engine.components.Camera;
import hellomisterme.artillery_engine.components.Component;
import hellomisterme.artillery_engine.components.images.AdvancedImage;
import hellomisterme.artillery_engine.components.physics.FreeBody;
import hellomisterme.artillery_engine.components.scripts.Planet;
import hellomisterme.artillery_engine.components.scripts.PlayerMovement;
import hellomisterme.artillery_engine.io.ArteReader;
import hellomisterme.artillery_engine.io.ArteWriter;
import hellomisterme.artillery_engine.io.Keyboard;
import hellomisterme.artillery_engine.io.Savable;
import hellomisterme.artillery_engine.rendering.Render;
import hellomisterme.artillery_engine.rendering.Renderable;
import hellomisterme.util.Vector2;

import java.awt.Color;
import java.awt.Dimension;
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
	
	private Camera camera = new Camera();
	
	public Camera getCamera() {
		return camera;
	}
	
	public void setCamera(Camera camera) {
		this.camera = camera;
	}
	
	public World(int w, int h) {
		bounds = new Dimension(w, h);
	}
	
	public void init() {
		AdvancedImage playerImage = new AdvancedImage("graphics/sprites/player.png");
		playerImage.transform.rotation = Math.PI * 0.5;
		playerImage.transform.position = new Vector2(playerImage.getWidth() * -0.5, playerImage.getHeight() * -0.5);
		playerImage.transform.scale = new Vector2(1, 1);
		Camera cam = new Camera(true, false, true);
		// camera.transform.scale = new Vector2(0.5, 0.5);
		cam.transform.rotation = 0;
		camera = cam;
		Entity player = new Entity(new Component[] { new PlayerMovement(), playerImage, cam });
		player.transform.position = new Vector2(0, 100);
		player.transform.rotation = -Math.PI * 0.5;
		addEntity("player", player);
		
		Entity planet = new Entity(new Component[] { new Planet() });
		addEntity("planet", planet);
		planet.transform.position = new Vector2(500, 0);
		planet.getFreeBody().mass = 200;
		/*
		Entity planet2 = new Entity(new Component[] { new Planet() });
		addEntity("planet2", planet2);
		planet2.transform.position = new Vector2(-50, -140);
		planet2.getFreeBody().mass = 100;
		 */
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
		Vector2 camPos = camera.globalPosition();
		render.drawArrow(camPos, camPos.ADD(new Vector2(0, 50)), Color.RED);
		render.drawArrow(camPos, camPos.ADD(new Vector2(50, 0)), Color.BLUE);
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
	public void read(ArteReader in) {
		
	}
	
	@Override
	public void write(ArteWriter out) {
		
	}
	
	@Override
	public void readOncePerClass(ArteReader in) {
		
	}
	
	@Override
	public void writeOncePerClass(ArteWriter out) {
		
	}
}
