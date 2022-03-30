
package renderer;

import primitives.*;
/***
 * class of camera helps constructing rays through view plane pixels and find intersections
 */
public class Camera {
    private Vector _Vright;//y
    private Vector _Vup;//x
    private Vector _Vto;//z
    private Point _centerCam;
    private double  _heightVP;
    private double _widthVP;
    private double _distanceVPToCam;
    //constructor receives @param  Point centerCam,Vector vto, Vector vup and creates camera
    public Camera( Point centerCam,Vector vto, Vector vup) {
        _Vup = vup.normalize();
        _Vto = vto.normalize();
       //find third direction
        Vector crossP;
        //if vectors are parallel Vector will throw an exception
        //we will catch it and throw an exception explaining that the vectors are parallel
        try {
            crossP = vto.crossProduct(vup);
        }
        catch(Exception e){
            throw new IllegalArgumentException("Vectors are parallel");
        }
        //if did not throw save third direction
        _Vright=crossP.normalize();
        _centerCam = centerCam;
    }
    /*function setter
    *@param double distanceVPToCam for distance of view plane from camera
     @returns current object*/
    public Camera setVPDistance(double distanceVPToCam) {
        _distanceVPToCam = distanceVPToCam;
        return this;
    }
    /*
    function setter
    @param double width-width for size of view plane
    @param double height -height for size of view plane
    @returns current object
     */
    public Camera setVPSize(double width, double height){
        _widthVP=width;
        _heightVP=height;
        return this;
    }
    //getters
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
        //𝑃𝑐 = 𝑃0 + 𝑑 ∙ 𝑣
        //finding middle of view plane
        Point pc= _centerCam.add(_Vto.scale(_distanceVPToCam));
        //𝑅𝑦 = ℎ/𝑁𝑦
        //𝑅𝑥 = 𝑤/𝑁x
        //calculating size of pixels for width and height
        double ry = _heightVP/nY;
        double rx = _widthVP/nX;
        //finding how much to move up and right to find wanted pixel
        double xj = (j - ((nX - 1)/2.0))*rx;
        double yi = -(i - ((nY - 1)/2.0))*ry;

        //Pij = Pc + (xj*Vright + yi*Vup)
        //𝑦𝑖 = −(𝑖 – (𝑁𝑦 − 1)/2) ∙ 𝑅𝑦
        // 𝑥𝑗 = (𝑗 – (𝑁𝑥 − 1)/2) ∙ 𝑅x
        // finding point pij wanted pixel according to the formula
        //doing it in parts to prevent addition of zero
        Point Pij = pc;
        if(xj!=0)
        {
            //xj*Vright
            Pij = Pij.add(getVright().scale(xj) );
        }
        if(yi!=0){
            //yi*Vup
           Pij = Pij.add(getVup().scale(yi));
        }
        //pc!=p0 so subtract will not create vector 0
        Vector Vij = Pij.subtract(_centerCam);
        //𝒗𝒊,𝒋 = 𝑷𝒊,𝒋 − 𝑷0
        Ray rayReturn=new Ray(_centerCam,Vij);
        return rayReturn;
    }

    public Camera setImageWriter(ImageWriter base_render_test) {
    return null;
    }
}