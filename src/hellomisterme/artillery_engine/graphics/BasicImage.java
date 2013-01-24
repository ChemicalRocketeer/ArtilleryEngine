package hellomisterme.artillery_engine.graphics;


/**
 * Methods to interact with image data.
 * 
 * @since 11-12-12
 * @author David Aaron Suddjian
 */
public interface BasicImage {
	
	/**
	 * Returns all the pixels of this BasicImage as ints.
	 * 
	 * @return an int[] of all the pixels
	 */
	public int[] getPixels();
	
	/**
	 * Returns the width value of this BasicImage.
	 * 
	 * @return the width value
	 */
	public int getWidth();
	
	/**
	 * Returns the height value of this BasicImage.
	 * 
	 * @return the height value
	 */
	public int getHeight();
}
