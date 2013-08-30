package hellomisterme.artillery_engine.io;

/** A type code, used to show what type of data comes next in a file */
public enum SavefileTypecode {
	// even though enums have int values, the values written are bytes
	SAVABLE, CHAR, BOOLEAN, BYTE, SHORT, INT, LONG, FLOAT, DOUBLE, STRING, VECTOR, TRANSFORM, ARRAY
}
