package geometries;

import primitives.Point;
import primitives.Vector;

/***
 * Geometry is an interface for interacting with basic geometric shapes
 * */

public interface Geometry {
    /***
     *
     * @param point
     * @return returns a orthogonal normalized vector
     */
    Vector getNormal(Point point);
}
