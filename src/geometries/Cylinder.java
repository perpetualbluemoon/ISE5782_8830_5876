package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

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
        return super.getNormal(point);
    }

}
