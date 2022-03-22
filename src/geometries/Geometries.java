package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.*;
/***
 * Class Geometries implements intersectable and represents a list of intersectables in order to
 * do functions on group of shapes
 */

public class Geometries implements Intersectable{

    LinkedList<Intersectable> _geometries;
//constructor recieves nothing and creates empty list
    public Geometries() {
        _geometries = new LinkedList<>();
    }
//constructor recieves group of intersectables and creates list out of them
    public Geometries(Intersectable... geometries) {
        _geometries = new LinkedList<>();
        Collections.addAll(_geometries, geometries);
    }
    //add function recieves group of intersectables and adds them to list
    public void add(Intersectable... geometries){
        Collections.addAll(_geometries, geometries);
    }


    public LinkedList<Intersectable> getGeometries() {
        return _geometries;
    }
//function finds intersections of all shapes in list with a given ray
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
