package hellomisterme.gimbal.entities;

import hellomisterme.gimbal.Err;
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
	protected LightweightImage image = new LightweightImage();
	protected EntityBucket bucket;

	public void save(DataOutputStream out) {
		try {
			out.writeInt(x);
			out.writeInt(y);
		} catch (IOException e) {
			Err.error("Can't save Entity data!");
			e.printStackTrace();
		}
		image.save(out);
	}

	public void load(DataInputStream in) {
		try {
			x = in.readInt();
			y = in.readInt();
		} catch (IOException e) {
			Err.error("Can't read Entity data!");
			e.printStackTrace();
		}
		image.load(in);
	}

	public void setEntityBucket(EntityBucket h) {
		bucket = h;
	}

	public LightweightImage getImage() {
		return image;
	}

	public void setImage(LightweightImage img) {
		image = img;
	}

	public void setImage(String path) {
		image.setImage(path);
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
