package hellomisterme.artillery_engine.game.components.scripts;

import hellomisterme.artillery_engine.Tick;
import hellomisterme.artillery_engine.game.components.Component;

import java.io.DataInputStream;
import java.io.DataOutputStream;

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
	public void write(DataOutputStream out) {
	}

	@Override
	public void writeStatic(DataOutputStream out) {
	}

	@Override
	public void read(DataInputStream in) {
	}

	@Override
	public void readStatic(DataInputStream in) {
	}
}
