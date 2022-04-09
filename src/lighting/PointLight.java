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
    public PointLight setkC(double kC) {
        _kC = kC;
        return this;
    }

    public PointLight setkL(double kL) {
        _kL = kL;
        return this;
    }

    public PointLight setkQ(double kQ) {
        _kQ = kQ;
        return this;
    }

    @Override
    public Color getIntensity(Point p) {
        double d = _position.distance(p);
        double dim = _kC + _kL*d + _kQ*Math.pow(d,2);
        dim = 1.0/dim;
        return super._intensity.scale(dim);
    }

    @Override
    public Vector getL(Point p) {
        return p.subtract(_position).normalize();
    }
}
