package possiblydavid.gimbal;

/**
 * Err handles printing of error messages to the console
 * 
 * TODO remove error messages throughout game
 * 
 * @author David Aaron Suddjian
 */
public class Err {
	
	public static final String[] ERRORS = new String[] {
		"Trying to add null Entity to World!",
		"Trying to add null Ticker to World!",
		"LightweightImage can't read the file!",
		"Can't render a null entity!",
		"null call to tick method!"
	};
	
	// TODO replace email
	public static final String APPEND = "\nPlease contact aasuddjian@gmail.com with this error message.\n";
	
	/**
	 * 
	 * @param id the ID number of the error to print
	 */
	public static void error(int id) {
		if (id >= 0 && id < ERRORS.length) {
			System.out.println("\nERROR: " + ERRORS[id] + APPEND);
		} else {
			System.out.println("\nUnknown error!" + APPEND);
		}
	}
}
