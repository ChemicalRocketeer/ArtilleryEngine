package possiblydavid.gimbal.entities.mob;

import possiblydavid.gimbal.entities.Mover;
import possiblydavid.gimbal.input.KeyInput;
import possiblydavid.gimbal.world.World;

/**
 * The Player class. This is the player. Yessiree Bob.
 * 
 * @since 10-23-12
 * @author David Aaron Suddjian
 */
public class Player extends Mover {

	private double speed = 2.0;

	public Player(World w) {
		setWorld(w);
		setImage("graphics/sprites/player.png");
		world.add(this);
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
		if (exactX >= world.getWidth() - image.getWidth()) {
			setPos(world.getWidth() - image.getWidth(), exactY);
		} else if (exactX < 0) {
			setPos(0, exactY);
		}
		if (exactY >= world.getHeight() - image.getHeight()) {
			setPos(exactX, world.getHeight() - image.getHeight());
		} else if (exactY < 0) {
			setPos(exactX, 0);
		}
	}
}
