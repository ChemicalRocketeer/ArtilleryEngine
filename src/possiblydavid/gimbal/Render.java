package possiblydavid.gimbal;

public class Render {

	private int width;
	private int height;
	public int[] pixels;

	public Render(int width, int height) {
		this.width = width;
		this.height = height;
		pixels = new int[width * height];
	}

	public void render() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				pixels[x + (y * width)] = (x * (width + y) / (width * 5 - y) * 34) % (y * 2 + 4) % 0xFFFFFF;
			}
		}
		pixels[700 + 200 * width] = 0xFFFFFF;
	}
}