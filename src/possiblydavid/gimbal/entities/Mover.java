package possiblydavid.gimbal.entities;

import possiblydavid.gimbal.Game;
import possiblydavid.gimbal.Tick;
import possiblydavid.gimbal.Entity;

/**
 * A Mover has the ability to move to different positions
 * 
 * TODO: Remove/clean test methods and make class abstract
 * TODO: Take functionality out of Mover to make SmoothMover
 */
public class Mover extends Entity implements Tick {

	protected double deltaX, deltaY, potentialX, potentialY, modX, modY;

	public Mover() {
		super();
		deltaX = 0;
		deltaY = 0;
		potentialX = 0;
		potentialY = 0;
		modX = 0.005;
		modY = 0.007;
	}

	public void tick() {
		move();
	}

	public void move() {
		deltaX += modX;
		deltaY += modY;
		int lastX = x;
		int lastY = y;
		x += deltaX + potentialX;
		y += deltaY + potentialY;
		potentialX += deltaX - (x - lastX);
		potentialY += deltaY - (y - lastY);

		// check if outside arbitrary movement bounds
		if (x <= -40) {
			deltaX = Math.abs(deltaX);
			potentialX = Math.abs(potentialX);
			modX = Math.abs(modX);
			x = -40;
		} else if (x >= Game.width + 20) {
			deltaX = -Math.abs(deltaX);
			potentialX = -Math.abs(potentialX);
			modX = -Math.abs(modX);
			x = Game.width + 20;
		}
		if (y <= -20) {
			deltaY = Math.abs(deltaY);
			potentialY = Math.abs(potentialY);
			modY = Math.abs(modY);
			y = -20;
		} else if (y >= Game.height + 50) {
			deltaY = -Math.abs(deltaY);
			potentialY = -Math.abs(potentialY);
			modY = -Math.abs(modY);
			y = Game.height + 50;
		}
	}
}
