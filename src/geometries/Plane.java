package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

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

    @Override
    public List<Point> findIntersections(Ray ray) {
        //don't allow p0 == g0
        if(ray.getP0().equals(_g0)){
            return null;
        }
        Vector n = getNormal();
        double nv = n.dotProduct(ray.getDir());
        //vectors are parallel and don't have any intersections
        if(isZero(nv)){
            return null;
        }
        double t = alignZero((_g0.subtract(ray.getP0()).dotProduct(n))/nv);
        // t!=0 because the point is not on the plane
        if(t<0) {
            return null;
        }
        return List.of(ray.getPoint(t));
    }
}

