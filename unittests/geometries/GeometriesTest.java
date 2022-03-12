package geometries;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Unit tests for geometries.Geometries class
 * @author Nechama Eri-Barron and Chaya Yazersky
 */

public class GeometriesTest {
    @Test
    void testAdd() {
        Geometries g = new Geometries();
        Plane p = new Plane(new Point(1,0,0), new Vector(0,0,1));
        List l = List.of(p);
        g.add(p);

        assertEquals(l, g.getGeometries(),"List is incorrect");
    }

    /**
     * Test method for {@link geometries.Geometries#findIntersections(Ray)}.
     * This method checks the function findIntersections
     */
    @Test
    void testFindIntersections() {
        //Create geometries
        Plane planeInter = new Plane(new Point (3,0,0), new Vector(0,0,1));//one intersection
        Plane planeNoInter = new Plane(new Point (0,-5,0), new Vector (0,1,0));//no intersections
        Sphere sphereInter = new Sphere (new Point(0,0,-1), 2); // two intersections
        Sphere sphereNoInter = new Sphere (new Point(0, -5, 1), 3); // no intersections
        Triangle triangleInter = new Triangle (new Point(0, -3, 0), new Point (-1,1,0), new Point (3,0,0)); // one intersection
        Triangle triangleNoInter = new Triangle (new Point(1,-5,0), new Point(0,-5,0), new Point(5,-5,1)); // no intersections

        Ray ray = new Ray(new Point (0,0,-100), new Vector (0,0,1));
        //For all the test cases below:
        //Create list of geometries
        //Find intersections for geometries
        //Check that the number of intersections is correct

        //TC00: List of Geometries is Empty
        Geometries g0 = new Geometries();
        assertNull(g0.findIntersections(ray), "Empty list does not return intersections");

        //TC01: No Geometry has intersections with ray
        Geometries g1 = new Geometries(planeNoInter, sphereNoInter, triangleNoInter);
        assertNull(g1.findIntersections(ray), "No geometries failure");

        //TC02: Only one Geometry has intersections with the ray
        Geometries g2 = new Geometries(planeInter, sphereNoInter, triangleNoInter);
        assertEquals(1,g2.findIntersections(ray).size(), "No geometry intersections failure");

        //TC03: Several geometries but not all have intersections with the ray
        Geometries g3 = new Geometries(planeInter, sphereInter, sphereNoInter, triangleNoInter);
        assertEquals(3,g3.findIntersections(ray).size(), "Some geometries intersect failure");

        //TC04: All the geometries have intersections with the ray
        Geometries g4 = new Geometries(planeInter, sphereInter, triangleInter);
        assertEquals(4,g4.findIntersections(ray).size(), "All geometries intersect failure");
    }
}
