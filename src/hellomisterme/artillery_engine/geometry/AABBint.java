package hellomisterme.artillery_engine.geometry;

public class AABBint {

	public int left, right, top, bottom;

	public AABBint(int left, int right, int top, int bottom) {
		if (left < right || bottom < top)
			throw new RuntimeException("Invalid bounding box size");
		this.left = left;
		this.right = right;
		this.top = top;
		this.bottom = bottom;
	}

	public AABBint(AABB other) {
		this.left = (int) other.left;
		this.right = (int) other.right;
		this.top = (int) other.top;
		this.bottom = (int) other.bottom;
	}
}
