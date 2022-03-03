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

    /***
     * @param point point on the tube
     * @return vector normal vector
     */
    @Override
    public Vector getNormal(Point point) {
        //calculating t distance between p0 and point o
        Vector v= point.subtract(axisRay.getP0());
        double t = axisRay.getDir().dotProduct(v);
        Point o;

        // if t==0 point is on base 0 is p0
        if(isZero(t)){
            o=axisRay.getP0();
        }
        else
        {
            o=axisRay.getP0().add(axisRay.getDir().scale(t));
        }
        //calculate and return normal vector
        Vector normal=point.subtract(o);
        return normal.normalize();
    }
}

