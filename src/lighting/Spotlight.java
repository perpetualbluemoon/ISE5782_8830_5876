package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/***
 * class Spotlight is a type of PointLight
 */
public class Spotlight extends PointLight {
    Vector _direction;
    int _narrowBeam;

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

    /***
     * this function calculates the color
     * get light according to mathematical calculations
     *  𝑰𝑳 =point *max(0,l*dir)
     * @param p point on the object
     * @return the color for that point
     */
    @Override
    public Color getIntensity(Point p) {
        Vector l = getL(p); //vector from light to point p
        double max = Math.max(0, _direction.normalize().dotProduct(l));
        return super.getIntensity(p).scale(max); //uses parent function to implement DRY
    }

    /***
     * function for setting the narrowness of the beam, this is never used
     * @param narrowBeam width of beam
     * @return Spotlight for building purposes
     */
    public Spotlight setNarrowBeam(int narrowBeam) {
        _narrowBeam = narrowBeam;
        return this;
    }
}
