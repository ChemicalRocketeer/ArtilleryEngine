package hellomisterme.artillery_engine;

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
	public static final String PREPEND = "\nError: ";
	
	public static void error(String message) {
		System.out.println(PREPEND + message + APPEND);
	}
}
