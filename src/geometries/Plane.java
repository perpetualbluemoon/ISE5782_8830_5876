package geometries;

import primitives.Point;
import primitives.Vector;

/***
 * Plane class represents a two dimensional plane with cartesian coordinates
 */

public class Plane implements Geometry {
    final Point _g0;
    final Vector _normal;

    public Plane(Point g0, Vector normal) {
        _g0 = g0;
        _normal = normal.normalize();
    }
    public Plane(Point p1, Point p2, Point p3, Point g0){
        _g0 = p1;
        Vector U = p2.subtract(p1);
        Vector V = p3.subtract(p1);

        Vector N = U.crossProduct(V);

        N.normalize();

        //right hand rule
        _normal = N;

    }

    @Override
    public String toString() {
        return "Plane{" +
                "_g0=" + _g0 +
                ", _normal=" + _normal +
                '}';
    }

    public Point getG0() {
        return _g0;
    }

    @Override
    public Vector getNormal(Point point) {
        return getNormal();
    }

    //we added .normalize() and then it worked but the normal should be normalized already
    public Vector getNormal(){
        return _normal.normalize();
    }
}

