package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static java.lang.Math.sqrt;
import static primitives.Util.alignZero;

/***
 * class Sphere holds the information and functions used for a sphere
 */
public class Sphere extends Geometry {
    final Point center;
    double radius;

    /***
     * constructor
     * @param center of the sphere
     * @param radius radius of the sphere
     */
    public Sphere(Point center, double radius) {
        this.center = center;
        this.radius = radius;
    }

    /***
     * getter
     * @return center
     */
    public Point getCenter() {
        return center;
    }

    /***
     * getter
     * @return radius of sphere
     */
    public double getRadius() {
        return radius;
    }

    /***
     * tostring
     * @return string describing the object
     */
    @Override
    public String toString() {
        return "Sphere{" +
                "p=" + center +
                ", radius=" + radius +
                '}';
    }

    /**
     * Method for {@link geometries.Sphere#getNormal(Point)}.
     * This method finds the normal vector to a point on the sphere
     */
    @Override
    public Vector getNormal(Point point) {
        Vector normal = point.subtract(center);
        return normal.normalize();
    }

    /***
     * function find geo intersections helper function used by ray tracer basic
     * @param ray the ray to check for intersections
     * @return list of geo intersections
     */
    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
        Point P0 = ray.getP0();
        Vector v = ray.getDir();

        if (P0.equals(center)) {
//            if(alignZero(radius - maxDistance) > 0){
//                return null;
//            }
            return List.of(new GeoPoint(this, center.add(v.scale(radius))));
        }
        Vector U = center.subtract(P0);

        double tm = alignZero(v.dotProduct(U));
        double d = alignZero(Math.sqrt(U.lengthSquared() - tm * tm));

        // no intersections : the ray direction is above the sphere
        if (d >= radius) {
            return null;
        }

        double th = alignZero(Math.sqrt(radius * radius - d * d));

        double t1 = alignZero(tm - th);
        double t2 = alignZero(tm + th);

        if(t1 <=0 && t2 <= 0){
            return null;
        }

        if (t1 > 0 && t2 > 0 /*&& alignZero(t1 - maxDistance) <= 0 && alignZero(t2 - maxDistance) <= 0*/) {
//            Point P1 = P0.add(v.scale(t1));
//            Point P2 = P0.add(v.scale(t2));
            Point P1 =ray.getPoint(t1);
            Point P2 =ray.getPoint(t2);
            return List.of(new GeoPoint(this,P1), new GeoPoint(this,P2));
        }
        if (t1 > 0  /*&& alignZero(t1 - maxDistance) <= 0*/) {
//            Point P1 = P0.add(v.scale(t1));
            Point P1 =ray.getPoint(t1);
            return List.of(new GeoPoint(this,P1));
        }
        if (t2 > 0 /*&& alignZero(t2 - maxDistance) <= 0 */) {
//            Point P2 = P0.add(v.scale(t2));

            Point P2 =ray.getPoint(t2);
            return List.of(new GeoPoint(this,P2));
        }
        return null;
    }

    /**
     * Method for {@link geometries.Sphere#findGeoIntersectionsHelper(Ray)}.
     * This is the older function. It is still here to enable the original tests to run
     * This method finds intersections of a ray with a sphere and returns them in a list of GeoPoint
     * @param ray the ray to find intersections with
     * @return list of geopoints
     */
    public List<GeoPoint> findGeoIntersectionsHelperOriginal(Ray ray) {
        Point O = center;// center of the sphere

        if (ray.getP0().equals(O)) {
            //if the ray starts from the center the vector is dir scaled by the radius
            return List.of(new GeoPoint(this, O.add(ray.getDir().scale(radius))));
        }

        //finding the values of different vectors for the calculation according to the theory
        Vector U = O.subtract(ray.getP0());
        Vector V = ray.getDir().normalize();
        double tm = alignZero(V.dotProduct(U));
        double d = alignZero(Math.sqrt(U.dotProduct(U) - tm * tm));

        //if the distance from the center is greater than the radius then there are no intersections
        if (d >= radius) {
            return null;
        }

        //finding the values by which to scale dir
        double th = alignZero(sqrt(radius * radius - d * d));
        double t1 = alignZero(tm + th);
        double t2 = alignZero(tm - th);

        //calculating the intersection points p1 and p2 and then returning them in a list
        //if t1 or t2 is negative then its point is not on the circle
        if (t1 > 0 && t2 > 0) {
            Point P1 = ray.getPoint(t1);
            Point P2 = ray.getPoint(t2);
            return List.of(new GeoPoint(this, P1), new GeoPoint(this, P2));
        }
        if (t1 > 0) {
            Point P1 = ray.getPoint(t1);
            return List.of(new GeoPoint(this, P1));
        }
        if (t2 > 0) {
            Point P2 = ray.getPoint(t2);
            return List.of(new GeoPoint(this, P2));
        }
        //no valid points found
        return null;
    }
}
