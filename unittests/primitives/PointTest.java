package primitives;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;


import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for primitives.Point class
 * @author Nechama Eri-Barron and Chaya Yazersky
 */

class PointTest {

    /**
     * Test method for {@link primitives.Point#subtract(primitives.Point)}.
     */
    @Test
    void testSubtract() {

        Point p1 = new Point(2, 4, 3);
        Point p2 = new Point(1,1,1);
        Point p3 = new Point (-5,-6,-7);

        //  ====== Boundary Value Tests ======
        // TC00: Subtraction results in vector 0

        assertThrows(IllegalArgumentException.class,
               ()->p1.subtract(p1),
              "Subtraction results in vector 0");

        // ====== Equivalence Partition Tests ======
        //TC01: Result is positive
        assertEquals(new Vector(1,3,2),p1.subtract(p2),"Vector subtraction with positive result failed");

        //TC02: Result is negative 
        assertEquals(new Vector(-1,-3,-2),p2.subtract(p1),"Vector subtraction with negative result failed");

        //TC03: Subtracting a negative number
        assertEquals(new Vector(7,10,10),p1.subtract(p3),"Vector subtraction with negative vector failed");
    }

    /**
     * Test method for {@link primitives.Point#add(primitives.Vector)}.
     */
    @Test
    void testAdd() {
        //=====Equivalence Partition Tests=====
        //TC00: Addition with a positive result
        Point p0 = new Point(0,0,0);
        Vector v1 = new Vector(1,1,1);
        assertEquals(new Point (1,1,1), p0.add(v1), "Incorrect addition");

        //  ====== Boundary Value Tests ======
        //TC01: Addition with a negative result
        assertThrows(IllegalArgumentException.class,
                ()-> p0.add(new Vector(0,0,0)),
                "impossible addition");
    }

    /**
     * Test method for {@link primitives.Point#distanceSquared(primitives.Point)}.
     */
    @Test
    void testDistanceSquared() {
        //=====Equivalence Partition Tests=====
        //TC00: Squared distance between two points is calculated correctly
        assertEquals(
                3,
                new Point (0,0,0).distanceSquared(new Point (1,1,1)),
                "Problem with distance calculation");

    }

    /**
     * Test method for {@link primitives.Point#distance(primitives.Point)}.
     */
    @Test
    void testDistance() {

        //=====Equivalence Partition Tests=====
        //TC00: Distance between two points is calculated correctly
        assertEquals(
                9,
                new Point (0,0,0).distance(new Point (0,0,9)),
                "Problem with distance calculation");
    }
}