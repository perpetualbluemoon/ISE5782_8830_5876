package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static primitives.Util.isZero;

/**
 * Unit tests for geometries.Sphere class
 * @author Nechama Eri-Barron and Chaya Yazersky
 */
class SphereTest {

    @Test
    void testGetNormal() {
        // ====== Equivalence Partition Tests ======
        //TC00: Checking that the vector was normalized
       Sphere s = new Sphere(new Point(0,0,0), 3.0);
       Vector v = s.getNormal(new Point (0,0,3));

        // ====== Equivalence Partition Tests ======
        // ====== Boundary Value Tests ======
        // TC00 checking that the length of the normalized vector is 1
        assertTrue(isZero(v.length() - 1),"ERROR: the normalized vector is not a unit vector");

        //TC01: checking that the vector was calculated correctly
        assertEquals(new Vector (0,0,1),v, "Vector was calculated incorrectly");
    }

    @Test
    void testFindIntersections() {
        Sphere s = new Sphere (new Point(0,0,1), 3.0);
        // ====== Equivalence Partition Tests ======

        //TC00: there is one intersection - ray starts inside the sphere
        Ray r0 = new Ray(new Point(-0.5, 0, 1), new Vector(1,0,0));
        assertEquals(new Point(3,0,1), s.findIntersections(r0).get(0), "Intersection point on sphere failed");

        //TC01: there are two intersections
        Ray r1 = new Ray(new Point(-10, 0, 1), new Vector(1,0,0));
        List<Point> result=s.findIntersections(r1);
        assertEquals(2, result.size(),"Wrong number of points");
        Point p1 = new Point(-3,0,1);
        Point p2 = new Point(3,0,1);
        if (result.get(0).getXyz().getD1() > result.get(1).getXyz().getD1())
            result = List.of(result.get(1), result.get(0));
        assertEquals(List.of(p1, p2), result,"Ray crosses sphere");

        // TC02: there is no intersection
        Ray r2 = new Ray(new Point(4, 0, 1), new Vector(1,0,0));
        assertEquals(null, s.findIntersections(r2), "There is no intersection");

    }
}