package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.LinkedList;
import java.util.MissingResourceException;

import static primitives.Util.isZero;

/***
 * class of camera helps construct rays through view plane pixels and find intersections
 * it also implements depth of field and jagged edges improvements from part 8
 * as well as multi-threading and adaptive super-sampling from part 9
 */
public class Camera {

    //coordinates of the camera
    private Vector _Vright;//y
    private Vector _Vup;//x
    private Vector _Vto;//z
    private Point _centerCam;

    //information about the view plane
    private double _heightVP;
    private double _widthVP;
    private double _distanceVPToCam;

    //information about creation of the final image
    private ImageWriter _imageWriter; //creates the photo
    private RayTracerBase _rayTracerBase;


    //feature of part 8
    //ON/OFF button default is off
    private boolean _depthButton = false;
    //focal length
    private double _focalLength = 2;
    private double _apertureSize = 0.01;
    private static final int NUMBER_OF_APERTURE_POINTS = 3;

    //feature of part 8
    //ON/OFF button default is off
    private boolean _JaggedEdgesButton = false;
    //number of minipixels is used for both height and width of the minipixel for simplicity
    private static int NUMBER_OF_MINIPIXELS = 3;


    //feature of part 9
    //ON/OFF button default is off
    private static boolean _MultiThreadingButton = false;
    private static int NUM_OF_THREADS = 1;
    private double printInterval = 0;

    /**
     *  feature of part 9
     *  /ON/OFF button default is off
     */
    private boolean _adaptiveSuperSampling = false;
    /***
     * maximum recursion, for button
     */
    public static int MAX_RECURSION = 3;


    /***
    *constructor receives @param  Point centerCam,Vector vto, Vector vup and creates camera
     */

    /***
     * this constructor receives vup and vto and creates vright, creates an object of class camera
     * @param centerCam the center point of the camera
     * @param vto direction the camera is facing
     * @param vup orientation of the camera
     */
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

    //setters similar to builder pattern return the object in order to string together these operations

    /***function setter
     *@param distanceVPToCam for distance of view plane from camera
     *@return Camera current object - camera
     */
    public Camera setVPDistance(double distanceVPToCam) {
        _distanceVPToCam = distanceVPToCam;
        return this;
    }

    /***
     *function setter
     *@param width-width for size of view plane
     *@param height -height for size of view plane
     *@return current object - camera
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
     * set ray tracer base
     * @param rayTracerBase finds color
     * @return the camera
     */
    public Camera setRayTracerBase(RayTracerBase rayTracerBase) {
        _rayTracerBase = rayTracerBase;
        return this;
    }

    /***
     * set ray tracer
     * @param rayTracerBasic used to trace rays and find color
     * @return the camera
     */
    public Camera setRayTracer(RayTracerBasic rayTracerBasic) {
        _rayTracerBase = rayTracerBasic;
        return this;
    }

    /***
     * set focal length is used as part of the depth of field improvement (part 8)
     * this determines which items in the scene will be in focus
     * @param focalLength the distance of the focal plane from the camera
     * @return the camera
     */
    public Camera setFocalLength(double focalLength) {
        _focalLength = focalLength;
        return this;
    }

    /***
     * aperture size is used as part of the depth of field improvement (part 8)
     * this determines how blurry the items which are not in focus will be
     * @param apertureSize a small double indicating how blurry items should be when not in focus
     * @return the camera
     */
    public Camera setApertureSize(double apertureSize) {
        _apertureSize = apertureSize;
        return this;
    }

    /***
     * getter
     * @return vector
     */
    public Vector getVright() {
        return _Vright;
    }
    /***
     * getter
     * @return vector
     */
    public Vector getVup() {
        return _Vup;
    }
    /***
     * getter
     * @return vector
     */
    public Vector getVto() {
        return _Vto;
    }
    /***
     * getter
     * @return point center of cam
     */
    public Point getCenterCam() {
        return _centerCam;
    }
    /***
     * getter
     * @return double height
     */
    public double getHeightVP() {
        return _heightVP;
    }
    /***
     * getter
     * @return double width
     */
    public double getWidthVP() {
        return _widthVP;
    }
    /***
     * getter
     * @return double distance
     */
    public double getDistanceVPToCam() {
        return _distanceVPToCam;
    }

    /**
     * basic camera functions
     */

    /***
     * function render Image checks that all the fields are initialized and then renders the image
     * this function uses some of the on/off buttons to determine which functions to call and in what manner
     * @return camera in build like manner
     */
    public Camera renderImage() {
        //coordinates of the camera are not null
        if ((_Vright == null) || (_Vup == null) || (_Vto == null) || (_centerCam == null))
            throw new MissingResourceException("Camera coordinates are not initialized", "Camera", "coordinates");

        //information about the view plane does not need to be checked because double cannot be null

        //information about creation of the final image
        if ((_imageWriter == null) || (_rayTracerBase == null))
            throw new MissingResourceException("Image creation details are not initialized", "Camera", "Writer details");
        //implementing the multi-threading time improvement of part 9
        if (_MultiThreadingButton) {

            Pixel.initialize(_imageWriter.getNy(), _imageWriter.getNx(), printInterval); //debug print is print interval
           int threadsCount = 3;
            while (threadsCount-- > 0) {
                new Thread(() -> {
                    for (Pixel pixel = new Pixel(); pixel.nextPixel(); Pixel.printPixel(),Pixel.pixelDone()) {
                        if(_adaptiveSuperSampling) {
                            //implementing the adaptive super sampling time improvement of part 9
                            Color thisPixelColor = castRay(pixel.col, pixel.row);
                           _imageWriter.writePixel(pixel.col, pixel.row, thisPixelColor);
                        }
                        else{
                            Color thisPixelColor = castRayOld(pixel.col, pixel.row);
                            _imageWriter.writePixel(pixel.col, pixel.row, thisPixelColor);
                        }

                    }
                }).start();

            } Pixel.waitToFinish();


        } else { //when not using multithreading
            //for every row
            for (int i = 0; i < _imageWriter.getNy(); i++) {
                //for every column
                for (int j = 0; j < _imageWriter.getNx(); j++) {
                    if(_adaptiveSuperSampling) { //implementing adaptive super sampling (part 9)
                        Color thisPixelColor = castRay(j, i);
                        _imageWriter.writePixel(j, i, thisPixelColor);
                        //out.print("calling cast ray new  ");
                    }
                    else{
                        Color thisPixelColor = castRayOld(j, i);
                        //out.print("calling cast ray old ");
                        _imageWriter.writePixel(j, i, thisPixelColor);
                    }
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
        //vi,j = pi,j − p0
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
        //pc =p0+ d*v
        //finding middle of view plane
        Point pc = _centerCam.add(_Vto.scale(_distanceVPToCam));
        //RY= H/NY{
        //        //
        //RX = W/NX
        //calculating size of pixels for width and height
        double ry = _heightVP / nY;
        double rx = _widthVP / nX;
        //finding how much to move up and right to find wanted pixel
        double xj = (j - ((nX - 1) / 2.0)) * rx;
        double yi = -(i - ((nY - 1) / 2.0)) * ry;

        //Pi,j = pc + (xj*Vright + yi*Vup)
        //yi = -(i - (NY − 1)/2) * RY
        // XJ = (J - (NX -1)/2) * RX
        // finding point pij wanted pixel according to the formula
        //doing it in parts to prevent addition of zero
        Point Pij = pc;
        if (!isZero(xj)) {
            //XJ*Vright
            Pij = Pij.add(_Vright.scale(xj));
        }
        if (!isZero(yi)) {
            //YI*VUP
            Pij = Pij.add(_Vup.scale(yi));
        }

        return Pij;
    }


    /***
     * cast ray is the main function in which the modifications of part 8 are made
     * for this reason, there are multiple clauses checking which fix we are currently in
     * this function casts a ray from the camera through the current pixel and returns the color of that pixel
     * this is called "old" because it is called when the adaptive super sampling is not used
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


    /***
     * this function casts a ray from the camera through the current pixel and returns the color of that pixel
     * this function is designed to save time when implementing the fix for jagged edges
     * the time improvement here is from 38 seconds to 16 seconds on my computer
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
            //initial color for average calculation
            double colorX = 0;
            double colorY = 0;
            double colorZ = 0;

            //creating the initial parameters for the recursive calculation

            Point thisPixelPoint = createMiddlePixel(_imageWriter.getNx(), _imageWriter.getNy(), j, i);
            // out.print(thisPixelPoint);

            //creating a point in the top left corner
            Point topLeftCorner = thisPixelPoint;
            Color topLeftColor = _rayTracerBase.traceRay(new Ray(_centerCam, topLeftCorner.subtract(_centerCam).normalize()));


            Point bottomRightCorner = thisPixelPoint.add(_Vup.scale((_heightVP / _imageWriter.getNy()) * -1));
            bottomRightCorner = bottomRightCorner.add(_Vright.scale((_widthVP / _imageWriter.getNx())));
            Color bottomRightColor = _rayTracerBase.traceRay(new Ray(_centerCam, bottomRightCorner.subtract(_centerCam).normalize()));

            //calling recursive function to calculate the color for this pixel
            Color fullPixelColor = recursivePixelColor(topLeftCorner, topLeftColor, bottomRightCorner, bottomRightColor,
                    _Vup, _Vright, "left", 0, (_heightVP / _imageWriter.getNy()), (_widthVP / _imageWriter.getNx()));

            return fullPixelColor;


        }
        return Color.BLACK;
    }


    /***
     * This function is the recursive calculation of a color for a pixel or a section of a pixel.
     * @param upperCorner upper left or right corner of pixel section
     * @param upperColor color of upper corner
     * @param bottomCorner bottom left or right corner of pixel section
     * @param bottomColor color of bottom corner
     * @param Vup vector going up (camera coordinates)
     * @param Vright vector going right (camera coordinates)
     * @param leftRight whether upper corner is to the left or the right of the bottom corner
     * @param recursionNum number of the current recursion
     * @param widthOfPixelSection the width of the current pixel section
     * @param heightOfPixelSection the height of the current pixel section
     * @return color of pixel segment( it's the recursive function)
     */
    public Color recursivePixelColor(Point upperCorner, Color upperColor, Point bottomCorner, Color bottomColor,
                                     Vector Vup, Vector Vright, String leftRight, int recursionNum, double widthOfPixelSection,
                                     double heightOfPixelSection) {

        //stop condition
        //MAX_RECURSION = 3 because we started from 0
        //this means we've divided the pixel into 64 sections
        if (recursionNum >= MAX_RECURSION)
            return upperColor;

        // out.print(recursionNum);

        //initializing the points and colors so that they can be calculated separately for left and right calls
        Point leftTopPoint;
        Point rightTopPoint;
        Point leftBottomPoint;
        Point rightBottomPoint;
        Point middlePoint;

        Color leftTopColor;
        Color rightTopColor;
        Color leftBottomColor;
        Color rightBottomColor;

        //Calculations here depend on where upper corner is relative to bottom corner. If the upper corner is to the
        //left of the bottom corner, then calculations are slightly different. Therefor we calculated all four corners
        //as well as their colors and then calculated each of the four sub-pixels afterwards using these points
        if (leftRight == "left") {
            middlePoint = upperCorner.add(_Vup.scale(heightOfPixelSection * -0.5));
            middlePoint = middlePoint.add(_Vright.scale(widthOfPixelSection * 0.5)).movePointRandom(_Vup, _Vright, widthOfPixelSection / 4);

            rightBottomPoint = bottomCorner.movePointRandom(_Vup, _Vright, widthOfPixelSection / 4);
            leftTopPoint = upperCorner.movePointRandom(_Vup, _Vright, widthOfPixelSection / 4);
            rightTopPoint = upperCorner.add(_Vright.scale(widthOfPixelSection)).movePointRandom(_Vup, _Vright, widthOfPixelSection / 4);
            leftBottomPoint = upperCorner.add(_Vup.scale(-1 * heightOfPixelSection)).movePointRandom(_Vup, _Vright, widthOfPixelSection / 4);

            leftTopColor = upperColor;
            rightBottomColor = bottomColor;
            rightTopColor = _rayTracerBase.traceRay(new Ray(_centerCam, rightTopPoint.subtract(_centerCam).normalize()));
            //if(!rightTopColor.equals(Color.MAGENTA)){
            //  out.print(rightTopColor);
            //}
            leftBottomColor = _rayTracerBase.traceRay(new Ray(_centerCam, leftBottomPoint.subtract(_centerCam).normalize()));

        } else {
            middlePoint = upperCorner.add(_Vup.scale(heightOfPixelSection * -0.5));
            middlePoint = middlePoint.add(_Vright.scale(widthOfPixelSection * -0.5)).movePointRandom(_Vup, _Vright, widthOfPixelSection / 4);

            leftBottomPoint = bottomCorner.movePointRandom(_Vup, _Vright, widthOfPixelSection / 4);
            rightTopPoint = upperCorner.movePointRandom(_Vup, _Vright, widthOfPixelSection / 4);
            leftTopPoint = upperCorner.add(_Vright.scale(-1 * widthOfPixelSection)).movePointRandom(_Vup, _Vright, widthOfPixelSection / 4);
            rightBottomPoint = upperCorner.add(_Vup.scale(-1 * heightOfPixelSection)).movePointRandom(_Vup, _Vright, widthOfPixelSection / 4);


            rightTopColor = upperColor;
            leftBottomColor = bottomColor;
            leftTopColor = _rayTracerBase.traceRay(new Ray(_centerCam, leftTopPoint.subtract(_centerCam).normalize()));
            rightBottomColor = _rayTracerBase.traceRay(new Ray(_centerCam, rightBottomPoint.subtract(_centerCam).normalize()));
        }

        //now that we have all five points we can find the middle color (this is done outside because it is the same for
        //both types of calls


        Color middleColor = _rayTracerBase.traceRay(new Ray(_centerCam, middlePoint.subtract(_centerCam).normalize()));

        //Each sub-pixel's color is calculated. The color is initially the same as the middle and is only updated if
        //there is a difference between it and the matching corner.
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

        //once the color for all four sub-pixels has been calculated, they are all added up into one color and then
        //divided by four in order to have an average.
        //the average color is then returned
        Color totalColor = topRight.add(topLeft, bottomRight, bottomLeft);
        Color averageColor = totalColor.scale(0.25);
        return averageColor;
    }


    //ON/OFF Button implementation:

    /***
     * on/off button for depth of field (part 8)
     * @param button on/off
     * @param apertureSize a small number indicating how blurry objects which are not in focus should be
     * @param focalLength how far away the objects which are in focus are
     */
    public void setDepthButton(boolean button, double apertureSize, double focalLength) {
        _depthButton = button;
        _apertureSize = apertureSize;
        _focalLength = focalLength;
    }

    /***
     * on/off button for depth of field
     * @param depthButton on/off
     */
    public void setDepthButton(boolean depthButton) {
        _depthButton = depthButton;
    }

    /***
     * on/off button for jagged edges (part 8)
     * @param jaggedEdgesButton on/off
     * @param numberOfMiniPixels root of how many rays should be created per minipixel
     */
    public void setJaggedEdgesButton(boolean jaggedEdgesButton, int numberOfMiniPixels) {
        _JaggedEdgesButton = jaggedEdgesButton;
        NUMBER_OF_MINIPIXELS = numberOfMiniPixels;
    }

    /***
     * on/off button for jagged edges
     * @param jaggedEdgesButton on/off
     */
    public void setJaggedEdgesButton(boolean jaggedEdgesButton) {
        _JaggedEdgesButton = jaggedEdgesButton;
    }


    /***
     * on/off button for multi-threading time improvement (part 9)
     * @param i how many threads to have
     * @return the camera
     */
    public Camera setMultithreading(int i) {
        if (i > 1)
            _MultiThreadingButton = true;
        NUM_OF_THREADS = i;
        return this;
    }

    /***
     * set button for multi-threading (part 9) this allows for easier debugging
     * @param v interval of how often to print
     * @return the camera
     */
    public Camera setDebugPrint(double v) {
        printInterval = v;
        return this;
    }

    /***
     * on/off button for adaptive super sampling time improvement(part 9)
     * @param adaptiveSuperSampling on/off
     * @return the camera
     */
    public Camera setAdaptiveSuperSampling(boolean adaptiveSuperSampling) {
        _adaptiveSuperSampling = adaptiveSuperSampling;
        return this;
    }

    /***
     * on/off button for adaptive super sampling (part 9)
     * @param adaptiveSuperSampling on/off
     * @param recursion max amount of recursions
     * @return the camera
     */
    public Camera setAdaptiveSuperSampling(boolean adaptiveSuperSampling, int recursion) {
        _adaptiveSuperSampling = adaptiveSuperSampling;
        MAX_RECURSION = recursion;
        return this;
    }

}