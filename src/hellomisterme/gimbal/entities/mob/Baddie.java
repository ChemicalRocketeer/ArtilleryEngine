package hellomisterme.gimbal.entities.mob;

import hellomisterme.gimbal.Game;
import hellomisterme.gimbal.graphics.AnimatedSprite;

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
		this.x = x;
		this.y = y;
		animation = new AnimatedSprite("graphics/sprites/baddie");
		image = animation;
		hitbox = new Rectangle(3, 3, 20, 20);
	}

	public void tick() {
		animate();
		moveRandom();
	}

	public void moveRandom() {
		double angle = Game.RAND.nextDouble(); // between 0.0 and 1.0
		angle *= Math.PI * 2; // direction is now between 0.0 and 2 pi. Gonna do some trig here
		setPos(x + Math.cos(angle), y + Math.sin(angle) * Game.ISOMETRIC_RATIO);

		correctOOB();
	}
}
