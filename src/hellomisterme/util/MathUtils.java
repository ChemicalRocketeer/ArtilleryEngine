package hellomisterme.util;

public class MathUtils {
	
	/**
	 * Calculates a weighted average.
	 * 
	 * @param values the values that will be averaged
	 * @param weights the weights to assign to each value
	 * @return The weighted average of values
	 */
	public static double WeightedAverage(double[] values, double[] weights) {
		double totalWeight = 0;
		double totalWeightedValue = 0;
		for (int i = 0; i < values.length; i++) {
			totalWeightedValue += values[i] * weights[i];
			totalWeight += weights[i];
		}
		return totalWeightedValue / totalWeight;
	}
	
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
