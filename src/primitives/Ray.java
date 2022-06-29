package primitives;

import geometries.Intersectable.GeoPoint;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/***
 * class Ray used for finding intersections and other interactions between light and objects
 */
public class Ray {
    /***
     * DELTA small number for moving points in order to prevent self-intersection
     */
    private static final double DELTA = 0.1;

    /***
     * start point of the ray
     */
    private final Point p0;
    /***
     * direction of the ray
     */
    private final Vector dir;

    /***
     * constructor
     * @param p0 start point
     * @param dir direction
     */
    public Ray(Point p0, Vector dir) {
        this.p0 = p0;
        this.dir = dir.normalize();
    }

    /***
     *
     * @param p the point to move
     * @param n normal vector to the point - the direction to move it in
     * @param direction direction of the light
     */
    public Ray(Point p, Vector n, Vector direction) {
        Vector scaled;
        double nv = alignZero(n.dotProduct(direction));
         if (nv < 0) {
            scaled = n.scale(-DELTA);
        } else {
            scaled = n.scale(DELTA);
        }
        p0 = p.add(scaled);
        dir = direction.normalize();
    }

    /***
     * getter
     * @return p0
     */
    public Point getP0() {
        return p0;
    }

    /***
     * getter
     * @return direction
     */
    public Vector getDir() {
        return dir;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ray ray = (Ray) o;
        return Objects.equals(p0, ray.p0) && Objects.equals(dir, ray.dir);
    }

    @Override
    public String toString() {
        return "Ray{" +
                "p0=" + p0 +
                ", dir=" + dir +
                '}';
    }

    /***
     * helper function used frequently
     * @param t amound to move the point
     * @return moved point in the direction of dir scaled by t
     */
    public Point getPoint(double t) {
        if (isZero(t))
            return p0;
        return p0.add(dir.scale(t));
    }

    /***
     * finds the closest point from a list
     * @param geoPointList list of points
     * @return the closest one
     */
    public GeoPoint findClosestGeoPoint(LinkedList<GeoPoint> geoPointList) {
        //if the list is empty return null, there is no closest point
        if (geoPointList == null || geoPointList.isEmpty())
            return null;

        //closest point starts as first point on the list
        GeoPoint closestPoint = geoPointList.get(0);
        //distance initialized to the largest value
        double distance = Double.MAX_VALUE;
        double d;

        //compare the distance of each point, if smaller then update closest point and distance
        for (var pt : geoPointList) {
            d = p0.distance(pt._geoPoint);
            if (d < distance) {
                distance = d;
                closestPoint = pt;
            }
        }
        return closestPoint;
    }

    /***
     * this function makes it possible for tests from previous stages to run without a problem while avoiding repetitions in the code
     * @param pointList list of points
     * @return closest point
     */
    public Point findClosestPoint(List<Point> pointList) {
        //if the list is empty return null, there is no closest point
        if (pointList == null || pointList.isEmpty())
            return null;
        LinkedList<GeoPoint> l = new LinkedList<>();
        for (var item : pointList) {
            l.add(new GeoPoint(null, item));
        }
        GeoPoint closestPoint = findClosestGeoPoint(l);
        return closestPoint._geoPoint;
    }
}
