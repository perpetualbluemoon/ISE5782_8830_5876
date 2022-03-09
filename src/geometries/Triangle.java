package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static java.lang.System.out;
import static primitives.Util.alignZero;
import static primitives.Util.isZero;

public class Triangle extends Polygon{
    /***
     * if all three points are in a line
     * @param p1, p2, p3 - points to build the triangle
     */
    public Triangle(Point p1, Point p2, Point p3) {
        super(p1,p2,p3);
    }

    @Override
    public String toString() {
        return "Triangle{" +
                "vertices=" + vertices +
                ", plane=" + plane +
                '}';
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
       List<Point> intersections = plane.findIntersections(ray);
       // List<Point> intersections=List.of(new Point(0.8,0.8,0));
        if(intersections == null){
            return null;
        }
         Point p0 = ray.getP0();
        Vector v = ray.getDir().normalize();

        Vector v1 = vertices.get(0).subtract(p0);
        Vector v2 = vertices.get(1).subtract(p0);
        Vector v3 = vertices.get(2).subtract(p0);

        double s1 = alignZero(v.dotProduct(v1.crossProduct(v2).normalize()));
        if (isZero(s1)){
            return null;
        }
        double s2 = alignZero(v.dotProduct(v2.crossProduct(v3).normalize()));
        if (isZero(s2)){
            return null;
        }
        double s3 = alignZero(v.dotProduct(v3.crossProduct(v1).normalize()));
        if (isZero(s3)){
            return null;
        }
        if(s1*s2 <= 0){
            return null;
        }

        if(s1*s3 <= 0)
        {
            return null;
        }
        //the list we got from super is valid, return the list
        return intersections;
    }
}
