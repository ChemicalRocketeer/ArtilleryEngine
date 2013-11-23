package hellomisterme.artillery_engine;

import hellomisterme.artillery_engine.behaviors.Behavior;
import hellomisterme.artillery_engine.behaviors.Collision;
import hellomisterme.artillery_engine.behaviors.Gravity;
import hellomisterme.artillery_engine.components.Camera;
import hellomisterme.artillery_engine.components.physics.FreeBody;
import hellomisterme.artillery_engine.geometry.Circle;
import hellomisterme.artillery_engine.io.ArteReader;
import hellomisterme.artillery_engine.io.ArteWriter;
import hellomisterme.artillery_engine.io.Keyboard;
import hellomisterme.artillery_engine.io.Savable;
import hellomisterme.artillery_engine.rendering.Render;
import hellomisterme.artillery_engine.rendering.Renderable;
import hellomisterme.artillery_engine.util.Vector;

import java.awt.Dimension;
import java.util.HashSet;
import java.util.Set;

/**
 * World keeps track of objects in the game.
 * 
 * @since 10-23-12
 * @author David Aaron Suddjian
 */
public class World implements Tick, Savable, Renderable {
	
	private String name = "New Game";
	private Dimension bounds;
	
	private Set<Entity> entities = new HashSet<>();
	private Set<Behavior> behaviors = new HashSet<>();
	
	private Camera camera = new Camera();
	private Entity player;
	
	private boolean baddieOrdered = false;

	public World(int w, int h) {
		bounds = new Dimension(w, h);
	}
	
	public void init() {
		/*
		 * AdvancedImage playerImage = new AdvancedImage("graphics/sprites/player.png");
		 * playerImage.transform.rotation = Math.PI * 0.5;
		 * playerImage.transform.position = new Vector(playerImage.getWidth() * -0.5, playerImage.getHeight() * -0.5);
		 * playerImage.transform.scale = new Vector(0.5, 0.5);
		 * player = new Entity(new PlayerMovement(), playerImage);
		 * player.transform.position = new Vector(0, 0);
		 * player.transform.scale = new Vector(2, 2);
		 * // player.transform.rotation = -Math.PI * 0.5;
		 * addEntity(player);
		 */
		
		Camera cam = new Camera();
		// camera.transform.scale = new Vector(0.5, 0.5);
		camera = cam;
		addEntity(new Entity(cam));

		/*
		 * Entity planet = new Entity(new Component[] { new Planet() });
		 * // addEntity("planet", planet);
		 * planet.transform.position = new Vector(500, 0);
		 * planet.getFreeBody().mass = 20;
		 */
		
		Entity circle1 = new Entity(Circle.fromArea(3000), FreeBody.create(new Vector(0, 0.1), 3, 0));
		circle1.transform.position.x = -120;
		circle1.transform.position.y = 30;
		addEntity(circle1);
		
		Entity circle2 = new Entity(Circle.fromArea(3000), FreeBody.create(new Vector(0, -0.1), 3, 0));
		circle2.transform.position.x = 0;
		circle2.transform.position.y = 0;
		addEntity(circle2);
		
		/*
		Entity circle1 = new Entity(new Component[] { CircleHitbox.createFromMass(2.7299), FreeBody.create(new Vector(-0.9842, -0.0256), 2.72999, 0) });
		circle1.transform.position.x = -39.619;
		circle1.transform.position.y = -202.71;
		addEntity(circle1);
		
		Entity circle2 = new Entity(new Component[] { CircleHitbox.createFromMass(1.3757), FreeBody.create(new Vector(-1.1006, 0.1969), 1.3757, 0) });
		circle2.transform.position.x = 102.91;
		circle2.transform.position.y = -199.04;
		addEntity(circle2);
		*/
		
		behaviors.add(new Gravity());
		behaviors.add(new Collision());
		
		/*
		Entity planet2 = new Entity(new Component[] { new Planet() });
		addEntity("planet2", planet2);
		planet2.transform.position = new Vector(-50, -140);
		planet2.getFreeBody().mass = 100;
		 */
	}
	
	@Override
	public void tick() {
		for (Entity e : entities)
			e.tick();
		
		for (Behavior b : behaviors)
			b.run();
		
		// if the addbaddie key is pressed
		if (Keyboard.Controls.ADDBADDIE.pressed()) {
			if (baddieOrdered == false) { // if the key was up before
				addBaddie();
				baddieOrdered = true; // remember that the key was pressed
			}
		} else { // key not pressed
			baddieOrdered = false;
		}
	}
	
	public void addBaddie() {
		double circleMass = Game.RAND.nextDouble() * 3;
		Entity circle = new Entity(Circle.fromArea(circleMass * 1000), FreeBody.create(new Vector(Game.RAND.nextDouble() * 2 - 1, Game.RAND.nextDouble() * 2 - 1), circleMass, 0));
		circle.transform.position.x = Game.RAND.nextDouble() * 2000 - 1000 + camera.globalPosition().x;
		circle.transform.position.y = Game.RAND.nextDouble() * 1000 - 500 + camera.globalPosition().y;
		addEntity(circle);
		for (Behavior b : behaviors)
			b.addEntity(circle);
	}

	@Override
	public void render(Render render) {
		for (Entity e : entities) {
			e.render(render);
		}
	}
	
	@Override
	public void devmodeRender(Render render) {
		for (Entity e : entities) {
			e.devmodeRender(render);
		}
	}
	
	public void addEntity(Entity e) {
		entities.add(e);
	}
	
	public Entity getEntity(int entityID) {
		for (Entity e : entities) {
			if (e.getID() == entityID)
				return e;
		}
		return null;
	}
	
	public void removeEntity(Entity e) {
		entities.remove(e);
		for (Behavior s : behaviors) {
			s.addEntity(e);
		}
	}
	
	public int entityCount() {
		return entities.size();
	}
	
	public Entity getPlayer() {
		return player;
	}
	
	public Camera getCamera() {
		return camera;
	}
	
	public void setCamera(Camera camera) {
		this.camera = camera;
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
