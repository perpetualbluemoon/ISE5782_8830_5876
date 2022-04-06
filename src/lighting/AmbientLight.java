package lighting;

import primitives.Color;
import primitives.Double3;

/***
 * Ambient Light for all the objects in the scene
 */
public class AmbientLight {

    //light intensity as color
    private final Color intensity;

    /***
     * Constructor
     * @param Ia basic light intensity
     * @param Ka factor of intensity
     */
    public AmbientLight(Color Ia, Double3 Ka){
        intensity = Ia.scale(Ka);
    }

    /***
     * default Constructor
     */
    public AmbientLight(){
        intensity = Color.BLACK;
    }

    /***
     * getter for ambient light intensity
     * @return intensity of ambient light
     */
    public Color getIntensity() {
        return intensity;
    }
}
