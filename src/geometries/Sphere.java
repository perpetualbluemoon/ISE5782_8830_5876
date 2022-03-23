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

    /**
     * Method for {@link geometries.Sphere#getNormal(Point)}.
     * This method finds the normal vector to a point on the sphere
     */
    @Override
    public Vector getNormal(Point point) {
        Vector normal = point.subtract(p);
        return normal.normalize();
    }

    /**
     * Method for {@link geometries.Sphere#findIntersections(Ray)}.
     * This method finds intersections of a ray with a sphere and returns them in a list
     */
    @Override
    public List<Point> findIntersections(Ray ray) {
        Point O = p;// center of the sphere

        if (ray.getP0().equals(O)) {
            //if the ray starts from the center the vector is dir scaled by the radius
            return List.of(O.add(ray.getDir().scale(radius)));
        }

        //finding the values of different vectors for the calculation according to the theory
        Vector U = O.subtract(ray.getP0());
        Vector V = ray.getDir().normalize();
        double tm = alignZero(V.dotProduct(U));
        double d = alignZero(Math.sqrt(U.dotProduct(U)-tm*tm));

        //if the distance from the center is greater than the radius then there are no intersections
        if (d>= radius){
            return null;
        }

        //finding the values by which to scale dir
        double th = alignZero(sqrt(radius*radius - d*d));
        double t1 = alignZero(tm + th);
        double t2 = alignZero(tm - th);

        //calculating the intersection points p1 and p2 and then returning them in a list
        //if t1 or t2 is negative then its point is not on the circle
        if (t1 > 0 && t2 > 0) {
            Point P1 =ray.getPoint(t1);
            Point P2 =ray.getPoint(t2);
            return List.of(P1, P2);
        }
        if (t1 > 0) {
            Point P1 =ray.getPoint(t1);
            return List.of(P1);
        }
        if (t2 > 0) {
            Point P2 =ray.getPoint(t2);
            return List.of(P2);
        }
        //no valid points found
        return null;
    }
}
