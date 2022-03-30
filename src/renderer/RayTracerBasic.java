package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import scene.Scene;

import java.util.LinkedList;


public class RayTracerBasic extends RayTracerBase{

    public RayTracerBasic(Scene scene){
        super(scene);
    }

    @Override
    public Color traceRay(Ray ray) {
       LinkedList<Point> listPointsIntersections = (LinkedList<Point>) _scene._geometries.findIntersections(ray);
       if(listPointsIntersections ==null || listPointsIntersections.isEmpty()){
           return _scene._background;//.add(_scene._ambientLight.getIntensity());
       }
       Point closestPoint = ray.findClosestPoint(listPointsIntersections);
       Color thisPixelColor = calcColor(closestPoint);
        return thisPixelColor;
    }

    public Color calcColor(Point closestPoint){
        Color pointColor = new Color (java.awt.Color.PINK);
        return pointColor;
    }


}
