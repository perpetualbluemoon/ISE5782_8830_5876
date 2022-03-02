package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static primitives.Util.isZero;

/***
 * Class Tube represents an infinite cylinder using a vector axis and a radius
 */

public class Tube implements Geometry{
    Ray axisRay;
    double radius;

    public Tube(Ray axisRay, double radius) {
        this.axisRay = axisRay;
        this.radius = radius;
    }

    public Ray getAxisRay() {
        return axisRay;
    }

    public double getRadius() {
        return radius;
    }

    @Override
    public String toString() {
        return "Tube{" +
                "axisRay=" + axisRay +
                ", radius=" + radius +
                '}';
    }

    @Override
    public Vector getNormal(Point point) {
        Vector v= point.subtract(axisRay.getP0());
        double t = axisRay.getDir().dotProduct(v);
        Point o;
        if(isZero(t)){
            o=axisRay.getP0();
        }
        else {
            o=axisRay.getP0().add(axisRay.getDir().scale(t));
        }
        Vector normal=point.subtract(o);
        return normal.normalize();
    }
}

