package renderer;

/***
 * test class for testing ray casting methods
 */

import geometries.*;
import org.junit.jupiter.api.Test;
import primitives.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/***
 * test method for constructRay function
 */
public class testCameraRayIntersectionsIntegration {
//non specific function that checks the every call on the same view point for DRY rule
    private void assertCountIntersections(Camera cam, Intersectable geo, int expected) {
        cam.setVPSize(3,3);
        //distance set to one for ease of calculation
        cam.setVPDistance(1);
        int countIntersections = 0;
        final int width = 3;//nX
        final int height = 3;//nY
        //check on every row and in each row for every different pixel according to column
        for(int i=0;i< height;i++){
            for(int j=0;j< width;j++){
                //create ray going from center of camera through center of pixel
                Ray camToVPRay=cam.constructRay(width,height,j,i);
                //create list of points of intersections the ray has with all objects in geo
                 List<Point>  intersectionsList=geo.findIntersections(camToVPRay);
                 if(intersectionsList!=null) {
                     //if the list is not empty add number of points found to sum of points
                     countIntersections += intersectionsList.size();
                 }
            }
        }
        //we assume the returned points are the right points, relying on previous checks
        //if sum is not the expected the test failed
        assertEquals(expected, countIntersections, "Wrong number of intersections");
    }

    /**
     * Integration tests of Camera Ray construction with Ray-Plane intersections
     */
    @Test
    //There is a plane
    public void cameraRayPlaneIntegration() {
        //create camera and zero point
        final Point ZERO_POINT = new Point(0, 0, 0);
        Camera camera = new Camera(ZERO_POINT, new Vector(0, 0, -1),
                new Vector(0, -1, 0)).setVPDistance(1);

        //TC00:view plane is parallel to plane right under camera
        Plane plane1 = new Plane(new Point(0, 0, -1), new Vector(0, 0, 1));
        assertCountIntersections(camera, plane1, 9);

        //TC01::no intersections
        Plane plane2=new Plane(new Point(0, 0, 1), new Vector(0, 0, 1));
        assertCountIntersections(camera, plane2, 0);

        //TC02: plane cuts through view plane, there are 6 pixels that see plane
        assertCountIntersections(camera, new Plane(new Point(0, 0, -5), new Vector(0, 1, 1)), 6);

        //TC03: another one like t2
        assertCountIntersections(camera, new Plane(new Point(0, 0.5, -1),new Point(-1,0.5 , -1),new Point(1, 1, -100),new Point(1, 1, -100)), 3);

        //TC04: small angle but all pixels show plane
        Plane plane3 = new Plane(new Point(1, 0, -2), new Point(0, 0, -2), new Point(0, 2, -1), new Point(0, 0, -2) );
        assertCountIntersections(camera, plane3, 9);
    }
    /**
     * Integration tests of Camera Ray construction with Ray-triangle intersections
     */
    //There is a triangle
    public void cameraRayTriangleIntegration(){
        //create camera and zero point
        final Point ZERO_POINT = new Point(0, 0, 0);
        Camera camera = new Camera(ZERO_POINT, new Vector(0, 0, -1),
                new Vector(0, -1, 0)).setVPDistance(1);

        //TC00: small triangle
        Triangle triangle0 = new Triangle(new Point(-1, 0, -2),new Point(0, 0.5, -2),new Point(0, -0.5, -2));
        assertCountIntersections(camera, triangle0, 3);

        //TC01: large triangle
        Triangle triangle1 = new Triangle(new Point(100, -100, -2),new Point(100, 100, -2),new Point(-100, 0, -2));
        assertCountIntersections(camera, triangle1, 9);
    }
    /**
     * Integration tests of Camera Ray construction with Ray-Sphere intersections
     */
    //There is sphere
    public void cameraRaySphereIntegration(){
        final Point ZERO_POINT = new Point(0, 0, 0);
        Camera camera = new Camera(ZERO_POINT, new Vector(0, 0, -1),
                new Vector(0, -1, 0)).setVPDistance(1);

        //TC00: small sphere - goes through one pixel and gives 2 points
        Sphere sphere0 = new Sphere(new Point(0,0,-2), 0.5);
        assertCountIntersections(camera, sphere0, 2);

        //TC01: medium sphere - goes through four pixels and gives 8 points
        Sphere sphere1 = new Sphere(new Point(0.5,-0.5,-2), 1);
        assertCountIntersections(camera, sphere1, 8);

        //TC02: big sphere - goes through all nine pixels and gives 18 points
        Sphere sphere2 = new Sphere(new Point(0,0,-10), 5);
        assertCountIntersections(camera, sphere2, 18);

        //TC03: in sphere - goes through nine pixels and gives 9 points
        Sphere sphere3 = new Sphere(new Point(0,0,2), 20);
        assertCountIntersections(camera, sphere3, 9);

        //TC04: facing away from sphere - goes through zero pixels
        Sphere sphere4 = new Sphere(new Point(0,0,2), 1);
        assertCountIntersections(camera, sphere4, 0);

    }

}

