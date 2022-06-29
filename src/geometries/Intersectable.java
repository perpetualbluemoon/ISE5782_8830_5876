package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.List;

/***
 * Interface for Composite Design Pattern
 */

public abstract class Intersectable {


    /***
     * @param ray the ray to examine for intersections
     * @return list of GeoPoint intersections
     */
    public List<GeoPoint> findGeoIntersections(Ray ray) {
        return findGeoIntersectionsHelper(ray);
    }

    /***
     * helper function for findGeoIntersections using patten NVI
     * @param ray the ray to check for intersections
     * @return list of GeoPoint intersections
     */
    protected abstract List<GeoPoint> findGeoIntersectionsHelper(Ray ray);

    /***
     * function finds intersections between a ray and the list of geometries
     * @param ray the ray
     * @return the list of points
     */
    public List<Point> findIntersections(Ray ray) {
        var geoList = findGeoIntersections(ray);
        return geoList == null ? null
                : geoList.stream().map(gp -> gp._geoPoint).toList();
    }

    /***
     * this is a helper class for Intersectable which saves the color of a point
     */
    public static class GeoPoint {
        /***
         * the geometry that the point is on
         */
        public Geometry _geoPointGeometry;
        /***
         * the point in the 3D space
         */
        public Point _geoPoint;

        /***
         * constructor
         * @param geoPointGeometry geometry
         * @param geoPoint the point on the geometry
         */
        public GeoPoint(Geometry geoPointGeometry, Point geoPoint) {
            _geoPointGeometry = geoPointGeometry;
            _geoPoint = geoPoint;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null) return false;
            GeoPoint geoPoint = (GeoPoint) o;
            return _geoPoint.equals(geoPoint._geoPoint) && _geoPointGeometry == geoPoint._geoPointGeometry;
        }

        @Override
        public String toString() {
            return "GeoPoint{" +
                    "_geoPointGeometry=" + _geoPointGeometry +
                    ", _geoPoint=" + _geoPoint +
                    '}';
        }
    }
}
