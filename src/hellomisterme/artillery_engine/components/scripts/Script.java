package hellomisterme.artillery_engine.components.scripts;

import hellomisterme.artillery_engine.Tick;
import hellomisterme.artillery_engine.components.Component;
import hellomisterme.artillery_engine.io.ArteReader;
import hellomisterme.artillery_engine.io.ArteWriter;

/**
 * A script dictates Entity behavior
 * 
 * @since 2013-3-13
 * @author David Aaron Suddjian
 */
public abstract class Script extends Component implements Tick {

	@Override
	public void tick() {
	}

	@Override
	public void write(ArteWriter out) {
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
