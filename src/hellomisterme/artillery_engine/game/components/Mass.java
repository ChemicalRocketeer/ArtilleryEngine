package hellomisterme.artillery_engine.game.components;

import hellomisterme.artillery_engine.Err;
import hellomisterme.util.Vector2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


public class Mass extends Component {

	public double mass = 1;
	public Vector2 center = new Vector2(0, 0);

	public Mass() {
		this(1, new Vector2(0, 0));
	}

	public Mass(double mass, Vector2 center) {
		this.mass = mass;
		this.center = center;
	}

	@Override
	public void write(DataOutputStream out) {
		try {
			out.writeDouble(mass);
		} catch (IOException e) {
			Err.error("Can't save Mass data!");
			e.printStackTrace();
		}
		center.write(out);
	}

	@Override
	public void writeStatic(DataOutputStream out) {
	}

	@Override
	public void read(DataInputStream in) {
		try {
			mass = in.readDouble();
		} catch (IOException e) {
			Err.error("Can't read Mass data!");
			e.printStackTrace();
		}
		center.read(in);
	}

	@Override
	public void readStatic(DataInputStream in) {
	}
}
