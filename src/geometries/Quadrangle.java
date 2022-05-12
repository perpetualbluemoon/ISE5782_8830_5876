package geometries;

import primitives.*;

import java.util.LinkedList;
import java.util.List;

/***
 * 2D geometry representing a 4-sided shape
 * this is a helper class in order to make the creation of scenes with four sided objects easier
 */
public class Quadrangle extends Geometry {

    Triangle firstTriangle;
    Triangle secondTriangle;

    /***
     * constructor for quadrangle receives four points in clockwise order
     * @param p0 first point
     * @param p1 second point
     * @param p2 third point
     * @param p3 fourth point
     */
    public Quadrangle(Point p0, Point p1, Point p2, Point p3) {
        firstTriangle = new Triangle(p0, p1, p2);
        secondTriangle = new Triangle(p2, p3, p0);
    }

    /***
     * uses the functions from triangle in order to implement DRY
     * @param p the point for the normal
     * @return the normal to the quadrangle
     */
    public Vector getNormal(Point p) {
        return firstTriangle.getNormal(p);
    }

    /***
     * uses the functions from triangle in order to implement DRY
     * @param ray the ray to find intersections with
     * @return the list of intersections
     */
    @Override
    public LinkedList<Intersectable.GeoPoint> findGeoIntersectionsHelper(Ray ray) {
        LinkedList<Intersectable.GeoPoint> intersections = new LinkedList<>();
        if (firstTriangle.findGeoIntersections(ray) == null || firstTriangle.findGeoIntersections(ray).isEmpty()) {
        }
        else
            intersections.addAll(firstTriangle.findGeoIntersections(ray));
        if (secondTriangle.findGeoIntersections(ray) == null || secondTriangle.findGeoIntersections(ray).isEmpty()) {
        }
        else
            intersections.addAll(secondTriangle.findGeoIntersections(ray));
        return intersections;
    }
    @Override
    public LinkedList<Intersectable.GeoPoint> findGeoIntersections(Ray ray) {
        LinkedList<Intersectable.GeoPoint> intersections = new LinkedList<>();
        if (firstTriangle.findGeoIntersections(ray) == null || firstTriangle.findGeoIntersections(ray).isEmpty()) {
        }
        else
            intersections.addAll(firstTriangle.findGeoIntersections(ray));
        if (secondTriangle.findGeoIntersections(ray) == null || secondTriangle.findGeoIntersections(ray).isEmpty()) {
        }
        else
            intersections.addAll(secondTriangle.findGeoIntersections(ray));
        return intersections;
    }

    /***
     * sets the color of the quadrangle
     * @param color the color to set to
     */
    public Quadrangle setColor(Color color){
        firstTriangle.setEmission(color);
        secondTriangle.setEmission(color);
        return this;
    }

    public Quadrangle setTransparency(Double3 kt){
        firstTriangle.getMaterial().setKt(kt);
        secondTriangle.getMaterial().setKt(kt);
        return this;
    }

    public Quadrangle setReflectivity(Double3 kr){
        firstTriangle.getMaterial().setKr(kr);
        secondTriangle.getMaterial().setKr(kr);
        return this;
    }

    public Quadrangle setShininess(int shine){
        firstTriangle.getMaterial().setShininess(shine);
        secondTriangle.getMaterial().setShininess(shine);
        return this;
    }


}
