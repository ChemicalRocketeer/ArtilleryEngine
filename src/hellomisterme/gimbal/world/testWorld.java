package hellomisterme.gimbal.world;

import hellomisterme.gimbal.Game;
import hellomisterme.gimbal.entities.mob.Baddie;
import hellomisterme.gimbal.entities.mob.Player;
import hellomisterme.gimbal.io.Keyboard;

/**
 * testWorld is just a temporary class to test out World's functionality.
 * 
 * @since 10-23-12
 * @author David Aaron Suddjian
 */
public class testWorld extends World {

	private Player player;
	private boolean baddieOrdered = false;

	public testWorld(int width, int height) {
		setWidth(width);
		setHeight(height);

		player = new Player();
		add(player);
		player.world = this;

		generateBaddie();
		generateBaddie();
	}

	public void tick() {
		// if the addbaddie key is pressed
		if (Keyboard.pressed(Keyboard.addbaddie)) {
			if (baddieOrdered == false) { // if the key was up before
				generateBaddie();
				baddieOrdered = true; // remember that the key was pressed
			}
		} else { // key not pressed
			baddieOrdered = false;
		}
	}
	
	public void generateBaddie() {
		add(new Baddie((double) Game.RAND.nextInt(getWidth()), (double) Game.RAND.nextInt(getHeight())));
	}
}
