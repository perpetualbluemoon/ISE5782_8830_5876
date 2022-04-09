package renderer;

import geometries.Intersectable;
import geometries.Intersectable.GeoPoint;
import lighting.AmbientLight;
import lighting.LightSource;
import primitives.*;
import scene.Scene;


import java.util.LinkedList;

import static java.lang.System.out;
import static primitives.Util.alignZero;
import static primitives.Util.isZero;


public class RayTracerBasic extends RayTracerBase {

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
    private Color calcDiffusive(double kD, Vector l, Vector n, Color lightIntensity) {
       double calcD = kD * Math.abs(l.dotProduct(n));
       return lightIntensity.scale(calcD);
    }

    /***
     * Method which calculates the effects of specular light
     * @return color of the light
     */
    private Color calcSpecular(double kS, Vector l, Vector n, Vector v, int nShininess, Color lightIntensity) {
        Vector r = l.createR(n);
        double x=v.scale(-1).dotProduct(r);
        double max=Math.max(0,x);
        double calcS = kS * Math.pow(max, nShininess);
        return lightIntensity.scale(calcS);
    }

    private Color calcLocalEffectsSimple(GeoPoint intersection, Ray ray) {
        Vector v = ray.getDir().normalize();
        Vector n = intersection._geoPointGeometry.getNormal(intersection._geoPoint).normalize();

        //check if the angle is too wide
        double nv = alignZero(n.dotProduct(v));
        if (isZero(nv)) {
            return Color.BLACK;
        }

        int nShininess = intersection._geoPointGeometry.getMaterial().nShininess;

        //Double3 here has the same value in all 3 spots
        double kD = intersection._geoPointGeometry.getMaterial()._kD.getD1();
        double kS = intersection._geoPointGeometry.getMaterial()._kS.getD1();

        Color color = Color.BLACK;

        for (LightSource lightSource : _scene._lights) {
            Vector l = lightSource.getL(intersection._geoPoint).normalize();
            double nl = alignZero(n.dotProduct(l));


            //if their signs are the same then it is blocking itself from the lgiht
            if (nl * nv > 0) {
                Color lightIntensity = lightSource.getIntensity(intersection._geoPoint);
                Color defused = calcDiffusive(kD, l, n, lightIntensity);
                color = color.add(defused);
                Color specular = calcSpecular(kS, l, n, v, nShininess, lightIntensity);
                color = color.add(specular);
            }
        }
        return color;
    }

    public Color calcColor(GeoPoint closestPoint, Ray ray) {

        Color pointColor = closestPoint._geoPointGeometry.getEmission();
        pointColor = pointColor.add(_scene._ambientLight.getIntensity());
        pointColor = pointColor.add(calcLocalEffectsSimple(closestPoint, ray));

        return pointColor;
    }


}
