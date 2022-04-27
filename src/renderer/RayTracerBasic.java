package renderer;

import geometries.Intersectable;
import geometries.Intersectable.GeoPoint;
import lighting.AmbientLight;
import lighting.LightSource;
import primitives.*;
import scene.Scene;


import java.util.LinkedList;
import java.util.List;

import static java.lang.System.out;
import static primitives.Util.alignZero;
import static primitives.Util.isZero;


public class RayTracerBasic extends RayTracerBase {

    //added in part7 in order to support shadows
    private static final double DELTA = 0.1;
    private static final double EPSILON = 0.001;

    public RayTracerBasic(Scene scene) {
        super(scene);
    }

    /***
     * @param ray ray from the camera through the viewplane
     * @return the color of the closest point among the list of intersections
     */
    @Override
    public Color traceRay(Ray ray) {
        LinkedList<GeoPoint> listPointsIntersections = (LinkedList<GeoPoint>) _scene._geometries.findGeoIntersectionsHelper(ray);
        if (listPointsIntersections == null || listPointsIntersections.isEmpty()) {
            return _scene._background;//.add(_scene._ambientLight.getIntensity());
        }

        GeoPoint closestPoint = ray.findClosestGeoPoint(listPointsIntersections);
        //out.print(closestPoint);
        Color thisPixelColor = calcColor(closestPoint, ray);
        return thisPixelColor;
    }

    /***
     * Method which calculates the effects of deffusive light
     * @return color of the light
     */
    private double calcDiffusive(double kD, double nl) {
        //according to the phong model
        double calcD = kD*Math.abs(nl);
        return calcD;
    }

    /***
     * Method which calculates the effects of specular light
     * @return color of the light
     */
    private double calcSpecular(double kS, Vector n, Vector l,  double nl, Vector v, double shininess) {
        //according to the phong model
        Vector r =  l.createR(n);
        double max = Math.max(0, v.scale(-1).dotProduct(r));
        double calcS = kS*Math.pow(max, shininess);
        return calcS;
    }

    /***
     * Mathematical calculations according to the slides from the fist semester
     * @param intersection the point for which we want the color
     * @param ray ray from the camera
     * @return the color of the point
     */
    private Color calcLocalEffectsSimple(GeoPoint intersection, Ray ray) {
        Color color = intersection._geoPointGeometry.getEmission();
        Vector v = ray.getDir();
        Vector n = intersection._geoPointGeometry.getNormal(intersection._geoPoint);
        double nv = alignZero(n.dotProduct(v));
        if (nv == 0) return color;
        Material material = intersection._geoPointGeometry.getMaterial();
        for (LightSource lightSource : _scene._lights) {
            Vector l = lightSource.getL(intersection._geoPoint);
            double nl = alignZero(n.dotProduct(l));
            if (nl * nv > 0) { // sign(nl) == sing(nv)
                if(unshaded(intersection,l,n,lightSource)) {
                    Color iL = lightSource.getIntensity(intersection._geoPoint);
                    color = color.add(iL.scale(calcDiffusive(material._kD.getD1(), nl)),
                            iL.scale(calcSpecular(material._kS.getD1(), n, l, nl, v, material.nShininess)));
                }
            }
        }
        return color;

    }

    /***
     *
     * @param closestPoint the point that we want to find the color of
     * @param ray the ray from the camera through a pixel
     * @return the color for that point
     */
    public Color calcColor(GeoPoint closestPoint, Ray ray) {

        //ambient light
        Color pointColor = _scene._ambientLight.getIntensity();
        //adding the local effects according to the phong model, including emissions
        pointColor = pointColor.add(calcLocalEffectsSimple(closestPoint, ray));

        return pointColor;
    }

    /***
     * this function tells us whether a certain point is shaded or not
     * @param gp the point to check
     * @param l the vector from the light to the point
     * @param n normal to that point
     * @return true if the point is unshaded
     */
    private boolean unshaded(GeoPoint gp, Vector l, Vector n, LightSource lightSource){
        Vector lightDirection = l.scale(-1); //from point to light source
        //took a point adding epsilon in the direction of the normal
        Point rayHead;
        if(n.dotProduct(lightDirection)>0)
            rayHead = gp._geoPoint.add(n.scale(EPSILON));
        else
            rayHead = gp._geoPoint.add(n.scale(-EPSILON));
        Ray lightRay = new Ray(rayHead, lightDirection);
        //checks for intersections between the point and the light
        List<GeoPoint> intersections = _scene._geometries.findGeoIntersections(lightRay);
        //if there is nothing between the point and the light then the point is unshaded
        if(intersections == null)
            return true;
        if(intersections.isEmpty())
            return true;
        double distance=lightSource.getDistance(gp._geoPoint);
        for (GeoPoint intersection: intersections ){
            if(gp._geoPoint.distance(intersection._geoPoint)<distance)
                return false;
        }
        return true;
    }
}
