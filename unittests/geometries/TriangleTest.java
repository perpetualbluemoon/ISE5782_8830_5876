package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Unit tests for geometries.Triangle class
 * @author Nechama Eri-Barron and Chaya Yazersky
 */

class TriangleTest {

    /**
     * Test method for {@link geometries.Triangle#findIntersections(Ray)}.
     * This method checks the findIntersections function for triangle
     */
    @Test
    void testFindIntersections() {

        // ====== Equivalence Partition Tests ======
        // TC00: no intersections with the plane
        Triangle t0 = new Triangle(new Point(1,0,0), new Point(0,1,0), new Point(1,1,0));
        Ray r0 = new Ray(new Point(1,1,1), new Vector (0,1,0));
        assertEquals(null, t0.findIntersections(r0), "Ray is parallel to triangle");

        // TC01: intersection point is inside the triangle
        Ray r1 = new Ray(new Point(0.8,0.8,-1), new Vector (0,0,1));
        assertEquals(new Point(0.8,0.8,0), t0.findIntersections(r1).get(0), "Intersection point inside triangle failed");

        // TC02: intersection point is off the side of the triangle
        Ray r2 = new Ray(new Point(2,0.5,-1), new Vector (0,0,1));
        assertEquals(null, t0.findIntersections(r2), "Intersection point on the side of triangle failed");

        // TC03: intersection point is off the corner of the triangle
        Triangle t1 = new Triangle (new Point(-1,2,0), new Point(0,-1,0), new Point(1,-2,0));
        Ray r3 = new Ray(new Point(2,3,-1), new Vector (0,0,1));
        assertEquals(null, t1.findIntersections(r3), "Intersection point outside corner of triangle failed");

    }
}