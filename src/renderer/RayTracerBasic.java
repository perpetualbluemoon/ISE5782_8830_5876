package renderer;

import geometries.Intersectable;
import geometries.Intersectable.GeoPoint;
import lighting.AmbientLight;
import primitives.Color;
import primitives.Point;
import primitives.Ray;
import scene.Scene;


import java.util.LinkedList;

import static java.lang.System.out;


public class RayTracerBasic extends RayTracerBase {

    public RayTracerBasic(Scene scene) {
        super(scene);
    }

    @Override
    public Color traceRay(Ray ray) {
        LinkedList<GeoPoint> listPointsIntersections = (LinkedList<GeoPoint>) _scene._geometries.findGeoIntersectionsHelper(ray);
        if (listPointsIntersections == null || listPointsIntersections.isEmpty()) {
            return _scene._background;//.add(_scene._ambientLight.getIntensity());
        }

        GeoPoint closestPoint = ray.findClosestGeoPoint(listPointsIntersections);
        //out.print(closestPoint);
        Color thisPixelColor = calcColor(closestPoint);
        return thisPixelColor;
    }

    public Color calcColor(GeoPoint closestPoint) {

        Color pointColor = closestPoint._geoPointGeometry.getEmission();
        pointColor = pointColor.add(_scene._ambientLight.getIntensity());
        return pointColor;
    }


}
