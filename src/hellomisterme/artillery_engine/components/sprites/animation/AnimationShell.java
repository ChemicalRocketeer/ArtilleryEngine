package hellomisterme.artillery_engine.components.sprites.animation;

import hellomisterme.artillery_engine.components.sprites.BasicImage;

public class AnimationShell extends BasicImage {

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
