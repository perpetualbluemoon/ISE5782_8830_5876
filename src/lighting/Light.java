package lighting;

import primitives.Color;

/***
 * Light class
 */
abstract class Light {
    Color _intensity;

    /**
    *constructor
    */
    protected Light(Color intensity){
        _intensity = intensity;
    }

    /***getter
     * @return intensity
     */
    public Color getIntensity() {
        return _intensity;
    }
}
