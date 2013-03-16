package hellomisterme.artillery_engine.game.components;

import hellomisterme.artillery_engine.game.Entity;
import hellomisterme.artillery_engine.io.Savable;

import java.util.LinkedList;
import java.util.List;

public abstract class Component implements Savable {

	public Entity entity;
	private static List<Class<? extends Component>> requiredComponents = new LinkedList<Class<? extends Component>>();

	/**
	 * Override to require a component before this component can be used
	 * 
	 * @return a list of all the components necessary
	 */
	public static boolean requiresComponent(Component c) {
		return requiredComponents.contains(c);
	}

	/**
	 * Call this in the constructor to require another Component for this Component to work
	 * 
	 * @param c
	 */
	public static void require(Class<? extends Component> c) {
		if (!requiredComponents.contains(c)) requiredComponents.add(c);
	}

	public void init() {
	}
}
