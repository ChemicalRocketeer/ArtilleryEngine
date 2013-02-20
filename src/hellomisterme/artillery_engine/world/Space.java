package hellomisterme.artillery_engine.world;

import hellomisterme.artillery_engine.entities.Mass;

import java.util.ArrayList;

public class Space extends World {

	private ArrayList<Mass> bodies;

	public Space() {
		bodies = new ArrayList<Mass>();
		makePlayer();
	}

	@Override
	public void add(Object o) {
		super.add(o);
		if (o instanceof Mass) {
			bodies.add((Mass) o);
		}
	}

	@Override
	public void remove(Object o) {
		super.remove(o);
		if (o instanceof Mass) {
			bodies.remove((Mass) o);
		}
	}
}
