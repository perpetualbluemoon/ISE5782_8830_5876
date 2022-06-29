package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/***
 * Plane class represents a 2D plane with cartesian coordinates
 */

public class Plane extends Geometry {
    final Point _g0;
    final Vector _normal;


    /***
     * constructor
     * @param g0 point on the plane
     * @param normal normal to the plane
     */
    public Plane(Point g0, Vector normal) {
        _g0 = g0;
        _normal = normal.normalize();
    }

    /***
     * constructor using four points
     * @param p1 first point on the plane
     * @param p2 second point on the plane
     * @param p3 third point on the plane
     * @param g0 initial point on the plane
     */
    public Plane(Point p1, Point p2, Point p3, Point g0) {
        _g0 = p1;
        //find first vector
        Vector U = p2.subtract(p1);
        //find second vector
        Vector V = p3.subtract(p1);

        //find normal orthogonal to the two vectors
        Vector N = U.crossProduct(V);

        //return normalized vector
        _normal = N.normalize();

    }

    @Override
    public String toString() {
        return "Plane{" +
                "_emission=" + _emission +
                ", _g0=" + _g0 +
                ", _normal=" + _normal +
                '}';
    }

    /***
     * getter
     * @return g0
     */
    public Point getG0() {
        return _g0;
    }

    @Override
    public Vector getNormal(Point point) {
        return getNormal();
    }

    /***
     * returns normal vector which is normalized again to be safe
     * @return normal vector
     */
    public Vector getNormal() {
        return _normal.normalize();
    }

    /***
     *
     * @param ray to find intersections with
     * @return list of GeoPoint intersections with the objects in the scene
     */
    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
        //don't allow p0 == g0
        if (ray.getP0().equals(_g0)) {
            return null;
        }
        Vector n = getNormal();
        double nv = n.dotProduct(ray.getDir());
        //vectors are parallel and don't have any intersections
        if (isZero(nv)) {
            return null;
        }
        double t = alignZero((_g0.subtract(ray.getP0()).dotProduct(n)) / nv);
        // t!=0 because the point is not on the plane
        if (t < 0) {
            return null;
        }
        List<GeoPoint> p = List.of(new GeoPoint(this, ray.getPoint(t)));
        return p;
    }
}

