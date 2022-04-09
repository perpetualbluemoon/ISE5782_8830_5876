package scene;

import lighting.AmbientLight;
import geometries.Geometries;
import lighting.LightSource;
import primitives.Color;

import java.util.LinkedList;

/***
 * Scene class: PDS holder for all the data about the 3D scene: objects, ambient light, colors, etc
 * this will be used by the renderer
 */
public class Scene {

    public String _name; //scene name
    public Color _background = Color.BLACK; //background color
    public AmbientLight _ambientLight = new AmbientLight(); //ambient light
    public Geometries _geometries; // all the shapes in the scene
    public LinkedList<LightSource> _lights = new LinkedList<>(); //all the light sources in the scene except for ambient light

    /***
     * constructor with mandatory field name
     * @param name name of the scene
     */
    public Scene(String name) {
        _name = name;
        _geometries = new Geometries();
    }

    /***
     * Setter using the pattern that is similar to builder which returns the current objects
     * @param background the background color of the scene
     * @return the scene
     */
    public Scene setBackground(Color background) {
        _background = background;
        return this;
    }

    /***
     * Setter using the pattern that is similar to builder which returns the current objects
     * @param ambientLight the ambient light sources in the scene
     * @return the scene
     */
    public Scene setAmbientLight(AmbientLight ambientLight) {
        _ambientLight = ambientLight;
        return this;
    }

    /***
     * Setter using the pattern that is similar to builder which returns the current objects
     * @param geometries the list objectss in the scene
     * @return the scene
     */
    public Scene setGeometries(Geometries geometries) {
        _geometries = geometries;
        return this;
    }

    /***
     * Setter using the pattern that is similar to builder which returns the current objects
     * @param lights the list of light sources in the scene
     * @return the scene
     */
    public Scene setLights(LinkedList<LightSource> lights) {
        _lights = lights;
        return this;
    }
}

