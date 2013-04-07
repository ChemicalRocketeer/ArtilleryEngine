package hellomisterme.artillery_engine.components.imagery.animation;

import hellomisterme.artillery_engine.components.imagery.ImageShell;

public class AnimationShell extends ImageShell {

	public Animation anim;
	public String file;
	public int frame;

	public AnimationShell() {

	}

	@Override
	public int[] getPixels() {
		return anim.getPixels(frame);
	}

	@Override
	public int getWidth() {
		return anim.getWidth();
	}

	@Override
	public int getHeight() {
		return anim.getHeight();
	}

}
