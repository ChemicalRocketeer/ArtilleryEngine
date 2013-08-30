package hellomisterme.artillery_engine.io;


/**
 * An object implementing Savable will have save and load methods in order to save and load its data from a location.
 * 
 * @since 11-4-12
 * @author David Aaron Suddjian
 */
public interface Savable {

	/**
	 * Reads this Savable's data from the given ArteReader.
	 * 
	 * @param in the ArteReader to read from
	 */
	public void read(ArteReader in);

	/**
	 * Writes this Savable's data to the given ArteWriter.
	 * 
	 * @param out the ArteWriter that will write the data
	 */
	public void write(ArteWriter out);

	/**
	 * Reads class variables that have only been written once. This method should only write static variables.
	 * 
	 * @param in the ArteReader to read from
	 */
	public void readOncePerClass(ArteReader in);

	/**
	 * Writes static class variables that only need to be written once.
	 * 
	 * @param out the ArteWriter that will write the data
	 */
	public void writeOncePerClass(ArteWriter out);
}
