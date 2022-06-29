package geometries;

import primitives.*;

import java.util.List;

import static primitives.Util.isZero;

/***
 * Class Tube represents an infinite cylinder using a vector axis and a radius
 */

public class Tube extends Geometry {
    Ray axisRay;    //central ray of the tube
    double radius;  //the radius of the tube

    /***
     * constructor
     * @param axisRay central ray of tube
     * @param radius size of the radius
     */
    public Tube(Ray axisRay, double radius) {
        this.axisRay = axisRay;
        this.radius = radius;
    }

    /***
     * getter
     * @return axisRay center of tube
     */
    public Ray getAxisRay() {
        return axisRay;
    }

    /***
     * getter
     * @return radius of tube
     */
    public double getRadius() {
        return radius;
    }

    /***
     * to string
     * @return string describing tube
     */
    @Override
    public String toString() {
        return "Tube{" +
                "axisRay=" + axisRay +
                ", radius=" + radius +
                '}';
    }

    /***
     * function get normal returns the normal vector to a point outside the tube
     * @param point point on the tube
     * @return vector normal vector
     */
    @Override
    public Vector getNormal(Point point) {
        //calculating t distance between p0 and point o
        Vector v = point.subtract(axisRay.getP0());
        double t = axisRay.getDir().dotProduct(v);
        Point o;

        // if t==0 point is on base 0 is p0
        if (isZero(t)) {
            o = axisRay.getP0();
        } else {
            o = axisRay.getPoint(t);
        }
        //calculate and return normal vector
        Vector normal = point.subtract(o);
        return normal.normalize();
    }

    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
        return null;
    }
}

