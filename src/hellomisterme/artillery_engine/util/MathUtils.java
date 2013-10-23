package hellomisterme.artillery_engine.util;

public class MathUtils {
	
	public static int round(double d){
	    double dAbs = Math.abs(d);
	    int i = (int) dAbs;
	    double result = dAbs - (double) i;
	    if(result<0.5){
	        return d<0 ? -i : i;            
	    }else{
	        return d<0 ? -(i+1) : i+1;          
	    }
	}
	
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
	 * Compares the magnitudes of 2 Vectors and returns the one that is shorter.
	 * Does not copy the Vector before returning it, so the returned Vector will either reference a or b.
	 * Returns a if the Vectors are the same.
	 */
	public static Vector min(Vector a, Vector b) {
		double amag = a.mag2();
		double bmag = b.mag2();
		if (amag <= bmag)
			return a;
		else
			return b;
	}
	
	/**
	 * Compares the magnitudes of 2 Vectors and returns the one that is longer.
	 * Does not copy the Vector before returning it, so the returned Vector will either reference a or b.
	 * Returns a if the Vectors are the same.
	 */
	public static Vector max(Vector a, Vector b) {
		double amag = a.mag2();
		double bmag = b.mag2();
		if (amag >= bmag)
			return a;
		else
			return b;
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
		return Math.hypot(ax - bx, ay - by);
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
		ax = ax - bx;
		ay = ay - by;
		return ax * ax + ay * ay;
	}
}
