package primitives;

import static primitives.Util.alignZero;

public class Vector extends Point{
    public Vector(double x, double y, double z){
        super(x,y,z);
        if(_xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("creation of Vector (0,0,0)");
    }


    /**
     * vector length squared
     * @return
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
     */
    public Vector crossProduct(Vector other) {

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
}
