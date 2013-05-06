package hellomisterme.util;

public class MathUtils {

	/**
	 * Calculates the distance between 2 points.
	 * 
	 * @param ax the x coordinate of point a
	 * @param ay the y coordinate of point a
	 * @param bx the x coordinate of point b
	 * @param by the y coordinate of point b
	 * @return the distance between the two points
	 */
	public static double dist(double ax, double ay, double bx, double by) {
		return Math.sqrt(dist2(ax, ay, bx, by));
	}

	/**
	 * Calculates the square of the distance between 2 points. Faster than dist() because of the lack of a costly sqrt() operation.
	 * 
	 * @param ax the x coordinate of point a
	 * @param ay the y coordinate of point a
	 * @param bx the x coordinate of point b
	 * @param by the y coordinate of point b
	 * @return the square of the distance between the two points
	 */
	public static double dist2(double ax, double ay, double bx, double by) {
		double x = ax - bx;
		double y = ay - by;
		return x * x + y * y;
	}
}
