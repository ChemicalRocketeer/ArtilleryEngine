package hellomisterme.gimbal.world;

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
	}
}
