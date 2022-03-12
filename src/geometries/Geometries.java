package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.*;


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
        List<Point> intersectionsWithAllShapes = null;

        //for each item in the list add the intersections to the list of intersections
        for (var item : _geometries) {
            var itemList = item.findIntersections(ray);
            //if there are(!) intersections with the specific item
            if (itemList != null) {
                //if the list of intersections with everyone is null, then create a new list
                if(intersectionsWithAllShapes == null){
                    intersectionsWithAllShapes = new LinkedList<>();
                }
                //(if there are intersections with item) add list of intersections with item to list of all intersections
                intersectionsWithAllShapes.addAll(itemList);
            }
        }
        return intersectionsWithAllShapes;
    }
}
