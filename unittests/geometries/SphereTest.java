package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static primitives.Util.isZero;

/**
 * Unit tests for geometries.Sphere class
 * @author Nechama Eri-Barron and Chaya Yazersky
 */
class SphereTest {
    /**
     * Test method for {@link geometries.Sphere#getNormal(Point)}.
     * This method checks the function getNormal of sphere
     */
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
    /**
     * Test method for {@link geometries.Sphere#findIntersections(Ray)}.
     * This method checks the function findIntersection of sphere
     */
    @Test
    void testFindIntersections() {
        Sphere s = new Sphere (new Point(0,0,1), 3.0);
        Sphere s1 = new Sphere(new Point(0, 0, 1), 1.0);
        Point gp1 = new Point(0.0651530771650466, 0.355051025721682, 0);
        Point gp2 = new Point(1.53484692283495, 0.844948974278318, 0);
        List<Point> exp = List.of(gp1, gp2);

        //========Equivalence Tests============

        // TC00: Ray's line is outside the sphere (0 points)
        assertNull(s1.findIntersections(
                      new Ray(
                             new Point(-1, 0, 0),
                             new Vector(1, 1, 0))),
                     "Ray's line out of sphere");

        // TC01: Ray starts before and crosses the sphere (2 points)
        //List<Point> res = s1.findIntersections(new Ray(new Point(-1, 0, 0), new Vector(3, 1, 0)));
        //assertEquals(2, res.size(), "Wrong number of points");
        //if (res.get(0).getXyz().getD1() > res.get(1).getXyz().getD1())
          //  res = List.of(res.get(1), res.get(0));
        //assertEquals(exp, res, "Ray crosses sphere");


        // TC02: Ray starts inside the sphere (1 point)
        assertEquals(
                List.of(new Point(0.8,0.6,1.0)),
                s1.findIntersections(
                        new Ray(
                                new Point(0.5, 0.5, 1),
                                new Vector(3, 1, 0))),
                "Ray from inside sphere");

        // TC03: Ray starts after the sphere (0 points)
        assertNull(
                s1.findIntersections(
                        new Ray(new Point(2, 1, 0), new Vector(3, 1, 0))),
                "Sphere behind Ray");

        // ====== Boundary Tests ======
        //TC04: there is one intersection - ray starts inside the sphere
        Ray r0 = new Ray(new Point(-0.5, 0, 1), new Vector(1,0,0));
        assertEquals(new Point(3,0,1), s.findIntersections(r0).get(0), "Intersection point on sphere failed");

       // TC05: there are two intersections
        Ray r1 = new Ray(new Point(-10, 0, 1), new Vector(1,0,0));
        List<Point> result=s.findIntersections(r1);
        assertEquals(2, result.size(),"Wrong number of points");
        Point p1 = new Point(-3,0,1);
        Point p2 = new Point(3,0,1);
        if (result.get(0).getXyz().getD1() > result.get(1).getXyz().getD1())
            result = List.of(result.get(1), result.get(0));
        assertEquals(List.of(p1, p2), result,"Ray crosses sphere");

        // TC06: there is no intersection
        Ray r2 = new Ray(new Point(4, 0, 1), new Vector(1,0,0));
        assertEquals(null, s.findIntersections(r2), "There is no intersection");

        // TC07: Ray's line is outside, ray is orthogonal to ray start to sphere's
        // center line
        assertNull(s1.findIntersections(new Ray(new Point(-1, 0, 0), new Vector(0, 0, 1))),
                "Ray orthogonal to ray head -> O line");

        // TC08: Ray starts at the center (1 points)
        assertEquals(List.of(new Point(0, 1, 1)),
                s1.findIntersections(new Ray(new Point(0, 0, 1), new Vector(0, 1, 0))),
                "Line through O, ray from O");

        // Ray is a tangent
        // TC09: Ray starts before the tangent point
        assertNull(s1.findIntersections(new Ray(new Point(0, 1, 0), new Vector(1, 0, 0))),
                "Intersection is on tangent");
    }
}