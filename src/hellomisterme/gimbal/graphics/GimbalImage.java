package hellomisterme.gimbal.graphics;

import hellomisterme.gimbal.io.Savable;

/**
 * A basic class to describe an image.
 * 
 * @since 11-12-12
 * @author David Aaron Suddjian
 */
public interface GimbalImage extends Savable {
	
	public int[] getPixels();
	public void setImage(String path);
	public int getWidth();
	public int getHeight();
}
