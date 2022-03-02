package geometries;

import primitives.Point;
import primitives.Vector;

public class Sphere implements Geometry
{
    final Point p;
    double radius;

    public Sphere(Point p, double radius) {
        this.p = p;
        this.radius = radius;
    }

    public Point getP() {
        return p;
    }

    public double getRadius() {
        return radius;
    }

    @Override
    public String toString() {
        return "Sphere{" +
                "p=" + p +
                ", radius=" + radius +
                '}';
    }

    @Override
    public Vector getNormal(Point point) {
        Vector normal = point.subtract(p);
        return normal.normalize();
    }
}
