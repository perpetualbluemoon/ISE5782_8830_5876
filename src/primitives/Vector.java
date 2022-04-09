package primitives;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/***
 * Clas Vector represents a three dimensional vector. Class vector extends Point.
 */

public class Vector extends Point{
    public Vector(double x, double y, double z){
        super(x,y,z);
        if(_xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("creation of Vector (0,0,0)");
    }

    public Vector(Double3 xyz) {
        super(xyz);
        if(_xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("creation of Vector (0,0,0)");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector vector = (Vector) o;
        return _xyz.equals(vector._xyz);
    }

    @Override
    public String toString() {
        return "Vector{" +
                "_xyz=" + _xyz +
                '}';
    }

    public Vector add(Vector other)
    {
        return new Vector(
                _xyz.d1+other._xyz.d1,
                _xyz.d2+other._xyz.d2,
                _xyz.d3+other._xyz.d3
        );
    }


    /***
     *
     * @param s is a scaler
     * @return a new Vector which is scaled
     */
    public Vector scale(double s){
        return new Vector(s*_xyz.d1,s*_xyz.d2,s*_xyz.d3);
    }

    /**
     * vector length squared
     * @return returns distance
     */
    public double lengthSquared() {
        return _xyz.d1 * _xyz.d1 + _xyz.d2*_xyz.d2 + _xyz.d3*_xyz.d3;
    }

    /**
     * cartesian length of the vector
     * @return the length as a double
     */
    public double length() {
        return Math.sqrt(lengthSquared());
    }

    /**
     * scalar product of two vectors
     * @param other the second vector
     * @return resulting scalar
     */
    public double dotProduct(Vector other) {
        return _xyz.d1*other._xyz.d1+_xyz.d2*other._xyz.d2+_xyz.d3*other._xyz.d3;
    }

    /**
     *
     * @param other second vector
     * @return new Vector resulting from cross product
     *(ax,ay,az)x(bx,by,bz)=(aybz-azby,azbx-axbz,axby-aybx)
     */
    public Vector crossProduct(Vector other) {
        double u1 = _xyz.d1;
        double u2 = _xyz.d2;
        double u3 = _xyz.d3;

        double v1 = other._xyz.d1;
        double v2 = other._xyz.d2;
        double v3 = other._xyz.d3;

        return new Vector(
                u2*v3 - u3*v2,
                u3*v1 - u1*v3,
                u1*v2 - u2*v1
        );

    }

    /***
     *
     * @return new vector which is the original vector normalized
     */
    public Vector normalize() {
        double size = alignZero(length());
        if(isZero(size)){
            throw new ArithmeticException("normalizing resulting in Vector 0"); // check done also by builder
        }
        return new Vector(
                _xyz.d1/size,
                _xyz.d2/size,
                _xyz.d3/size
        );
    }

    /***
     * assistant function which creates vector r to help frequently used calculations
     * @param n vector
     * @return vector r
     */
    public Vector createR(Vector n){
        Vector r = this.subtract(n.scale(2*this.dotProduct(n)));
        return r.normalize();
    }
}
