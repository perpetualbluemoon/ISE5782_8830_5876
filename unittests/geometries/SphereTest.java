package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;
import static primitives.Util.isZero;

class SphereTest {

    @Test
    void testGetNormal() {
        // ====== Equivalence Partition Tests ======
        //TC00: Checking that the vector was normalized
       Sphere s = new Sphere(new Point(0,0,0), 3.0);
       Vector v = s.getNormal(new Point (0,0,3));

        // ====== Equivalence Partition Tests ======
        // ====== Boundary Value Tests ======//TC00 checking that the length of the normalized vector is 1
        assertTrue(isZero(v.length() - 1),"ERROR: the normalized vector is not a unit vector");

        //TC01: checking that the vector was calculated correctly
        assertEquals(new Vector (0,0,1),v, "Vector was calculated incorrectly");
    }
}