package primitives;

import java.util.Objects;

/***
 * Class Point is the basic class representing a point of euclidean geometry in cartesian
 * three dimensional coordinate system.
 */

public class Point {
    final Double3 _xyz;

    public Point(double d1, double d2, double d3)
    {
        _xyz = new Double3(d1, d2, d3);
    }

    public Point(Double3 xyz) {
        _xyz = xyz;
    }

    public Double3 getXyz() {
        return _xyz;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return _xyz.equals(point._xyz);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_xyz);
    }

    @Override
    public String toString() {
        return "Point{" +
                "_xyz=" + _xyz +
                '}';
    }

    /***
     *
     * @param other is other vector
     * @return my vector minus other vector
     */
    public Vector subtract(Point other) {
        if (other._xyz.equals(_xyz)){
            throw new IllegalArgumentException("Cannot create Vector (0,0,0)");
        }
        return new Vector(
                _xyz.d1 - other._xyz.d1,
                _xyz.d2 - other._xyz.d2,
                _xyz.d3 - other._xyz.d3
        );
    }

    /***
     *
     * @param vector to add to vector
     * @return point result of vector addition
     */
    public Point add(Vector vector) {
        return new Point(
                _xyz.d1 + vector._xyz.d1,
                _xyz.d2 + vector._xyz.d2,
                _xyz.d3 + vector._xyz.d3
        );
    }



    /***
     *
     * @param p one of the points for comparison
     * @return returns the distance between two points
     */
    public double distanceSquared(Point p){
        double a = _xyz.d1 - p._xyz.d1;
        double b = _xyz.d2 - p._xyz.d2;
        double c = _xyz.d3 - p._xyz.d3;

        return a*a + b*b + c*c;
        }

        public double distance(Point p){
        return Math.sqrt(distanceSquared(p));
        }
}
