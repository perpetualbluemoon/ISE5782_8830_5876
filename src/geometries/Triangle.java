package geometries;

import primitives.Point;
import primitives.Vector;

public class Triangle extends Polygon{
    /***
     * if all three points are in a line
     * @param vertices
     */
    public Triangle(Point... vertices) {
        super(vertices);
        if(vertices.length != 3) {
            throw new IllegalArgumentException("A triangle must have three sides");
        }
    }

    @Override
    public String toString() {
        return "Triangle{" +
                "vertices=" + vertices +
                ", plane=" + plane +
                '}';
    }
}
