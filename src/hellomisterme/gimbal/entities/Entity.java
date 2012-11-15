package hellomisterme.gimbal.entities;

import hellomisterme.gimbal.Err;
import hellomisterme.gimbal.graphics.GimbalImage;
import hellomisterme.gimbal.graphics.LightweightImage;
import hellomisterme.gimbal.io.Savable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * An Entity contains a LightweightImage and x and y coordinates. Subclasses can be rendered.
 * 
 * @since 10-16-12
 * @author David Aaron Suddjian
 */
public abstract class Entity implements Savable {

	private int x = 0, y = 0;
	protected GimbalImage image;
	protected EntityBucket bucket;

	/**
	 * Saves this Entity's x and y coordinates, then if the LightweightImage isn't null, writes a true and calls the LightweightImage's save() method. Else writes a false.
	 */
	public void save(DataOutputStream out) {
		try {
			out.writeInt(x);
			out.writeInt(y);
			saveData(out);
			if (image != null) {
				out.writeBoolean(true);
				image.save(out);
			} else {
				out.writeBoolean(false);
			}
		} catch (IOException e) {
			Err.error("Can't save Entity data!");
			e.printStackTrace();
		}
	}
	
	/**
	 * Saves this Entity's data.
	 */
	public void saveData(DataOutputStream out) {
		
	}

	/**
	 * Calls loadData(), then if it reads a true, calls the loadImage().
	 */
	public void load(DataInputStream in) {
		try {
			x = in.readInt();
			y = in.readInt();
			loadData(in);
			if (in.readBoolean()) {
				loadImage(in);
			}
		} catch (IOException e) {
			Err.error("Can't read Entity data!");
			e.printStackTrace();
		}
	}
	
	/**
	 * Loads this Entity's saved data.
	 */
	public void loadData(DataInputStream in) {
		
	}

	/**
	 * Loads saved image data. Can be overridden to load other types of images.
	 */
	public void loadImage(DataInputStream in) {
		image = new LightweightImage();
		image.load(in);
	}

	public void setEntityBucket(EntityBucket h) {
		bucket = h;
	}

	/**
	 * Returns the LightweightImage. Can return null.
	 * 
	 * @return the LightweightImage associated with this Entity
	 */
	public GimbalImage getImage() {
		if (image == null) {
			System.out.println("Returning null LightweightImage from Entity"); // TODO remove/change
		}
		return image;
	}

	protected void setImage(GimbalImage img) {
		image = img;
	}

	protected void setImage(String path) {
		image = new LightweightImage(path);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setPos(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
