package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Unit tests for geometries.Tube class
 * @author Nechama Eri-Barron and Chaya Yazersky
 */

class TubeTest {

    /**
     * Test method for {@link geometries.Tube#getNormal(Point)}.
     * This method checks the getNormal function for tube
     */
    @Test
    void testGetNormal() {
        Ray centerLine = new Ray(new Point(0,0,0), new Vector(0,0,1));
        Tube tube = new Tube(centerLine, 3.0);

        // ====== Equivalence Partition Tests ======
        //TC00: check that the correct vector is calculated
        assertEquals(tube.getNormal(new Point (0,3,0.5)), new Vector(0,1,0), "Incorrect vector");

        // ====== Boundary Value Tests ======
        //TC01: check the case where the vector from the point to the center is orthogonal to the axis
        assertEquals(tube.getNormal(new Point (0,3,0)), new Vector(0,1,0), "Incorrect vector");
    }
}