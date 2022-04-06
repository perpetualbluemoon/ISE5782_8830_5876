package geometries;

import primitives.*;

import java.util.List;

/***
 * Geometry is an interface for interacting with basic geometric shapes
 * */

public abstract class Geometry extends Intersectable {

    protected Color _emission = Color.BLACK;
    private Material _material = new Material();

    public Color getEmission() {
        return _emission;
    }

    public Geometry setEmission(Color emission) {
        _emission = emission;
        return this;
    }

    /***
     * getter for field material
     * @return material
     */
    public Material getMaterial() {
        return _material;
    }

    public Geometry setMaterial(Material material) {
        _material = material;
        return this;
    }

    /***
     *
     * @param point
     * @return returns an orthogonal normalized vector
     */
    public abstract Vector getNormal(Point point);
}
