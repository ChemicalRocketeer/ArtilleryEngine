package hellomisterme.artillery_engine;

import hellomisterme.artillery_engine.components.Component;
import hellomisterme.artillery_engine.components.physics.FreeBody;
import hellomisterme.artillery_engine.components.sprites.BasicImage;
import hellomisterme.artillery_engine.graphics.Render;
import hellomisterme.artillery_engine.graphics.Renderable;
import hellomisterme.artillery_engine.io.Savable;
import hellomisterme.util.Transform;
import hellomisterme.util.Vector2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
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
	public Entity parent;

	public Entity(Component[] components) {
		for (Component c : components) {
			addComponent(c);
		}
		for (Component c : components) {
			c.init();
		}
	}

	public Entity() {
	}

	/**
	 * Returns the BasicImage. Can return null.
	 * 
	 * @return the BasicImage associated with this Entity
	 */
	public BasicImage getBasicImage() {
		return (BasicImage) getComponent(BasicImage.class);
	}

	public FreeBody getFreeBody() {
		return (FreeBody) getComponent(FreeBody.class);
	}

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

	/**
	 * @return a clone of all the position vectors affecting this Entity. Don't worry about modifying their values.
	 */
	public Vector2 globalPosition() {
		if (parent == null) return transform.position.clone();

		Vector2 pos = parent.globalPosition();
		pos.add(transform.position);
		return pos;
	}

	/**
	 * @return a clone of all the scale vectors affecting this Entity. Don't worry about modifying their values.
	 */
	public Vector2 globalScale() {
		if (parent == null) return transform.scale.clone();

		Vector2 scale = parent.globalScale();
		scale.mul(transform.scale);
		return scale;
	}

	/**
	 * @return the combined value of all rotation values affecting this Entity.
	 */
	public double globalRotation() {
		if (parent == null) return transform.rotation;
		return parent.globalRotation() + transform.rotation;
	}

	/**
	 * @return a clone of all the Transforms affecting this Entity. Don't worry about modifying their values.
	 */
	public Transform globalTransform() {
		if (parent == null) return transform.clone();

		Transform trans = parent.globalTransform();
		trans.add(transform);
		return trans;
	}

	@Override
	public void write(DataOutputStream out) {
		transform.write(out);
		try {
			out.writeInt(components.size());
			for (Component c : components) {
				c.write(out);
			}
		} catch (IOException e) {
			Err.error("Can't save entity data!", e);
		}
	}

	@Override
	public void read(DataInputStream in) {
		transform.read(in);
		int compCount = 0;
		components.clear();
		try {
			compCount = in.readInt();
			for (int i = 0; i < compCount; i++) {
				Component c = (Component) Class.forName(in.readUTF()).newInstance(); // create an object based on the class name read
				components.add(c);
				c.read(in);
			}
		} catch (IOException e) {
			Err.error("Can't read entity data!", e);
		} catch (ClassNotFoundException e) {
			Err.error("Can't read entity data! Class not found!", e);
		} catch (IllegalAccessException e) {
			Err.error("Can't read entity data! Illegal Access!", e);
		} catch (InstantiationException e) {
			Err.error("Can't read entity data! Class cannot be instantiated!", e);
		}
		for (Component c : components) {
			c.init();
		}
	}

	@Override
	public void writeOncePerClass(DataOutputStream out) {
	}

	@Override
	public void readOncePerClass(DataInputStream in) {
	}

	public static World getWorld() {
		return Game.getWorld();
	}
}
