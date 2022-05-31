package lighting;

import geometries.Intersectable;
import primitives.Color;
import primitives.Point;
import primitives.Vector;

import java.util.LinkedList;

/***
 * class Directional Light represents directional light
 */
public class DirectionalLight extends Light implements LightSource {
    Vector _direction;

    /***
     * constructor
     * @param intensity intensity of the life
     * @param direction the direction of the light
     */
    public DirectionalLight(Color intensity, Vector direction) {
        super(intensity);
        _direction = direction;
    }

    @Override
    public Color getIntensity(Point p) {
        return super._intensity;
    }

    @Override
    public Vector getL(Point p) {
        return _direction.normalize();
    }

    @Override
    public double getDistance(Point p) {
        return Double.POSITIVE_INFINITY;
    }

    /***
     *
     * @param lightDirection vector from the object to the light
     * @return list of points around the light source
     */
    @Override
    public LinkedList<Point> findPointsAroundLight(Vector lightDirection, int rootOfMovedPoints) {
        return null;
    }
}
