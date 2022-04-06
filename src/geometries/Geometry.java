package geometries;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

/***
 * Geometry is an interface for interacting with basic geometric shapes
 * */

public abstract class Geometry extends Intersectable {

    protected Color _emission = Color.BLACK;

    public Color getEmission() {
        return _emission;
    }

    public Geometry setEmission(Color emission) {
        _emission = emission;
        return this;
    }

    /***
     *
     * @param point
     * @return returns an orthogonal normalized vector
     */
    public abstract Vector getNormal(Point point);
}
