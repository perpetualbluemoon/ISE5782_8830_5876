package geometries;

import primitives.Color;
import primitives.Material;
import primitives.Point;
import primitives.Vector;

/***
 * Geometry is an interface for interacting with basic geometric shapes
 * */

public abstract class Geometry extends Intersectable {

    /***
     * color of the object default is black
     */
    protected Color _emission = Color.BLACK;
    /***
     * material of the object
     */
    private Material _material = new Material();

    /***
     * getter
     * @return color of the object
     */
    public Color getEmission() {
        return _emission;
    }

    /***
     * setter
     * @param emission the color for the object
     * @return the object for builder-like use
     */
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

    /***
     * setter
     * @param material of the object
     * @return object for builder-like use
     */
    public Geometry setMaterial(Material material) {
        _material = material;
        return this;
    }

    /***
     *abstract function.
     * @param point get normal from
     * @return returns an orthogonal normalized vector
     */
    public abstract Vector getNormal(Point point);
}
