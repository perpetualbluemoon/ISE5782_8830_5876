package renderer;

import geometries.Intersectable.GeoPoint;
import lighting.LightSource;
import primitives.*;
import scene.Scene;

import java.util.LinkedList;
import java.util.List;

import static primitives.Util.alignZero;

/***
 * class RayTracerBasic used for finding the color for pixels
 */
public class RayTracerBasic extends RayTracerBase {

    /***
     * added in part7 in order to support shadows
     */
    private static final double DELTA = 0.1;
    /***
     * added in part7 in order to support shadows
     */
    private static final double EPSILON = 0.001;

    //max values to stop recursion of functions calling each other
    //values were chosen according to the given instructions
    private static final int MAX_CALC_COLOR_LEVEL = 3;//for less recursion and faster calculation time
    private static final double MIN_CALC_COLOR_K = 0.001;
    private static final double INITIAL_K = 1.0;

    //ON/OFF button default is off
    private boolean _softShadowsButton =true;
    //root of number of points around the lightsource
    private static int ROOT_OF_MOVED_LIGHT_POINTS = 1;

    /***
     * creates a scene using the parent class
     * @param scene the scene
     */
    public RayTracerBasic(Scene scene) {
        super(scene);
    }

    /***
     * @param ray ray from the camera through the viewplane
     * @return the color of the closest point among the list of intersections
     */
    @Override
    public Color traceRay(Ray ray) {
        //GeoPoint closestPoint = ray.findClosestGeoPoint(listPointsIntersections);
        GeoPoint closestPoint = findClosestGeoIntersection(ray);
        //out.print(closestPoint);
        if (closestPoint == null)
            return _scene.getBackground();
        return calcColor(closestPoint, ray);
    }

    /***
     * Method which calculates the effects of deffusive light
     * @return color of the light
     */
    private double calcDiffusive(double kD, double nl) {
        //according to the phong model
        return kD * Math.abs(nl);
    }

    /***
     * Method which calculates the effects of specular light
     * @return color of the light
     */
    private double calcSpecular(double kS, Vector n, Vector l, double nl, Vector v, double shininess) {
        //according to the phong model
        Vector r = createR(l, n);
        double max = Math.max(0, v.scale(-1).dotProduct(r));
        return kS * Math.pow(max, shininess);
    }

    private Vector createR(Vector l, Vector n) {
        Vector r = l.subtract(n.scale(2 * l.dotProduct(n)));
        return r.normalize();

    }

    /***
     * Mathematical calculations according to the slides from the fist semester
     * This function adds the specular and diffusive effects to the color of the object
     * @param intersection the point for which we want the color
     * @param ray ray from the camera
     * @return the color of the point
     */
    private Color calcLocalEffects(GeoPoint intersection, Ray ray, double k) {
        Color color = intersection._geoPointGeometry.getEmission();//.scale(1 - intersection._geoPointGeometry.getMaterial().kT.getD1());
        Vector v = ray.getDir();
        Vector n = intersection._geoPointGeometry.getNormal(intersection._geoPoint);
        double nv = alignZero(n.dotProduct(v));
        if (nv == 0) return color;
        Material material = intersection._geoPointGeometry.getMaterial();
        for (LightSource lightSource : _scene.getLights()) {
            Vector l = lightSource.getL(intersection._geoPoint);
            double nl = alignZero(n.dotProduct(l));
            if (nl * nv > 0) { // sign(nl) == sing(nv)
                double ktr = transparency(intersection, l, n, lightSource);
                if (ktr * k > MIN_CALC_COLOR_K) {
                    Color iL = lightSource.getIntensity(intersection._geoPoint).scale(ktr);
                    color = color.add(iL.scale(calcDiffusive(material.getkD().getD1(), nl)),
                            iL.scale(calcSpecular(material.getkS().getD1(), n, l, nl, v, material.nShininess)));
                }
            }
        }
        return color;

    }

    /***
     * inner function
     * helper function for calcGlobalEffects used for recursion
     * could have been written in reg calcGlobal but instead of writing it twice for reflect and refract
     * @param ray from the point to the camera
     * @param level number of recursions left
     * @param kx constant coefficient of lowering (could be kt or kr)
     * @param kkx current kx, if it is smaller than min it won't add much to total color so no need to calc it
     * @return color
     */
    public Color calcGlobalEffects(Ray ray, int level, Double3 kx, double kkx) {
        GeoPoint gp = findClosestGeoIntersection(ray);
        if (gp == null) {
            return _scene.getBackground();
        }

        //level-1 to ensure that the recursion stops
        //scales by kx to return only a percentage of the color according to the coefficient
        //out.print(c);
        return calcColor(gp, ray, level - 1, kkx).scale(kx);
    }

    /***
     * outer function
     * function calcGlobalEffects handles reflections and refractions of light on a specific point
     * @param geoPoint the point to calculate the color for
     * @param v the vector from the camera to the point
     * @param level how many recursion can still be performed
     * @param k constant coefficient of lowering
     * @return the color for that point
     */
    public Color calcGlobalEffects(GeoPoint geoPoint, Vector v, int level, double k) {
        //base color
        Color color = Color.BLACK;
        //normal
        Vector n = geoPoint._geoPointGeometry.getNormal(geoPoint._geoPoint);
        Material material = geoPoint._geoPointGeometry.getMaterial();

        //checking that we haven't had too many refractions
        //adding refracted color
        double kkt = k * material.kT.getD1();

        if (kkt > MIN_CALC_COLOR_K) {
            //kkt changes every iteration, kt stays the same

            color = color.add(calcGlobalEffects(constructRefractedRay(geoPoint._geoPoint, v, n), level, material.kT, kkt));
        }

        //checking that we haven't had too many reflections
        //adding reflected color
        double kkr = k * material.kR.getD1();
        if (kkr > MIN_CALC_COLOR_K) {
            color = color.add(calcGlobalEffects(constructReflectedRay(geoPoint._geoPoint, v, n), level, material.kR, kkr));
        }
        return color;
    }

    /***
     * inner function
     * @param closestPoint the point that we want to find the color of
     * @param ray the ray from the camera through a pixel
     * @param k influence of current recursion
     * @param level number of recursion
     * @return the color for that point
     * recursive function
     */
    public Color calcColor(GeoPoint closestPoint, Ray ray, int level, double k) {
        // calcLocalEffects includes the color of the object
        Color color = calcLocalEffects(closestPoint, ray, k);
        //adding the local effects according to the phong model, including emissions
        if (level == 1)
            return color;
        else
            return color.add(calcGlobalEffects(closestPoint, ray.getDir(), level, k));
    }

    /***
     * this function calls calcColor with properties to limit recursion
     * outer function
     * @param gp the point for which we want the color
     * @param ray the ray to the point from the camera to the point
     * @return the color for the point
     */
    private Color calcColor(GeoPoint gp, Ray ray) {
        Color x = calcColor(gp, ray, MAX_CALC_COLOR_LEVEL, INITIAL_K);
        return x.add(_scene.getAmbientLight().getIntensity());
    }

    /***
     * this function tells us whether a certain point is shaded or not
     * @param gp the point to check
     * @param l the vector from the light to the point
     * @param n normal to that point
     * @return true if the point is unshaded
     */
    private boolean unshaded(GeoPoint gp, Vector l, Vector n, LightSource lightSource) {
        Vector lightDirection = l.scale(-1); //from point to light source
        //took a point adding epsilon in the direction of the normal
        Ray lightRay = new Ray(gp._geoPoint, n, lightDirection);
        //checks for intersections between the point and the light
        List<GeoPoint> intersections = _scene.getGeometries().findGeoIntersections(lightRay);
        //if there is nothing between the point and the light then the point is unshaded
        if (intersections == null)
            return true;
        if (intersections.isEmpty())
            return true;

        //using distance to ensure that we don't put a shadow based on objects behind the light
        double distance = lightSource.getDistance(gp._geoPoint);
        for (GeoPoint intersection : intersections) {
            if (alignZero(intersection._geoPoint.distance(gp._geoPoint) - distance) <= 0 &&
                    intersection._geoPointGeometry.getMaterial().kT.getD1() == 0)
                return false;
        }
        return true;
    }


    /***
     *
     * @param p point at the head of the ray for the reflection
     * @param v the vector from the camera to the point
     * @param n the normal vector from the point
     * @return the reflected ray
     */
    public Ray constructReflectedRay(Point p, Vector v, Vector n) {
        return new Ray(p, n, createR(v, n));
    }

    /***
     *
     * @param p point at the head of the refracted ray
     * @param v the vector from the camera to the point
     * @param n the normal vector from the point
     * @return the refracted ray
     */
    public Ray constructRefractedRay(Point p, Vector v, Vector n) {
        //something transparent will show only the objects behind it.
        //we are moving the point using minus delta because we are moving inward in the opposite direction of the normal
        return new Ray(p, n, v);
    }

    /***
     * helper function for frequently used code (DRY)
     * @param ray the ray for which we want to find the closest point
     * @return GeoPoint
     */
    private GeoPoint findClosestGeoIntersection(Ray ray) {
        LinkedList<GeoPoint> l = (LinkedList<GeoPoint>) _scene.getGeometries().findGeoIntersections(ray);
        if (l == null)
            return null;
        if (l.isEmpty())
            return null;
        return ray.findClosestGeoPoint(l);
    }

    /***
     * this function tells us whether a certain point is shaded or not
     * @param geoPoint the point to check
     * @param l the vector from the light to the point
     * @param n normal to that point
     * @return double percentage of how shaded the point is
     */
    private double transparency(GeoPoint geoPoint, Vector l, Vector n, LightSource lightSource) {
        //This while is so that we can break out of the soft shadows clause if our light source is directional light
        //which returns null
        boolean x = true;
        while (x) {
            x = false;
            if (_softShadowsButton) {
                //in light: field:  size of plane to move in
                //function is called on the light source
                //implement soft shadows:

                Vector lightDirection = l.scale(-1); //from point to light source
                //getting a list of points around the light source from function defined in each light source
                //the size of the field of movement around the light source is defined per light source

                LinkedList<Point> pointsAroundLight = lightSource.findPointsAroundLight(lightDirection, ROOT_OF_MOVED_LIGHT_POINTS);


                double sumTransparency = 0;
                if ((pointsAroundLight == null) || (pointsAroundLight.isEmpty()))
                    break;

                else {
                    //for each point of the list find the percentage of shading and add to the total sum
                    //this is done by creating a ray for each point and finding how shaded it is
                    for (Point lightPointMoved : pointsAroundLight) {
                        //took a lightPointMoved adding epsilon in the direction of the normal
                        //dot product check is done inside the constructor
                        Ray lightRay = new Ray(geoPoint._geoPoint, n, lightPointMoved.subtract(geoPoint._geoPoint));

                        //checks for intersections between the lightPointMoved and the light
                        List<GeoPoint> intersections = _scene.getGeometries().findGeoIntersections(lightRay);

                        double currentTransparency = 1.0;
                        //if there is nothing between the lightPointMoved and the light then the lightPointMoved is unshaded
                        if ((intersections != null) && (!intersections.isEmpty())) {
                            //using distance to ensure that we don't put a shadow based on objects behind the light
                            //distance from geopoint to the light
                            double distanceLightToGeoPoint = lightPointMoved.distance(geoPoint._geoPoint);
                            //out.print(distanceLightToGeoPoint+"\n");
                            for (GeoPoint intersection : intersections) {
                                //out.print(currentTransparency+"\n");

                                //if the distance between the geoPoint and the movedLight is greater than the distance between
                                // the geoPoint and the intersection then the intersected point is in the way
                                double distanceIntersectionToGeoPoint = intersection._geoPoint.distance(geoPoint._geoPoint);
                                //out.print(distanceIntersectionToGeoPoint+"\n");
                                if (alignZero(distanceIntersectionToGeoPoint - distanceLightToGeoPoint) <= 0) {

                                    currentTransparency = alignZero(currentTransparency *(1-intersection._geoPointGeometry.getMaterial().kT.getD1()));
                                   // out.print(intersection._geoPointGeometry.getMaterial().kT.getD1());

                                }

                            }
                            sumTransparency += currentTransparency;
                        }

                    }
                    //return the transparency averaged from all the points

                    return 1-sumTransparency / pointsAroundLight.size();

                }
            }
        }
        //default code
        Vector lightDirection = l.scale(-1); //from point to light source
        //took a point adding epsilon in the direction of the normal
        //dot product check is done inside the constructor
        Ray lightRay = new Ray(geoPoint._geoPoint, n, lightDirection);

        //checks for intersections between the point and the light
        List<GeoPoint> intersections = _scene.getGeometries().findGeoIntersections(lightRay);
        //if there is nothing between the point and the light then the point is unshaded
        if (intersections == null)
            return 1.0;
        if (intersections.isEmpty())
            return 1.0;

        double ktr = 1.0;

        //using distance to ensure that we don't put a shadow based on objects behind the light
        double distance = lightSource.getDistance(geoPoint._geoPoint);
        for (GeoPoint intersection : intersections) {
            if (alignZero(intersection._geoPoint.distance(geoPoint._geoPoint) - distance) <= 0) {
                ktr = alignZero(ktr * intersection._geoPointGeometry.getMaterial().kT.getD1());
                if (ktr == 0)
                    return ktr;
            }
        }
        return ktr;
    }

    /***
     * ON/OFF button for soft shadows
     * @param softShadowsButton boolean enable soft shadows
     * @return RayTracerBasic for builder-like use
     */
    public RayTracerBasic setSoftShadowsButton(boolean softShadowsButton) {
        _softShadowsButton = softShadowsButton;
        return this;
    }

    /***
     *
     * @param softShadowsButton boolean enable soft shadows
     * @param number_of_points number of points for caluculating shadows
     * @return RayTracerBasic for builder-like implementation
     */
    public RayTracerBasic setSoftShadowsButton(boolean softShadowsButton, int number_of_points) {
        _softShadowsButton = softShadowsButton;
        ROOT_OF_MOVED_LIGHT_POINTS = number_of_points;
        return this;
    }
}
