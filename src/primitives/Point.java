package primitives;

import java.util.Objects;

public class Point {
    final Double3 _xyz;

    public Point(double d1, double d2, double d3)
    {
        _xyz = new Double3(d1, d2, d3);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return Objects.equals(_xyz, point._xyz);
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
     * @param vector
     * @return
     */
    public Point add(Vector vector) {
        return new Point(
                _xyz.d1 + vector._xyz.d1,
                _xyz.d2 + vector._xyz.d2,
                _xyz.d3 + vector._xyz.d3
        );
    }
}
