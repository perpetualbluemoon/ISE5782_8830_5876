package primitives;

import java.util.List;
import java.util.Objects;

import static primitives.Util.isZero;

public class Ray {
   private final Point p0;
   private final Vector dir;

    public Ray(Point p0, Vector dir) {
        this.p0 = p0;
        this.dir = dir.normalize();
    }

    public Point getP0() {
        return p0;
    }

    public Vector getDir() {
        return dir;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ray ray = (Ray) o;
        return Objects.equals(p0, ray.p0) && Objects.equals(dir, ray.dir);
    }

    @Override
    public String toString() {
        return "Ray{" +
                "p0=" + p0 +
                ", dir=" + dir +
                '}';
    }

    public Point getPoint(double t) {
        if(isZero(t))
            return p0;
        return p0.add(dir.normalize().scale(t));
    }

    public Point findClosestPoint(List<Point> pointList){
        //if the list is empty return null, there is no closest point
        if(pointList==null || pointList.isEmpty())
            return null;

        //closest point starts as first point on the list
        Point closestPoint = pointList.get(0);
        //distance initialized to the largest value
        double distance = Double.MAX_VALUE;
        double d;

        //compare the distance of each point, if smaller then update closest point and distance
        for (var pt: pointList){
            d = p0.distance(pt);
            if (d<distance){
                distance = d;
                closestPoint = pt;
            }
        }
        return closestPoint;
    }
}
