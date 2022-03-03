package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static primitives.Util.isZero;

/***
 * Class Cylinder extends class Tube and represents the geometrical shape of a cylinder using height
 * and the attributes of the parent class
 */

public class Cylinder extends Tube{
    double height;

    public Cylinder(Ray axisRay, double radius, double height) {
        super(axisRay, radius);
        this.height = height;
    }

    public double getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return "Cylinder{" +
                "height=" + height +
                ", axisRay=" + axisRay +
                ", radius=" + radius +
                '}';
    }

    /***
     *
     * @param point point on the cylinder
     * @return vector normal vector
     */
    @Override
    public Vector getNormal(Point point) {
        //check if the point is the base of the axis ray - if it is return dir
        if (point.equals(axisRay.getP0())){
            return axisRay.getDir().normalize();
        }

        //calculate t distance between base of axis ray and point o
        Vector v = point.subtract(axisRay.getP0());
        double t = axisRay.getDir().dotProduct(v);

        // if t == 0 the point is on the bottom base but not the center - return dir
        // if t == h the point is on the top base - return dir
        if (isZero(t) || isZero(t - height)) {
            return axisRay.getDir().normalize();
        }
        else
        //use the parent function
        {
            return super.getNormal(point);
        }
    }
}
