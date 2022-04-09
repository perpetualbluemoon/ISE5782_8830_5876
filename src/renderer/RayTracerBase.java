package renderer;

import primitives.Color;
import primitives.Ray;
import scene.Scene;

public abstract class RayTracerBase {
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
