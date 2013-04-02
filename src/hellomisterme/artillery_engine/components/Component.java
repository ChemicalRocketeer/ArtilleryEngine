package hellomisterme.artillery_engine.components;

import hellomisterme.artillery_engine.Entity;
import hellomisterme.artillery_engine.io.Savable;

public abstract class Component implements Savable {

	public Entity entity;

	/**
	 * Call this in init() to require another Component for this Component to work
	 * 
	 * @param c
	 */
	public void require(Class<? extends Component> c) {
		if (entity.getComponent(c) == null) {
			entity.addComponent(c);
		}
	}

	public void init() {
	}

	/**
	 * If a Component is mutually exclusive, then an Entity can only hold one of that type of Component. By default all Components are set to false,
	 * but if you want to make a Component mutually exclusive, simply override this method to return true.
	 * 
	 * @return true
	 */
	public static boolean isMutuallyExclusive() {
		return false;
	}
}
