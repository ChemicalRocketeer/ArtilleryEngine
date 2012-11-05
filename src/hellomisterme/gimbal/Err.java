package hellomisterme.gimbal;

/**
 * Err handles printing of error messages to the console
 * 
 * TODO remove error messages throughout game
 * 
 * @since 10-23-12
 * @author David Aaron Suddjian
 */
public class Err {
	
	// TODO replace email
	public static final String APPEND = "\nPlease contact aasuddjian@gmail.com with this error message.\n";
	public static final String PREPEND = "Error: ";
	
	public static String error(String message) {
		return PREPEND + message + APPEND;
	}
}
