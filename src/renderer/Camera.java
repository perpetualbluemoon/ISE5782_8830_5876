
package renderer;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;

public class Camera {
    private Vector _Vright;//y
    private Vector _Vup;//x
    private Vector _Vto;//z
    private Point _centerCam;
    private double  _heightVP;
    private double _widthVP;
    private double _distanceVPToCam;

    public Camera( Point centerCam,Vector vto, Vector vup) {
        _Vup = vup.normalize();
        _Vto = vto.normalize();

        Vector crossP;
        //if vectors are parallel Vector will throw an exception
        //we will catch it and throw an exception explaining that the vectors are parallel
        try {
            crossP = vto.crossProduct(vup);
        }
        catch(Exception e){
            throw new IllegalArgumentException("Vectors are parallel");
        }

        _Vright=crossP.normalize();
        _centerCam = centerCam;
    }

    public Camera setVPDistance(double distanceVPToCam) {
        _distanceVPToCam = distanceVPToCam;
        return this;
    }
    public Camera setVPSize(double width, double height){
        _widthVP=width;
        _heightVP=height;
        return this;
    }
    public Vector getVright() {
        return _Vright;
    }

    public Vector getVup() {
        return _Vup;
    }

    public Vector getVto() {
        return _Vto;
    }

    public Point getCenterCam() {
        return _centerCam;
    }

    public double getHeightVP() {
        return _heightVP;
    }

    public double getWidthVP() {
        return _widthVP;
    }

    public double getDistanceVPToCam() {
        return _distanceVPToCam;
    }

    /***
     * function creates a ray through the pixel
     * @param nX number of columns
     * @param nY number of rows
     * @param j current column
     * @param i current row
     * @return ray through that pixel
     */
    public Ray constructRay(int nX, int nY, int j, int i) {
       //calculating center point
       //you cannot use getPoint because there is no ray
        Point pc= _centerCam.add(_Vto.scale(_distanceVPToCam));
        double ry = _heightVP/nY;
        double rx = _widthVP/nX;

        double xj = (j - ((nX - 1)/2.0))*rx;
        double yi = -(i - ((nY - 1)/2.0))*ry;

        //Pij = Pc + (xj*Vright + yi*Vup)
        // finding point pij according to the formula
        Point Pij = pc;
        if(xj!=0)
        {
            Pij = Pij.add(getVright().scale(xj) );
        }
        if(yi!=0){
           Pij = Pij.add(getVup().scale(yi));
        }
        //pc!=p0 so subtract will not create vector 0
        Vector Vij = Pij.subtract(_centerCam);
        Ray rayReturn=new Ray(_centerCam,Vij);
        return rayReturn;
    }
}