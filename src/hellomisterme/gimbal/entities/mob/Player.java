package hellomisterme.gimbal.entities.mob;

import hellomisterme.gimbal.entities.Mover;
import hellomisterme.gimbal.input.KeyInput;

/**
 * The Player class. This is the player. Yessiree Bob. Must be contructed with a World object, or it won't work.
 * 
 * @since 10-23-12
 * @author David Aaron Suddjian
 */
public class Player extends Mover {

	private double speed = 2.0;

	public Player() {
		setImage("graphics/sprites/player.png");
	}

	public void tick() {
		// Move if the right button(s) are held down
		if (KeyInput.pressed(KeyInput.up)) {
			setPos(getExactX(), getExactY() - speed * .5);
		}
		if (KeyInput.pressed(KeyInput.down)) {
			setPos(getExactX(), getExactY() + speed * .5);
		}
		if (KeyInput.pressed(KeyInput.left)) {
			setPos(getExactX() - speed, getExactY());
		}
		if (KeyInput.pressed(KeyInput.right)) {
			setPos(getExactX() + speed, getExactY());
		}

		// correct for out of bounds
		if (exactX >= bucket.getWidth() - image.getWidth()) { // right
			setPos(bucket.getWidth() - image.getWidth(), exactY);
		} else if (exactX < 0) { // left
			setPos(0, exactY);
		}
		if (exactY >= bucket.getHeight() - image.getHeight()) { // bottom
			setPos(exactX, bucket.getHeight() - image.getHeight());
		} else if (exactY < 0) { // top
			setPos(exactX, 0);
		}
	}
}
