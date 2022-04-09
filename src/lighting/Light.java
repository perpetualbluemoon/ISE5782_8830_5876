package lighting;

import primitives.Color;

/***
 * Light class
 */
abstract class Light {
    Color _intensity;

    //constructor
    protected Light(Color intensity){
        _intensity = intensity;
    }

    //getter
    public Color getIntensity() {
        return _intensity;
    }
}
