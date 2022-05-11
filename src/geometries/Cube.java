package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.LinkedList;
import java.util.List;

public class Cube extends Intersectable{
    Quadrangle top;
    Quadrangle bottom;
    Quadrangle side1;
    Quadrangle side2;
    Quadrangle side3;
    Quadrangle side4;

    public Cube (Point p1, Point p2, Point p3, Point p4, Point p5, Point p6, Point p7, Point p8){
        Quadrangle top = new Quadrangle(p1, p2, p3, p4);
        Quadrangle bottom = new Quadrangle(p5, p6, p7, p8);
        Quadrangle side1 = new Quadrangle(p2, p6, p7, p3);
        Quadrangle side2 = new Quadrangle(p3, p7, p8, p4);
        Quadrangle side3 = new Quadrangle(p4, p8, p5, p1);
        Quadrangle side4 = new Quadrangle(p1, p5, p3, p2);
    }


    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
        LinkedList<GeoPoint> intersections = new LinkedList<>();
        if (top.findGeoIntersections(ray) == null || top.findGeoIntersections(ray).isEmpty()) {
        }
        else
            intersections.addAll(top.findGeoIntersections(ray));
        if (bottom.findGeoIntersections(ray) == null || bottom.findGeoIntersections(ray).isEmpty()) {
        }
        else
            intersections.addAll(bottom.findGeoIntersections(ray));
        if (side1.findGeoIntersections(ray) == null || side1.findGeoIntersections(ray).isEmpty()) {
        }
        else
            intersections.addAll(side1.findGeoIntersections(ray));
        if (side2.findGeoIntersections(ray) == null || side2.findGeoIntersections(ray).isEmpty()) {
        }
        else
            intersections.addAll(side2.findGeoIntersections(ray));
        if (side3.findGeoIntersections(ray) == null || side3.findGeoIntersections(ray).isEmpty()) {
        }
        else
            intersections.addAll(side3.findGeoIntersections(ray));
        if (bottom.findGeoIntersections(ray) == null || bottom.findGeoIntersections(ray).isEmpty()) {
        }
        else
            intersections.addAll(bottom.findGeoIntersections(ray));
        return intersections;
    }
}
