package lighting;

import primitives.Color;
import primitives.Double3;

/***
 * Ambient Light for all the objects in the scene
 */
public class AmbientLight extends Light {

    /***
     * Constructor
     * @param Ia basic light intensity
     * @param Ka factor of intensity
     */
    public AmbientLight(Color Ia, Double3 Ka) {
        super(Ia.scale(Ka));
    }

    /***
     * default Constructor
     */
    public AmbientLight() {
        super(Color.BLACK);
    }

}
