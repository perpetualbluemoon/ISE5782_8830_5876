package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class Geometries implements Intersectable{

    LinkedList<Intersectable> _geometries;

    public Geometries() {
        _geometries = new LinkedList<>();
    }

    public Geometries(Intersectable... geometries) {
        _geometries = new LinkedList<>();
        Collections.addAll(_geometries, geometries);
    }

    public void add(Intersectable... geometries){
        Collections.addAll(_geometries, geometries);
    }


    public LinkedList<Intersectable> getGeometries() {
        return _geometries;
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        List<Point> points = null;

        //for each item in the list add the intersections to the list of intersections
        for (var item : _geometries) {
            var itemList = item.findIntersections(ray);
            if (itemList != null) {
                if(points == null){
                    points = new LinkedList<>();
                }
                points.addAll(itemList);
            }
        }
        return points;
    }
}
