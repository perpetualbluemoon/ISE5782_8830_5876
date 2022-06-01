package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.LinkedList;
import java.util.MissingResourceException;

import static java.lang.System.out;
import static primitives.Util.isZero;

/***
 * class of camera helps constructing rays through view plane pixels and find intersections
 */
public class Camera {

    //coordinates of the camera
    private Vector _Vright;//y
    private Vector _Vup;//x
    private Vector _Vto;//z
    private Point _centerCam;


    private static boolean _MultiThreadingButton = false;
    private static int NUM_OF_THREADS = 1;
    private double _debugPrint = 0;


    //information about the view plane
    private double _heightVP;
    private double _widthVP;
    private double _distanceVPToCam;

    //information about creation of the final image
    private ImageWriter _imageWriter; //creates the photo
    private RayTracerBase _rayTracerBase;

    //ON/OFF button default is off
    private boolean _depthButton = false;

    //focal length
    private double _focalLength = 2;
    private double _apertureSize = 0.01;

    private static final int NUMBER_OF_APERTURE_POINTS = 3;


    //ON/OFF button default is off
    private boolean _JaggedEdgesButton = false;
    //number of minipixels is used for both height and width of the minipixel for simplicity
    private static int NUMBER_OF_MINIPIXELS = 3;

    public static int MAX_RECURSION = 3;


    //constructor receives @param  Point centerCam,Vector vto, Vector vup and creates camera
    public Camera(Point centerCam, Vector vto, Vector vup) {
        _Vup = vup.normalize();
        _Vto = vto.normalize();
        //find third direction
        Vector crossP;
        //if vectors are parallel Vector will throw an exception
        //we will catch it and throw an exception explaining that the vectors are parallel
        try {
            crossP = vto.crossProduct(vup);
        } catch (Exception e) {
            throw new IllegalArgumentException("Vectors are parallel");
        }
        //if did not throw save third direction
        _Vright = crossP.normalize();
        _centerCam = centerCam;
    }

    //setters

    /***function setter
     *@param distanceVPToCam for distance of view plane from camera
     @returns current object - camera
     */
    public Camera setVPDistance(double distanceVPToCam) {
        _distanceVPToCam = distanceVPToCam;
        return this;
    }

    /***
     function setter
     @param width-width for size of view plane
     @param height -height for size of view plane
     @returns current object - camera
     */
    public Camera setVPSize(double width, double height) {
        _widthVP = width;
        _heightVP = height;
        return this;
    }

    /***
     * set image writer
     * @param imageWriter creates the photo
     * @return the camera
     */
    public Camera setImageWriter(ImageWriter imageWriter) {
        _imageWriter = imageWriter;
        return this;
    }

    /***
     * set ray tracer
     * @param rayTracerBase finds color
     * @return the camera
     */
    public Camera setRayTracerBase(RayTracerBase rayTracerBase) {
        _rayTracerBase = rayTracerBase;
        return this;
    }

    public Camera setFocalLength(double focalLength) {
        _focalLength = focalLength;
        return this;
    }

    public Camera setApertureSize(double apertureSize) {
        _apertureSize = apertureSize;
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
     * function render Image checks that all the fields are initialized
     */
    public Camera renderImage() {
        //coordinates of the camera are not null
        if ((_Vright == null) || (_Vup == null) || (_Vto == null) || (_centerCam == null))
            throw new MissingResourceException("Camera coordinates are not initialized", "Camera", "coordinates");

        //information about the view plane does not need to be checked because double cannot be null

        //information about creation of the final image
        if ((_imageWriter == null) || (_rayTracerBase == null))
            throw new MissingResourceException("Image creation details are not initialized", "Camera", "Writer details");
        if (_MultiThreadingButton) {
            Pixel.initialize(_imageWriter.getNy(), _imageWriter.getNx(), _debugPrint); //debug print is print interval
            int threadsCount = 3;
            while (threadsCount-- > 0) {
                new Thread(() -> {
                    for (Pixel pixel = new Pixel(); pixel.nextPixel(); Pixel.pixelDone()) {
                        Color thisPixelColor = castRay(pixel.col, pixel.row);
                        _imageWriter.writePixel(pixel.col, pixel.row, thisPixelColor);
                    }
                }).start();
            }
            Pixel.waitToFinish();

        } else {
            //for every row
            for (int i = 0; i < _imageWriter.getNy(); i++) {
                //for every column
                for (int j = 0; j < _imageWriter.getNx(); j++) {
                    Color thisPixelColor = castRay(j, i);
                    _imageWriter.writePixel(j, i, thisPixelColor);
                }
            }
        }
        return this;
    }

    /***
     * function printGrid places a grid
     * @param interval size of the squares in the grid
     * @param color color of the grid
     */
    public void printGrid(int interval, Color color) {
        if (_imageWriter == null)
            throw new MissingResourceException("Image creation details are not initialized", "Camera", "Writer details");
        //for every row
        for (int i = 0; i < _imageWriter.getNx(); i++) {
            //for every column
            for (int j = 0; j < _imageWriter.getNy(); j++) {
                //grid: 800/50 = 16, 500/50 = 10
                if ((i % interval == 0) || (j % interval == 0)) {
                    _imageWriter.writePixel(i, j, color);
                }
            }
        }
    }

    /***
     * function checks the parameters and then calls function from class ImageWriter to create the image
     */
    public void writeToImage() {
        //checks that the image writer field has been initialized
        if (_imageWriter == null)
            throw new MissingResourceException("Image creation details are not initialized", "Camera", "Writer details");
        //calls writer function from class ImageWriter in renderer
        _imageWriter.writeToImage();
    }

    public Camera setRayTracer(RayTracerBasic rayTracerBasic) {
        _rayTracerBase = rayTracerBasic;
        return this;
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
        Point Pij = createMiddlePixel(nX, nY, j, i);
        //pc!=p0 so subtract will not create vector 0
        Vector Vij = Pij.subtract(_centerCam);
        //𝒗𝒊,𝒋 = 𝑷𝒊,𝒋 − 𝑷0
        Ray rayReturn = new Ray(_centerCam, Vij);
        return rayReturn;
    }

    /***
     * this function creates a ray from the aperture point (which changes multiple times in one pixel)
     * the ray goes through the focal point (calculated one time per pixel)
     * @param aperturePoint a point near the Camera moved slightly according to the aperture size
     * @param focalPoint the point of focus directly from the camera through the pixel according to the focal length
     * @return the ray
     */
    public Ray constructDepthRay(Point aperturePoint, Point focalPoint) {
        Vector direction = focalPoint.subtract(aperturePoint);
        Ray depthRay = new Ray(aperturePoint, direction);
        return depthRay;
    }


    /***
     * cast ray is the main function in which the modifications of part 8 are made
     * for this reason, there are multiple clauses checking which fix we are currently in
     * this function casts a ray from the camera through the current pixel and returns the color of that pixel
     * @param j the column of the pixel
     * @param i the row of the pixel
     * @return color of the pixel
     */
    public Color castRayOld(int j, int i) {

        if ((!_depthButton) && (!_JaggedEdgesButton)) { //default code
            Ray thisPixelRay = constructRay(_imageWriter.getNx(), _imageWriter.getNy(), j, i);

            Color thisPixelColor = _rayTracerBase.traceRay(thisPixelRay);

            return thisPixelColor;
        }

        if (_JaggedEdgesButton) {
            //inital color for average calculation
            double colorX = 0;
            double colorY = 0;
            double colorZ = 0;

            Point thisPixelPoint = createMiddlePixel(_imageWriter.getNx(), _imageWriter.getNy(), j, i);
            // out.print(thisPixelPoint);
            double heightOfMiniPixel = (_heightVP / _imageWriter.getNy()) / NUMBER_OF_MINIPIXELS;
            double widthOfMiniPixel = (_widthVP / _imageWriter.getNx()) / NUMBER_OF_MINIPIXELS;

            //creating a point in the top left corner that is slightly outside the square so that our loop will bring it
            Point outerTopLeftCorner = thisPixelPoint.add(_Vup.scale(heightOfMiniPixel * 0.5));
            outerTopLeftCorner = outerTopLeftCorner.add(_Vright.scale(widthOfMiniPixel * 0.5));

            LinkedList<Point> minipixelPoints = thisPixelPoint.createListOfMovedPoints(outerTopLeftCorner, _Vup, _Vright, _heightVP / _imageWriter.getNy()
                    , _widthVP / _imageWriter.getNx(), NUMBER_OF_MINIPIXELS);
            //for each mini pixel
            for (Point movedPoint : minipixelPoints) {

                Vector rayDirectionFromMiniPixel = _centerCam.subtract(movedPoint).normalize();
                Ray jaggedEdgesRay = new Ray(_centerCam, rayDirectionFromMiniPixel.scale(-1));

                //tracing ray for color
                Color thisPointColor = _rayTracerBase.traceRay(jaggedEdgesRay);
                //out.print(thisPointColor);
                //adding the colors for average
                colorX += thisPointColor.getColor().getRed();
                colorY += thisPointColor.getColor().getGreen();
                colorZ += thisPointColor.getColor().getBlue();
                //out.print(_centerCam.subtract(movedPoint).normalize());
            }


            //calculating the average - dividing by the number of mini pixels squared because of the double loop

            double averageX = colorX / (NUMBER_OF_MINIPIXELS * NUMBER_OF_MINIPIXELS);
            double averageY = colorY / (NUMBER_OF_MINIPIXELS * NUMBER_OF_MINIPIXELS);
            double averageZ = colorZ / (NUMBER_OF_MINIPIXELS * NUMBER_OF_MINIPIXELS);

            Color thisPixelColor = new Color(averageX, averageY, averageZ);
            return thisPixelColor;

        }

        if (_depthButton) { //on/off button
            //one time calculate focal point
            Ray thisPixelRay = constructRay(_imageWriter.getNx(), _imageWriter.getNy(), j, i);
            Point focalPoint = _centerCam.add(thisPixelRay.getDir().scale(_focalLength));
            //list of coordinates from each color for calculating the final color
            double colorX = 0;
            double colorY = 0;
            double colorZ = 0;

            //for: randomly calculate aperture point (point near camera origin moved by max aperture amount)


            //     construct depth ray from the aperture point to the focal point
            //     get the color using trace ray and add to list
            double heightOfMiniPixel = _apertureSize / NUMBER_OF_APERTURE_POINTS;
            double widthOfMiniPixel = _apertureSize / NUMBER_OF_APERTURE_POINTS;
            Point outerTopLeftCorner = _centerCam.add(_Vup.scale(heightOfMiniPixel * 0.5));
            outerTopLeftCorner = outerTopLeftCorner.add(_Vright.scale(widthOfMiniPixel * 0.5));
            //helper function returns one random point around the center
            LinkedList<Point> listPixelPoints = _centerCam.createListOfMovedPoints(outerTopLeftCorner, _Vup, _Vright, _apertureSize
                    , _apertureSize, NUMBER_OF_APERTURE_POINTS);
            for (Point movedPoint : listPixelPoints) {

                Ray depthRay = constructDepthRay(movedPoint, focalPoint);
                Color thisPointColor = _rayTracerBase.traceRay(depthRay);

                colorX += thisPointColor.getColor().getRed();
                colorY += thisPointColor.getColor().getGreen();
                colorZ += thisPointColor.getColor().getBlue();

            }
            // out of the for: calculate average of x,y,z for the colors (x,y,z)

            double averageX = colorX / (NUMBER_OF_APERTURE_POINTS * NUMBER_OF_APERTURE_POINTS);
            double averageY = colorY / (NUMBER_OF_APERTURE_POINTS * NUMBER_OF_APERTURE_POINTS);
            double averageZ = colorZ / (NUMBER_OF_APERTURE_POINTS * NUMBER_OF_APERTURE_POINTS);

            Color thisPixelColor = new Color(averageX, averageY, averageZ);
            return thisPixelColor;
        }
        return Color.BLACK;
    }

    public void setDepthButton(boolean button, double apertureSize, double focalLength) {
        _depthButton = button;
        _apertureSize = apertureSize;
        _focalLength = focalLength;
    }

    public void setDepthButton(boolean depthButton) {
        _depthButton = depthButton;
    }

    public void setJaggedEdgesButton(boolean jaggedEdgesButton, int numberOfMiniPixels) {
        _JaggedEdgesButton = jaggedEdgesButton;
        NUMBER_OF_MINIPIXELS = numberOfMiniPixels;
    }

    public void setJaggedEdgesButton(boolean jaggedEdgesButton) {
        _JaggedEdgesButton = jaggedEdgesButton;
    }


    /***
     * this function recieves the parameters of a pixel and returns a point in the middle of the pixel
     * @param nX number of columns
     * @param nY number of rows
     * @param j current column
     * @param i current row
     * @return middle of pixel
     */
    public Point createMiddlePixel(int nX, int nY, int j, int i) {
        // calculating center point
        //you cannot use getPoint because there is no ray
        //𝑃𝑐 = 𝑃0 + 𝑑 ∙ 𝑣
        //finding middle of view plane
        Point pc = _centerCam.add(_Vto.scale(_distanceVPToCam));
        //𝑅𝑦 = ℎ/𝑁𝑦{
        //        //
        //𝑅𝑥 = 𝑤/𝑁x
        //calculating size of pixels for width and height
        double ry = _heightVP / nY;
        double rx = _widthVP / nX;
        //finding how much to move up and right to find wanted pixel
        double xj = (j - ((nX - 1) / 2.0)) * rx;
        double yi = -(i - ((nY - 1) / 2.0)) * ry;

        //Pij = Pc + (xj*Vright + yi*Vup)
        //𝑦𝑖 = −(𝑖 – (𝑁𝑦 − 1)/2) ∙ 𝑅𝑦
        // 𝑥𝑗 = (𝑗 – (𝑁𝑥 − 1)/2) ∙ 𝑅x
        // finding point pij wanted pixel according to the formula
        //doing it in parts to prevent addition of zero
        Point Pij = pc;
        if (!isZero(xj)) {
            //xj*Vright
            Pij = Pij.add(_Vright.scale(xj));
        }
        if (!isZero(yi)) {
            //yi*Vup
            Pij = Pij.add(_Vup.scale(yi));
        }

        return Pij;
    }

    public Camera setMultithreading(int i) {
        if (i > 1)
            _MultiThreadingButton = true;
        NUM_OF_THREADS = i;
        return this;
    }

    public Camera setDebugPrint(double v) {
        //?? assuming debug print is the same as print interval
        _debugPrint = v;
        return this;
    }

    /***
     * this function casts a ray from the camera through the current pixel and returns the color of that pixel
     * @param j the column of the pixel
     * @param i the row of the pixel
     * @return color of the pixel
     */
    public Color castRay(int j, int i) {

        if ((!_depthButton) && (!_JaggedEdgesButton)) { //default code
            Ray thisPixelRay = constructRay(_imageWriter.getNx(), _imageWriter.getNy(), j, i);

            Color thisPixelColor = _rayTracerBase.traceRay(thisPixelRay);

            return thisPixelColor;
        }
        if (_JaggedEdgesButton) {
            //inital color for average calculation
            double colorX = 0;
            double colorY = 0;
            double colorZ = 0;

            Point thisPixelPoint = createMiddlePixel(_imageWriter.getNx(), _imageWriter.getNy(), j, i);
            // out.print(thisPixelPoint);

            //creating a point in the top left corner that is slightly outside the square so that our loop will bring it
            Point topLeftCorner = thisPixelPoint;
            Color topLeftColor = _rayTracerBase.traceRay(new Ray(_centerCam, topLeftCorner.subtract(_centerCam).normalize()));


            Point bottomRightCorner = thisPixelPoint.add(_Vup.scale((_heightVP / _imageWriter.getNy()) * -1));
            bottomRightCorner = bottomRightCorner.add(_Vright.scale((_widthVP / _imageWriter.getNx())));
            Color bottomRightColor = _rayTracerBase.traceRay(new Ray(_centerCam, bottomRightCorner.subtract(_centerCam).normalize()));

            //out.print(bottomRightColor+"\n");
            Color fullPixelColor = recursivePixelColor(topLeftCorner, topLeftColor, bottomRightCorner, bottomRightColor,
                    _Vup, _Vright, "left", 0, (_heightVP / _imageWriter.getNy()), (_widthVP / _imageWriter.getNx()));

            return fullPixelColor;


        }
        return Color.BLACK;
    }

    /***
     *
     * @param upperCorner upper left or right corner of pixel section
     * @param upperColor color of upper corner
     * @param bottomCorner bottom left or right corner of pixel section
     * @param bottomColor color of bottom corner
     * @param Vup vector going up (camera coordinates)
     * @param Vright vector going right (camera coordinates)
     * @param leftRight whether upper corner is to the left or the right of the bottom corner
     * @param recursionNum number of the current recursion
     * @return Color of the pixel section
     */
    public Color recursivePixelColor(Point upperCorner, Color upperColor, Point bottomCorner, Color bottomColor,
                                     Vector Vup, Vector Vright, String leftRight, int recursionNum, double widthOfPixelSection,
                                     double heightOfPixelSection) {

        if (recursionNum == MAX_RECURSION)
            return upperColor;

       // out.print(recursionNum);

        Point leftTopPoint;
        Point rightTopPoint;
        Point leftBottomPoint;
        Point rightBottomPoint;
        Point middlePoint;

        Color leftTopColor;
        Color rightTopColor;
        Color leftBottomColor;
        Color rightBottomColor;
        //we calculated colors inside because we dont know who is upper yet
        if (leftRight == "left") {
            middlePoint = upperCorner.add(_Vup.scale(heightOfPixelSection * -0.5));
            middlePoint = middlePoint.add(_Vright.scale(widthOfPixelSection * 0.5)).movePointRandom(_Vup, _Vright, widthOfPixelSection/4);

            rightBottomPoint = bottomCorner.movePointRandom(_Vup, _Vright, widthOfPixelSection/4);
            leftTopPoint = upperCorner.movePointRandom(_Vup, _Vright, widthOfPixelSection/4);
            rightTopPoint = upperCorner.add(_Vright.scale(widthOfPixelSection)).movePointRandom(_Vup, _Vright, widthOfPixelSection/4);
            leftBottomPoint = upperCorner.add(_Vup.scale(-1 * heightOfPixelSection)).movePointRandom(_Vup, _Vright, widthOfPixelSection/4);

            leftTopColor = upperColor;
            rightBottomColor = bottomColor;
            rightTopColor = _rayTracerBase.traceRay(new Ray(_centerCam, rightTopPoint.subtract(_centerCam).normalize()));
            //if(!rightTopColor.equals(Color.MAGENTA)){
              //  out.print(rightTopColor);
            //}
            leftBottomColor = _rayTracerBase.traceRay(new Ray(_centerCam, leftBottomPoint.subtract(_centerCam).normalize()));

        } else {
            middlePoint = upperCorner.add(_Vup.scale(heightOfPixelSection * -0.5));
            middlePoint = middlePoint.add(_Vright.scale(widthOfPixelSection * -0.5)).movePointRandom(_Vup, _Vright, widthOfPixelSection/4);

            leftBottomPoint = bottomCorner.movePointRandom(_Vup, _Vright, widthOfPixelSection/4);
            rightTopPoint = upperCorner.movePointRandom(_Vup, _Vright, widthOfPixelSection/4);
            leftTopPoint = upperCorner.add(_Vright.scale(-1 * widthOfPixelSection)).movePointRandom(_Vup, _Vright, widthOfPixelSection/4);
            rightBottomPoint = upperCorner.add(_Vup.scale(-1 * heightOfPixelSection)).movePointRandom(_Vup, _Vright, widthOfPixelSection/4);


            rightTopColor = upperColor;
            leftBottomColor = bottomColor;
            leftTopColor = _rayTracerBase.traceRay(new Ray(_centerCam, leftTopPoint.subtract(_centerCam).normalize()));
            rightBottomColor = _rayTracerBase.traceRay(new Ray(_centerCam, rightBottomPoint.subtract(_centerCam).normalize()));
        }




        Color middleColor = _rayTracerBase.traceRay(new Ray(_centerCam, middlePoint.subtract(_centerCam).normalize()));
       // if(!middleColor.equals(Color.MAGENTA)){
         // out.print(middleColor);
       // }
        Color topRight = middleColor;
        if (!middleColor.equals(rightTopColor)) {

            topRight = recursivePixelColor(rightTopPoint, rightTopColor, middlePoint, middleColor, Vup, Vright, "right",
                    recursionNum + 1, widthOfPixelSection / 2, heightOfPixelSection / 2);
        // out.print(topRight);
        }

        Color topLeft = middleColor;
        if (!middleColor.equals(leftTopColor)) {
            topLeft = recursivePixelColor(leftTopPoint, leftTopColor, middlePoint, middleColor, Vup, Vright, "left",
                    recursionNum + 1, widthOfPixelSection / 2, heightOfPixelSection / 2);
        }
        //great
        Color bottomRight = middleColor;
        if (!middleColor.equals(rightBottomColor)) {
            bottomRight = recursivePixelColor(middlePoint, middleColor, rightBottomPoint, rightBottomColor, Vup, Vright, "left",
                    recursionNum + 1, widthOfPixelSection / 2, heightOfPixelSection / 2);
        }

        Color bottomLeft = middleColor;
        if (!middleColor.equals(leftBottomColor)) {
            bottomLeft = recursivePixelColor(middlePoint, middleColor, leftBottomPoint, leftBottomColor, Vup, Vright, "right",
                    recursionNum + 1, widthOfPixelSection / 2, heightOfPixelSection / 2);
        }

        Color totalColor = topRight.add(topLeft, bottomRight, bottomLeft);
        //instead of dividing every single one


        return  totalColor.scale(0.25);
    }
}