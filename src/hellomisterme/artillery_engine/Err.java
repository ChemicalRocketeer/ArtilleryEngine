package hellomisterme.artillery_engine;

import java.lang.reflect.InvocationTargetException;

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
		System.err.println(PREPEND + message + APPEND);
	}

	public static void error(String message, Exception e) {
		System.err.println(PREPEND + message + APPEND + "\n\n" + e.getLocalizedMessage());
		if (e instanceof InvocationTargetException) {
			System.err.println("InvocationTargetException:");
			e.getCause().printStackTrace();
		} else {
			e.printStackTrace();
		}
	}
}
