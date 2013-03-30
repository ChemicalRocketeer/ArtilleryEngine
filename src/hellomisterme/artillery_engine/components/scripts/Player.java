package hellomisterme.artillery_engine.components.scripts;


/**
 * The Player class. This is the player. Yessiree Bob.
 * 
 * @since 10-23-12
 * @author David Aaron Suddjian
 */
public class Player extends Script {

	private boolean dead = false;

	public void die() {
		dead = true;
	}

	public boolean dead() {
		return dead;
	}
}
