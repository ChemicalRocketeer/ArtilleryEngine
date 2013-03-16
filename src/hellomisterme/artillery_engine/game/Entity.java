package hellomisterme.artillery_engine.game;

import hellomisterme.artillery_engine.Game;
import hellomisterme.artillery_engine.Tick;
import hellomisterme.artillery_engine.World;
import hellomisterme.artillery_engine.game.components.Component;
import hellomisterme.artillery_engine.game.components.PhysicsForces;
import hellomisterme.artillery_engine.game.components.rendering.BasicImage;
import hellomisterme.artillery_engine.graphics.Render;
import hellomisterme.artillery_engine.graphics.Renderable;
import hellomisterme.artillery_engine.io.Savable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * An Entity is an object in the World.
 * 
 * @since 10-16-12
 * @author David Aaron Suddjian
 */
public class Entity implements Renderable, Savable, Tick {

	private List<Component> components = new LinkedList<Component>();
	public Transform transform = new Transform();

	public Entity(Component[] components) {
		for (Component c : components) {
			this.components.add(c);
		}
	}

	public Entity() {
	}

	/**
	 * Returns the BasicImage. Can return null.
	 * 
	 * @return the BasicImage associated with this Entity
	 */
	public BasicImage getImage() {
		return (BasicImage) getComponent(BasicImage.class);
	}

	public PhysicsForces getForces() {
		return (PhysicsForces) getComponent(PhysicsForces.class);
	}

	public Component getComponent(Class<? extends Component> c) {
		for (Component comp : components) {
			if (comp.getClass().equals(c)) {
				return comp;
			}
		}
		return null;
	}

	public List<Component> getComponents(Class<? extends Component> c) {
		List<Component> list = new LinkedList<Component>();
		for (Component comp : components) {
			if (comp.getClass().equals(c)) {
				list.add(comp);
			}
		}
		return list;
	}

	public void addComponent(Component c) {
		components.add(c);
		c.entity = this;
		c.init();
	}

	public void removeComponent(Component c) {
		components.remove(c);
		c.entity = null;
	}

	/**
	 * Renders all of this Entity's images
	 */
	@Override
	public void render(Render render) {
		for (Component i : getComponents(BasicImage.class)) {
			((BasicImage) i).render(render);
		}
	}

	/**
	 * If this Entity is outside the world bounds, sets the x and y positions to be exactly at the edge of the world bounds.
	 */
	public void correctOOB() {
		World w = getWorld();
		BasicImage img = getImage();
		if (transform.position.x >= w.getWidth() - img.getWidth()) { // right
			transform.position.x = w.getWidth() - img.getWidth() - 1;
		} else if (transform.position.x < 0) { // left
			transform.position.x = 0;
		}
		if (transform.position.y >= w.getHeight() - img.getHeight()) { // bottom
			transform.position.y = w.getHeight() - img.getHeight() - 1;
		} else if (transform.position.y < 0) { // top
			transform.position.y = 0;
		}
	}

	public static World getWorld() {
		return Game.getWorld();
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
	public void write(DataOutputStream out) {
	}

	@Override
	public void read(DataInputStream in) {
	}

	@Override
	public void writeStatic(DataOutputStream out) {
	}

	@Override
	public void readStatic(DataInputStream in) {
	}
}
