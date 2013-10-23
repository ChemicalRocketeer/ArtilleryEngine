package hellomisterme.artillery_engine;

import hellomisterme.artillery_engine.components.Component;
import hellomisterme.artillery_engine.components.physics.FreeBody;
import hellomisterme.artillery_engine.io.ArteReader;
import hellomisterme.artillery_engine.io.ArteWriter;
import hellomisterme.artillery_engine.io.Savable;
import hellomisterme.artillery_engine.rendering.Render;
import hellomisterme.artillery_engine.rendering.Renderable;
import hellomisterme.artillery_engine.util.Transform;
import hellomisterme.artillery_engine.util.Vector;

import java.io.IOException;
import java.util.Hashtable;

/**
 * An Entity is an object in the World.
 * 
 * @since 10-16-12
 * @author David Aaron Suddjian
 */
public class Entity implements Renderable, Savable, Tick {

	private static long totalIDs = 0;
	private final long id;
	public Transform transform = new Transform();
	private Hashtable<Class<? extends Component>, Component> components = new Hashtable<>();

	public Entity() {
		id = totalIDs;
		totalIDs++;
	}

	public Entity(Component... components) {
		this();
		for (Component c : components) {
			addComponent(c);
		}
		for (Component c : components) {
			c.init();
		}
	}

	public FreeBody getFreeBody() {
		return (FreeBody) getComponent(FreeBody.class);
	}

	/**
	 * Tests whether this Entity contains the exact Component c
	 */
	public boolean hasComponent(Component c) {
		return components.containsValue(c);
	}

	public boolean hasComponent(Class<? extends Component> c) {
		return components.containsKey(c);
	}

	public Component getComponent(Class<? extends Component> c) {
		return components.get(c);
	}

	public void addComponent(Component c) {
		components.put(c.getClass(), c);
		c.entity = this;
	}

	public void removeComponent(Component c) {
		components.remove(c.getClass());
		c.entity = null;
	}

	public void addComponent(Class<? extends Component> c) {
		try {
			addComponent(c.newInstance());
		} catch (InstantiationException | IllegalAccessException e) {
			Err.error("Can't add component to entity by class name", e);
		}
	}

	protected void init() {
		for (Component c : components.values()) {
			c.init();
		}
	}

	public Transform globalTransform() {
		return transform.clone();
	}

	public Vector globalPosition() {
		return transform.position.clone();
	}

	public double globalRotation() {
		return transform.rotation;
	}

	public Vector globalScale() {
		return transform.scale.clone();
	}

	@Override
	public void tick() {
		for (Component c : components.values()) {
			if (c instanceof Tick) {
				((Tick) c).tick();
			}
		}
	}

	@Override
	public void render(Render render) {
		for (Component c : components.values()) {
			if (c instanceof Renderable) {
				((Renderable) c).render(render);
			}
		}
	}

	@Override
	public void devmodeRender(Render render) {
		for (Component c : components.values()) {
			if (c instanceof Renderable) {
				((Renderable) c).devmodeRender(render);
			}
		}
	}

	/** @return the total number of entities created over the course of this game */
	public static long getTotalEntities() {
		return totalIDs;
	}

	public long getID() {
		return id;
	}

	public static World getWorld() {
		return Game.getWorld();
	}

	@Override
	public void write(ArteWriter out) {
		try {
			out.write(transform);
			out.write((Savable[]) components.values().toArray());
		} catch (IOException e) {
			Err.error("Can't write entity data!", e);
		}
	}

	@Override
	public void writeOncePerClass(ArteWriter out) {
		try {
			out.write(totalIDs);
		} catch (IOException e) {
			Err.error("Can't write entity data!", e);
		}
	}

	@Override
	public void read(ArteReader in) {
	}

	@Override
	public void readOncePerClass(ArteReader in) {
	}
}
