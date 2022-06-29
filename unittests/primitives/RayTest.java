package primitives;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/***
 * this test class checks the functions of ray
 */
public class RayTest {
    Point p1 = new Point (0,1,1); //closest
    Point p2 = new Point (0,1,2); //middle
    Point p3 = new Point(0,1,3); //farthest
    Ray ray = new Ray (new Point(0, 1, 0), new Vector(0,0,1));

    //==============Equivalence Partition Test================

    /***
     * this test Find Closest point
     * TC01: the closest point is in the middle of the list
     */
    @Test
    public void testFindClosestPoint01(){
        //TC01: the closest point is in the middle of the list
        List<Point> mylist = List.of(p2,p1,p3);
        assertEquals(new Point (0,1,1),ray.findClosestPoint(mylist),"Find closest point failed");

    }

    //====================Boundary Value Analysis======================

    /***
     * testFindClosestPoint
     * TC02: the list is empty
     */
    @Test
    public void testFindClosestPoint02(){
        //TC02: the list is empty
        LinkedList<Point> mylist = new LinkedList<>();
        assertNull(ray.findClosestPoint(mylist),"Find closest point failed");

    }

    /***
     * testFindClosestPoint
     * TC03: the list is null
     */
    @Test
    public void testFindClosestPoint03(){
        //TC03: the list is null
        assertNull(ray.findClosestPoint(null),"Find closest point failed");
    }

    /***
     * testFindClosestPoint
     * the closest point is first
     */
    @Test
    public void testFindClosestPoint04(){
        //TC04: the closest point is first
        List<Point> mylist = List.of(p1,p2,p3);
        assertEquals(new Point (0,1,1),ray.findClosestPoint(mylist),"Find closest point failed");

    }

    /***
     * testFindClosestPoint
     * the closest point is last
     */
    @Test
    public void testFindClosestPoint05(){
        //TC05: the closest point is last
        List<Point> mylist = List.of(p2,p3,p1);
        assertEquals(new Point (0,1,1),ray.findClosestPoint(mylist),"Find closest point failed");

    }

}
