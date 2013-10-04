package hellomisterme.artillery_engine.geometry;

import hellomisterme.util.MathUtils;
import hellomisterme.util.Vector;

import java.awt.Graphics2D;

public class Polygon {
	
	private Vector[] points;
	private Vector center;
	
	public Polygon(Vector[] points) {
		this.points = points;
		calculateCenter();
	}
	
	protected void calculateCenter() {
		// separate the polygon into triangles, calculate each triangle'poly area and center,
		// then average the centers using area as a weight
		int numTriangles = points.length - 2;
		double[] triangleAreas = new double[numTriangles];
		double[] triangleCentersX = new double[numTriangles];
		double[] triangleCentersY = new double[numTriangles];
		for (int i = 0; i < numTriangles; i++) {
			// get the triangle'poly a and b points, points[0] will be c
			Vector a = points[i + 1];
			Vector b = points[i + 2];
			triangleCentersX[i] = (points[0].x + a.x + b.x) / 3;
			triangleCentersY[i] = (points[0].y + a.y + b.y) / 3;
			a.sub(points[0]);
			b.sub(points[0]);
			// This isn't the actual area of the triangle since it isn't divided by 2, but it still works the same as a weight
			triangleAreas[i] = Math.sin(a.angle() - b.angle()) * a.mag() * b.mag();
		}
		// calculating weighted average of all the triangle centers
		center = new Vector(MathUtils.WeightedAverage(triangleCentersX, triangleAreas), MathUtils.WeightedAverage(triangleCentersY, triangleAreas));
	}
	
	public Vector getCenter() {
		return center.clone();
	}
	
	public void render(Graphics2D g) {
		int[] xPoints = new int[points.length];
		int[] yPoints = new int[points.length];
		for (int i = 0; i < points.length; i++) {
			xPoints[i] = (int) points[i].x;
			yPoints[i] = (int) points[i].y;
		}
		g.fillPolygon(xPoints, yPoints, points.length);
	}
}
