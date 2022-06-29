package renderer;

import primitives.Color;
import primitives.Ray;
import scene.Scene;

/***
 * class RayTracerBase is the parent class to RayTracerBasic and as the basic functions for ray tracing
 */
public abstract class RayTracerBase {
    /***
     * scene after building
     */
    protected Scene _scene;

    /***
     * Constructor
     * @param scene creates scene
     */
    public RayTracerBase(Scene scene) {
        _scene = scene;
    }

    /***
     *
     * @param ray ray through the viewplane
     * @return color of the point
     */
    public abstract Color traceRay(Ray ray);


}
