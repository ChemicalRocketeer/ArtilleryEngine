package possiblydavid.gimbal.entities;

import possiblydavid.gimbal.Game;
import possiblydavid.gimbal.Tick;
import possiblydavid.gimbal.Entity;

/**
 * A Mover has the ability to move to different positions
 * 
 * TODO: Remove test methods and make class abstract
 */
public class Mover extends Entity implements Tick {

	protected double deltaX, deltaY;

	public Mover() {
		super();
		deltaX = 0;
		deltaY = 0;
	}

	/**
	 * TODO: Remove test code
	 */
	public void tick() {
		deltaX += 0.001;
		deltaY += 0.003;
		x += deltaX;
		y += deltaY;
		if (x <= -20) {
			deltaX = Math.abs(deltaX);
			x = -20;
		} else if (x >= Game.width + 20) {
			deltaX = -Math.abs(deltaX);
			x = Game.width + 20;
		}
		if (y <= -20) {
			deltaY = Math.abs(deltaY);
			y = -20;
		} else if (y >= Game.height + 20) {
			deltaY = -Math.abs(deltaY);
			y = Game.height + 20;
		}
	}

}
