package hellomisterme.artillery_engine;

import hellomisterme.artillery_engine.components.Component;
import hellomisterme.artillery_engine.components.physics.FreeBody;
import hellomisterme.artillery_engine.io.ArteReader;
import hellomisterme.artillery_engine.io.ArteWriter;
import hellomisterme.artillery_engine.io.Savable;
import hellomisterme.artillery_engine.rendering.Render;
import hellomisterme.artillery_engine.rendering.Renderable;
import hellomisterme.util.Transform;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

/**
 * An Entity is an object in the World.
 * 
 * @since 10-16-12
 * @author David Aaron Suddjian
 */
public class Entity implements Renderable, Savable, Tick {
	
	private Collection<Component> components = new LinkedList<Component>();
	public Transform transform = new Transform();
	
	public Entity() {
	}

	public Entity(Component[] components) {
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
		return components.contains(c);
	}
	
	public boolean hasComponent(Class<? extends Component> c) {
		for (Component comp : components) {
			if (c.isInstance(comp)) return true;
		}
		return false;
	}
	
	public Component getComponent(Class<? extends Component> c) {
		for (Component comp : components) {
			if (c.isInstance(comp)) return comp;
		}
		return null;
	}
	
	/**
	 * Returns a Collection of all components of class c
	 * 
	 * @param c the Class which all returned Components will match
	 * @return a list of Components that all descend from c
	 */
	public Collection<Component> getComponents(Class<? extends Component> c) {
		Collection<Component> list = new LinkedList<Component>();
		for (Component comp : components) {
			if (c.isInstance(comp)) list.add(comp);
		}
		return list;
	}
	
	public void addComponent(Component c) {
		components.add(c);
		c.entity = this;
	}
	
	public void removeComponent(Component c) {
		components.remove(c);
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
		for (Component c : components) {
			c.init();
		}
	}
	
	@Override
	public void tick() {
		for (Component c : components) {
			if (c instanceof Tick) {
				((Tick) c).tick();
			}
		}
	}
	
	/**
	 * Renders all of this Entity's basicImages
	 */
	@Override
	public void render(Render render) {
		for (Component c : components) {
			if (c instanceof Renderable) {
				((Renderable) c).render(render);
			}
		}
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
	}
	
	@Override
	public void read(ArteReader in) {
	}
	
	@Override
	public void readOncePerClass(ArteReader in) {
	}
}
