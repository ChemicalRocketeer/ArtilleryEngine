package possiblydavid.gimbal.world;

import possiblydavid.gimbal.entities.mob.TestMob;

public class testWorld extends World {
	
	public testWorld() {
		TestMob mob = new TestMob(this);
		add(mob);
		addTicker(mob);
	}
}
