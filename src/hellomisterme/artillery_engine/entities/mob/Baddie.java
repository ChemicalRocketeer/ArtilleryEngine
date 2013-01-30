package hellomisterme.artillery_engine.entities.mob;

import hellomisterme.artillery_engine.Game;
import hellomisterme.artillery_engine.graphics.AnimatedSprite;

import java.awt.Rectangle;

/**
 * A Baddie is... a baddie... It hits the player and damages him.
 * 
 * @since 11-17-12
 * @author David Aaron Suddjian
 */
public class Baddie extends Mob {

	public double width, height;

	public Baddie() {
		this(0, 0);
	}

	public Baddie(double x, double y) {
		setPos(x, y);
		animation = new AnimatedSprite("graphics/sprites/baddie");
		image = animation;
		hitbox = new Rectangle(3, 3, 20, 20);
		movement = new Vector2D(Game.RAND.nextDouble() * 4 - 2, Game.RAND.nextDouble() * 4 - 2);
	}

	public void tick() {
		animate();
		movement.rotate(Game.RAND.nextDouble() * 0.5 - 0.25); // rotate by an amount between 0 and 0.5
		move();
		correctOOB();
	}
}
