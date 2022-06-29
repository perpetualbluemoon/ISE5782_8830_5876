package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

import java.util.LinkedList;

/***
 * Point light is a type of light source that is from a certain point
 */
public class PointLight extends Light implements LightSource {

    Point _position;
    double _kC;
    double _kL;
    double _kQ;

    //side of square around lightSource used for soft shadows
    double _size=1;

    /***
     * constructor
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
     * @param kC kc
     * @return the object for builder-like use
     */
    public PointLight setKc(double kC) {
        _kC = kC;
        return this;
    }

    /***
     * setters in similar form to builder pattern which return this
     * @param kL kl
     * @return object for builder-like use
     */
    public PointLight setKl(double kL) {
        _kL = kL;
        return this;
    }

    /***
     * setters in similar form to builder pattern which return this
     * @param kQ kq
     * @return object for builder-like use
     */
    public PointLight setKq(double kQ) {
        _kQ = kQ;
        return this;
    }

    /***
     * setter
     * @param size size of box around light
     */
    public void setSize(double size) {
        _size = size;
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
        return p.subtract(_position).normalize();
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

    /***
     * this function inside PointLight returns a list of points around the origin
     * this function uses createListOfMovedPoints in order to achieve the goal
     * @param lightDirection vector from the object to the light source
     * @param root_of_points this number is used to calculate how many points to create
     * @return the list of points
     */
    @Override
    public LinkedList<Point> findPointsAroundLight(Vector lightDirection, int root_of_points) {

        Vector U; //vup
        U = new Vector(0, -lightDirection.getXyz().getD3(), lightDirection.getXyz().getD2()).normalize();
        Vector V; //vright
        //not sure about scaling with -1??????????????????????
        V = U.crossProduct(lightDirection).normalize();

        double height=_size;
        double width=_size;

        Point edgeOfPixel = _position.add(V.scale(-0.5*_size));
        edgeOfPixel=edgeOfPixel.add(U.scale(0.5*_size));

        return _position.createListOfMovedPoints(edgeOfPixel, U, V, height, width, root_of_points);
    }
}
