package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/***
 * Point light is a type of light
 */
public class PointLight extends Light implements LightSource {
    Point _position;
    double _kC;
    double _kL;
    double _kQ;

    /***
     *
     * @param intensity intensity of the light
     * @param position position of the light in the 3D space
     */
    public PointLight(Color intensity, Point position) {
        super(intensity);
        _position = position;
        _kC = 1;
        _kL = 0;
        _kQ = 0;
    }

    /***
     * setters in similar form to builder pattern which return this
     */
    public PointLight setKc(double kC) {
        _kC = kC;
        return this;
    }

    public PointLight setKl(double kL) {
        _kL = kL;
        return this;
    }

    public PointLight setKq(double kQ) {
        _kQ = kQ;
        return this;
    }

    /***
     * this function returns the intensity of the light calculated based on a given point
     * @param p point on the object
     * @return the Color of the point with respect to environmental impact
     */
    @Override
    public Color getIntensity(Point p) {
        double dim = _kC + _kL * p.distance(_position) + _kQ * p.distanceSquared(_position);
        return _intensity.scale(1/dim);
    }

    /***
     * this function calculates the vector between the light source and the point on the object
     * @param p the point
     * @return the vector
     */
    @Override
    public Vector getL(Point p) {
        Vector l = p.subtract(_position).normalize();
        return l;
    }

    /***
     * gives the distance between the pointlight and a point
     * @param p the point in the 3D space
     * @return the distance
     */
    @Override
    public double getDistance(Point p) {
        return _position.distance(p);
    }
}
