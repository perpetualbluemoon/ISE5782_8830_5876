package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static java.lang.Math.sqrt;
import static java.lang.System.out;
import static primitives.Util.alignZero;
import static primitives.Util.isZero;

public class Sphere implements Geometry
{
    final Point p;
    double radius;

    public Sphere(Point p, double radius) {
        this.p = p;
        this.radius = radius;
    }

    public Point getP() {
        return p;
    }

    public double getRadius() {
        return radius;
    }

    @Override
    public String toString() {
        return "Sphere{" +
                "p=" + p +
                ", radius=" + radius +
                '}';
    }

    @Override
    public Vector getNormal(Point point) {
        Vector normal = point.subtract(p);
        return normal.normalize();
    }

    /**
     * Test method for {@link geometries.Sphere#findIntersections(Ray)}.
     * This method checks the findIntersections function for sphere
     */
    @Override
    public List<Point> findIntersections(Ray ray) {
        Point O = p; // center of the sphere
        Vector U = O.subtract(ray.getP0());
        Vector V = ray.getDir().normalize();
        double tm = alignZero(V.dotProduct(U));
        double d = alignZero(sqrt(U.dotProduct(U)-tm*tm));
        if (d>= radius){
            return null;
        }
        if(U.dotProduct(V)<=0){
            return null;
        }
        double th = alignZero(sqrt(radius*radius - d*d));
        double t1 = alignZero(tm + th);
        double t2 = alignZero(tm - th);
        Point p1 = ray.getPoint(t1);
        Point p2 = ray.getPoint(t2);
        return List.of(p1,p2);
    }
}
