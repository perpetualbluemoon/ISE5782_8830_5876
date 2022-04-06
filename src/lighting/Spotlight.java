package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/***
 * class Spotlight is a type of PointLight
 */
public class Spotlight extends PointLight {
    Vector _direction;

    /***
     * constructor for spotlight
     * @param intensity intensity of the light
     * @param position location of the light in the 3D space
     * @param direction of the light
     */
    public Spotlight(Color intensity, Point position, Vector direction) {
        super(intensity, position);
        _direction = direction;
    }

    @Override
    public Color getIntensity(Point p) {
        Vector l = p.subtract(_position).normalize();
        double max = Math.max(0, _direction.normalize().dotProduct(l));
        super.getIntensity(p).scale(max);
        return super.getIntensity(p).scale(max);
    }
}
