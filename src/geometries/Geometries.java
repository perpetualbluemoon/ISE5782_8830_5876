package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.LinkedList;
import java.util.List;


public class Geometries implements Intersectable{

    LinkedList<Intersectable> _geometries;

    public Geometries() {
        _geometries = new LinkedList<>();
    }

    public Geometries(LinkedList<Intersectable> geometries) {
        _geometries = geometries;
    }
    public Geometries(Intersectable... geometries){
            _geometries.addAll(List.of(geometries));

    }
    public void add(Intersectable... geometries){
        _geometries.addAll(List.of(geometries));
    }


    public LinkedList<Intersectable> getGeometries() {
        return _geometries;
    }

    public List<Point> findIntersections(Ray ray) {
        LinkedList<Point> points = new LinkedList<>();

        for (var item : _geometries) {
            var itemList = item.findIntersections(ray);
            if (itemList != null) {
                points.addAll(itemList);
            }
        }
        if (points.isEmpty()){
            return null;
        }
        return points;
    }
}
