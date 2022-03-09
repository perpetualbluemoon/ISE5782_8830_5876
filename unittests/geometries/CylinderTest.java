package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Unit tests for geometries.Cylinder class
 * @author Nechama Eri-Barron and Chaya Yazersky
 */

class CylinderTest {

    @Test
    void testGetNormal() {
        Ray centerLine = new Ray(new Point(0,0,0), new Vector(0,0,1));
        Tube cylinder = new Cylinder(centerLine, 3.0, 3.0);
        // ====== Equivalence Partition Tests ======
        //TC00: check for point on the side of the tube (not the base)
        assertEquals(cylinder.getNormal(new Point (0,3,0.5)),
                new Vector(0,1,0),
                "Incorrect normal for point on side of cylinder");
        //TC01: check the point is on the bottom base
        assertEquals(cylinder.getNormal(new Point (0,3,0)),
                new Vector(0,0,1),
                "Incorrect vector for point on bottom base");
        //TC02: check the point is on the top base
        assertEquals(cylinder.getNormal(new Point (0,3,3)),
                new Vector(0,0,1),
                "Incorrect vector for point on top base");

        // ====== Boundary Value Tests ======
        //TC03: point is in the center of bottom base
        assertEquals(cylinder.getNormal(new Point (0,0,0)),
                new Vector(0,0,1),
                "Incorrect vector from center of bottom base");
        //TC04: point is in the center of top base
        assertEquals(cylinder.getNormal(new Point (0,0,3)),
                new Vector(0,0,1),
                "Incorrect vector from center of top base");
    }
}