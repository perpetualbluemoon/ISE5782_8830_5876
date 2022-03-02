package primitives;

import org.junit.jupiter.api.Test;

import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.*;
import static primitives.Util.isZero;

/**
 * Unit tests for primitives.Vector class
 * @author Nechama Eri-Barron and Chaya Yazersky
 */

class VectorTest {

    /**
     * Test method for {@link primitives.Vector#add(primitives.Vector)}.
     */
    @Test
    void testAdd() {
        Vector v1 = new Vector(2, 4, 3);
        Vector v2 = new Vector(1,1,1);
        Vector v3 = new Vector (-5,-6,-7);
        Vector v4 = new Vector (-1,-1,-1);

        // ====== Boundary Value Tests ======
        //TC00: Addition results in vector 0
        assertThrows(IllegalArgumentException.class,()->v2.add(v4), "Addition results in vector 0");
        //TC01: Addition with 0
        assertThrows(IllegalArgumentException.class,()->v2.add(new Vector(0,0,0)), "Addition with vector 0");

        // ====== Equivalence Partition Tests ======
        //TC02: Result is positive
        assertEquals(new Vector(3,5,4), v1.add(v2), "Error in addition calculation");
        //TC03: Result is negative
        assertEquals(new Vector(-3,-2,-4), v1.add(v3), "Error in addition calculation");
    }

    /**
     * Test method for {@link primitives.Vector#scale(double)}.
     */
    @Test
    void testScale() {
        Vector v1=new Vector(1,1,1);
        Vector v2=new Vector(-1,-1,-1);
        // ====== Boundary Value Tests ======
        //TC00: Scaled with 0
        assertThrows(IllegalArgumentException.class,
                ()->v1.scale(0),
                "Scaling with 0 failed");

        // ====== Equivalence Partition Tests ======
        //TC01: Scaled with positive number
        assertEquals(new Vector(3,3,3),v1.scale(3),"Scaling with positive number failed");
        //TC02: Scaled with negative number
        assertEquals(new Vector(-3,-3,-3),v1.scale(-3),"Scaling with negative number failed");
        //TC03: scale vector with negative values
        assertEquals(new Vector(-3,-3,-3),v2.scale(3),"Scaling vector with negative values failed");
    }

    /**
     * Test method for {@link primitives.Vector#lengthSquared()}.
     */
    @Test
    void testLengthSquared() {

        Vector v1 = new Vector(1, 2, 3);
        Vector v2 = new Vector(-2, -4, -6);
        Vector v3 = new Vector(0, 3, -2);

        // ====== Equivalence Partition Tests ======
        //TC00: Length squared is calculated correctly
        assertTrue(isZero(v1.lengthSquared() - 14),"ERROR: lengthSquared() wrong value");


    }

    /**
     * Test method for {@link primitives.Vector#length()}.
     */
    @Test
    void testLength() {
        // ====== Equivalence Partition Tests ======
        //TC00: Length is calculated correctly
        assertTrue(isZero(new Vector(0, 3, 4).length() - 5),"ERROR: length() wrong value");

    }

    /**
     * Test method for {@link primitives.Vector#dotProduct(Vector)}.
     */
    @Test
    void testDotProduct() {
        Vector v1 = new Vector(1, 2, 3);
        Vector v2 = new Vector(-2, -4, -6);
        Vector v3 = new Vector(0, 3, -2);

        // ====== Boundary Value Tests ======
        //TC00: dot product between orthogonal vectors
        assertTrue(isZero(v1.dotProduct(v3)),"ERROR: dotProduct() for orthogonal vectors is not zero");
        // ====== Equivalence Partition Tests ======
        //TC01: checking that dot product is the correct value
        assertTrue(isZero(v1.dotProduct(v2) + 28), "ERROR: dotProduct() wrong value");
    }

    /**
     * Test method for {@link primitives.Vector#crossProduct(Vector)}.
     */
    @Test
    void testCrossProduct() {

        Vector v1 = new Vector(1, 2, 3);
        Vector v2 = new Vector(-2, -4, -6);
        Vector v3 = new Vector(0, 3, -2);

        // ====== Boundary Value Tests ======
        //TC00: two vectors are parallel
        assertThrows(IllegalArgumentException.class,
                ()->v1.crossProduct(v2),
                "ERROR: crossProduct() for parallel vectors does not throw an exception" );

        // ====== Equivalence Partition Tests ======
        Vector vr = v1.crossProduct(v3);
        //TC01.1: checking correct length for vr
        assertTrue(isZero(vr.length() - v1.length() * v3.length()),"ERROR: crossProduct() wrong result length");
        //TC01.2: checking vr is orthogonal to v1 and v3
        assertTrue(isZero(vr.dotProduct(v1)) || !isZero(vr.dotProduct(v3)),
                "ERROR: crossProduct() result is not orthogonal to its operands");
    }

    /**
     * Test method for {@link primitives.Vector#normalize()}.
     */
    @Test
    void testNormalize() {
        Vector v = new Vector(0, 0, 9);
        Vector u = v.normalize();

        // ====== Boundary Value Tests ======
        //TC00.1 checking that the length of the normalized vector is 1
        assertTrue(isZero(u.length() - 1),"ERROR: the normalized vector is not a unit vector");

        //TC00.2 checking that the normalized vector is parallel to the original vector
        assertThrows(IllegalArgumentException.class,
                ()->v.crossProduct(u),
                "ERROR: the normalized vector is not parallel to the original one");

        //TC00.3 checking that the normalized vector is in the same direction as the original vector
        assertTrue(v.dotProduct(u) > 0, "ERROR: the normalized vector is opposite to the original one");

        // ====== Equivalence Partition Tests ======
        //TC01: checking that the vector was normalized correctly
        assertEquals(new Vector (0,0,1),u, "Vector was normalized incorrectly");
    }
}