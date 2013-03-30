package hellomisterme.artillery_engine.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * An object implementing Savable will have save and load methods in order to save and load its data from a location.
 * 
 * @since 11-4-12
 * @author David Aaron Suddjian
 */
public interface Savable {

	public void write(DataOutputStream out);

	public void writeOncePerClass(DataOutputStream out);

	public void read(DataInputStream in);

	public void readOncePerClass(DataInputStream in);

}
