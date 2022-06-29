package geometries;

import primitives.*;

import java.util.LinkedList;

/***
 * 2D geometry representing a 4-sided shape
 * this is a helper class in order to make the creation of scenes with four sided objects easier
 */
public class Quadrangle extends Geometry {

    Triangle _firstTriangle;
    Triangle _secondTriangle;

    /***
     * constructor for quadrangle receives four points in clockwise order
     * @param p0 first point
     * @param p1 second point
     * @param p2 third point
     * @param p3 fourth point
     */
    public Quadrangle(Point p0, Point p1, Point p2, Point p3) {
        _firstTriangle = new Triangle(p0, p1, p2);
        _secondTriangle = new Triangle(p2, p3, p0);
    }

    /***
     * uses the functions from triangle in order to implement DRY
     * @param p the point for the normal
     * @return the normal to the quadrangle
     */
    public Vector getNormal(Point p) {
        return _firstTriangle.getNormal(p);
    }

    /***
     * uses the functions from triangle in order to implement DRY
     * @param ray the ray to find intersections with
     * @return the list of intersections
     */
    @Override
    public LinkedList<Intersectable.GeoPoint> findGeoIntersectionsHelper(Ray ray) {
        LinkedList<Intersectable.GeoPoint> intersections = new LinkedList<>();
        if (_firstTriangle.findGeoIntersections(ray) == null || _firstTriangle.findGeoIntersections(ray).isEmpty()) {
        }
        else
            intersections.addAll(_firstTriangle.findGeoIntersections(ray));
        if (_secondTriangle.findGeoIntersections(ray) == null || _secondTriangle.findGeoIntersections(ray).isEmpty()) {
        }
        else
            intersections.addAll(_secondTriangle.findGeoIntersections(ray));
        return intersections;
    }

    /***
     * find geointersections is a helper function for interactions between objects
     * @param ray the ray to examine for intersections
     * @return list of intersections
     */
    @Override
    public LinkedList<Intersectable.GeoPoint> findGeoIntersections(Ray ray) {
        LinkedList<Intersectable.GeoPoint> intersections = new LinkedList<>();
        if (_firstTriangle.findGeoIntersections(ray) == null || _firstTriangle.findGeoIntersections(ray).isEmpty()) {
        }
        else
            intersections.addAll(_firstTriangle.findGeoIntersections(ray));
        if (_secondTriangle.findGeoIntersections(ray) == null || _secondTriangle.findGeoIntersections(ray).isEmpty()) {
        }
        else
            intersections.addAll(_secondTriangle.findGeoIntersections(ray));
        return intersections;
    }

    /***
     * sets the color of the quadrangle
     * @param color the color to set to
     * @return Quadrangle for builder-like use
     */
    public Quadrangle setColor(Color color){
        _firstTriangle.setEmission(color);
        _secondTriangle.setEmission(color);
        return this;
    }

    /***
     * setter
     * @param kt transparency
     * @return the object for builder-like use
     */
    public Quadrangle setTransparency(Double3 kt){
        _firstTriangle.getMaterial().setKt(kt);
        _secondTriangle.getMaterial().setKt(kt);
        return this;
    }

    /***
     * setter for reflectivity
     * @param kr reflection
     * @return the object for builder-like use
     */
    public Quadrangle setReflectivity(Double3 kr){
        _firstTriangle.getMaterial().setKr(kr);
        _secondTriangle.getMaterial().setKr(kr);
        return this;
    }

    /***
     * setter
     * @param shine shininess
     * @return the object for builder-like use
     */
    public Quadrangle setShininess(int shine){
        _firstTriangle.getMaterial().setShininess(shine);
        _secondTriangle.getMaterial().setShininess(shine);
        return this;
    }


}
