package possiblydavid.gimbal.entities.mob;

import possiblydavid.gimbal.entities.Mover;
import possiblydavid.gimbal.world.World;

public class TestMob extends Mover {
	
	public TestMob(World w) {
		setWorld(w);
		deltaX = 1.564;
		image.setImage("art/test.png");
	}

	public void tick() {
		move();
		if (exactX >= world.getWidth()) {
			deltaX = - Math.abs(deltaX);
		} else if (exactX < 0) {
			deltaX = Math.abs(deltaX);
		}
		if (exactY >= world.getHeight()) {
			deltaY = - Math.abs(deltaY);
		} else if (exactY < 0) {
			deltaY = Math.abs(deltaY);
		}
	}
}
