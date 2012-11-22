package hellomisterme.gimbal.world;

import hellomisterme.gimbal.entities.mob.Baddie;
import hellomisterme.gimbal.entities.mob.Player;

/**
 * testWorld is just a temporary class to test out World's functionality.
 * 
 * @since 10-23-12
 * @author David Aaron Suddjian
 */
public class testWorld extends World {
	
	private Player player;

	public testWorld(int width, int height) {
		setWidth(width);
		setHeight(height);
		
		player = new Player();
		add(player);
		
		add(new Baddie((double) width / 2, (double) height / 2));
		add(new Baddie((double) width / 2, (double) height / 2));
	}
}
