package hellomisterme.artillery_engine.game.components.scripts;

import hellomisterme.artillery_engine.Err;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * The Player class. This is the player. Yessiree Bob.
 * 
 * @since 10-23-12
 * @author David Aaron Suddjian
 */
public class Player extends Script {

	private BufferedImage image;

	private boolean dead = false;

	public Player() {
		try {
			image = ImageIO.read(new File("graphics/sprites/ship.png"));
		} catch (IOException e) {
			Err.error("Can't set player image!");
			e.printStackTrace();
		}
	}

	public void die() {
		dead = true;
	}

	public boolean dead() {
		return dead;
	}
}
