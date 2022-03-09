package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.List;

/***
 * Interface for Composite Design Pattern
 */

public interface Intersectable {
    public List<Point> findIntersections(Ray ray);
}
