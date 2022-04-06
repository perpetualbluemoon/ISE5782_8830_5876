package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/***
 * Interface Light source for managing all types of light
 */
public interface LightSource {
    public Color getIntensity(Point p);

    public Vector getL(Point p);
}
