package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static primitives.Util.isZero;

/***
 * Class Cylinder extends class Tube and represents the geometrical shape of a cylinder using height and the attributes
 * of the parent class
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

    @Override
    public Vector getNormal(Point point) {
        if (point == axisRay.getP0()){
            return axisRay.getDir().normalize();
        }
        Vector v= point.subtract(axisRay.getP0());
        double t = axisRay.getDir().dotProduct(v);

        if(isZero(t)||isZero(t-height)){
            return axisRay.getDir().normalize();
        }
        else {
            return super.getNormal(point);
        }


    }

}
