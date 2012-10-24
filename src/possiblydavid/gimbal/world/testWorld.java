package possiblydavid.gimbal.world;

import possiblydavid.gimbal.entities.mob.Player;

/**
 * testWorld is just a temporary class to test out World's functionality.
 * 
 * @since 10-23-12
 * @author David Aaron Suddjian
 */
public class testWorld extends World {

	public testWorld() {
		Player player = new Player(this);
	}
}
