package hellomisterme.artillery_engine.world;

import hellomisterme.artillery_engine.Game;
import hellomisterme.artillery_engine.entities.mob.Baddie;
import hellomisterme.artillery_engine.io.Keyboard;

/**
 * testWorld is just a temporary class to test out World's functionality.
 * 
 * @since 10-23-12
 * @author David Aaron Suddjian
 */
public class testWorld extends World {

	private boolean baddieOrdered = false;

	public testWorld(int width, int height) {
		setWidth(width);
		setHeight(height);

		makePlayer();
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
