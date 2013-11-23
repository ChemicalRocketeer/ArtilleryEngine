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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * An Entity is an object in the World, with unique ID, a Transform and Components.
 * 
 * @since 10-16-12
 * @author David Aaron Suddjian
 */
public class Entity implements Renderable, Savable, Tick {

	private static long totalIDs = 0;
	private final long id;
	public Transform transform = new Transform();
	private List<Component> components = new LinkedList<>(); // has to be a List so Components can require other Components

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
		for (Component comp : components)
			if (comp == c)
				return true;
		return false;
	}

	public boolean hasComponent(Class<? extends Component> cls) {
		for (Component comp : components)
			if (comp.getClass().isAssignableFrom(cls))
				return true;
		return false;
	}

	public void addComponent(Component c) {
		if (c != null) {
			if (c.isMutuallyExclusive() && this.hasComponent(c.getClass()))
				return;

			components.add(c);
			c.entity = this;
		}
	}

	public void addComponent(Class<? extends Component> cls) {
		if (cls != null) {
			try {
				addComponent(cls.newInstance());
			} catch (Exception e) {
				Err.error("Can't create new instance of component " + cls.getCanonicalName(), e);
			}
		}
	}

	public void removeComponent(Component c) {
		components.remove(c);
		c.entity = null;
	}

	public void removeComponent(Class<? extends Component> cls) {
		if (cls != null) {
			Iterator<Component> it = components.iterator();
			while (it.hasNext()) {
				Component comp = it.next();
				if (cls.isInstance(comp))
					it.remove();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public <T extends Component> T getComponent(Class<T> cls) {
		if (cls != null) {
			for (Component comp : components)
				if (cls.isInstance(comp))
					return (T) comp;
		}
		return null;

	}

	protected void init() {
		for (Component c : components) {
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
		for (Component c : components) {
			if (c instanceof Tick) {
				((Tick) c).tick();
			}
		}
	}

	@Override
	public void render(Render render) {
		for (Component c : components) {
			if (c instanceof Renderable) {
				((Renderable) c).render(render);
			}
		}
	}

	@Override
	public void devmodeRender(Render render) {
		for (Component c : components) {
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
			out.write((Savable[]) components.toArray());
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
