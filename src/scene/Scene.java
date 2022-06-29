package scene;

import lighting.AmbientLight;
import geometries.Geometries;
import lighting.LightSource;
import primitives.Color;
import primitives.Double3;

import java.util.LinkedList;

/***
 * Scene class: PDS holder for all the data about the 3D scene: objects, ambient light, colors, etc
 * this will be used by the renderer
 */
public class Scene {


    /***
     * name of the scene
     */
    private String _name;
    /***
     * background color
     */
    private Color _background;
    /***
     * ambient light in the scene
     */
    private AmbientLight _ambientLight;
    /***
     * list of shapes in the scene
     */
    private Geometries _geometries;
    /***
     * list of light sources in the scene
     */
    private LinkedList<LightSource> _lights;

    /***
     * Using builder pattern
     * @param built the scenebuilder with all of the information
     */
    private Scene(SceneBuilder built){
        _name=built._name; //scene name
        _background=built._background;  //background color
        _ambientLight=built._ambientLight;//ambient light
        _geometries=built._geometries; // all the shapes in the scene
        _lights=built._lights;

    }

    /***
     * scene builder is the helper class in order to implement the builder pattern
     */
    public static class SceneBuilder{
        /***
         * name of the scene
         */
        public String _name;
        /***
         * background color
         */
        public Color _background=Color.BLACK;
        /***
         * the ambient light in the scene
         */
        public AmbientLight _ambientLight=new AmbientLight(_background,new Double3(0));
        /***
         * list of geometries in the scene
         */
        public Geometries _geometries=new Geometries();
        /***
         * list of light sources in the scene
         */
        public LinkedList<LightSource> _lights=new LinkedList<>();

        /***
         * constructor
         * @param name of the scene
         */
        public SceneBuilder(String name) {
            _name = name;
        }

        /***
         * setter
         * @param name of the scene
         * @return scenebuilder for builder pattern
         */
        public SceneBuilder setName(String name) {
            _name = name;
            return this;
        }

        /***
         * setter
         * @param background the background color of the scene
         * @return scenebuilder for builder pattern
         */
        public SceneBuilder setBackground(Color background) {
            _background = background;
            return this;
        }

        /***
         * setter
         * @param ambientLight the ambient light for the scene
         * @return scenebuilder for builder pattern
         */
        public SceneBuilder setAmbientLight(AmbientLight ambientLight) {
            _ambientLight = ambientLight;
            return this;
        }

        /***
         * setter
         * @param geometries list of geometries
         * @return scenebuilder for builder pattern
         */
        public SceneBuilder setGeometries(Geometries geometries) {
            _geometries = geometries;
            return this;
        }

        /***
         * setter
         * @param lights lighting for the scene
         * @return scenebuilder for builder pattern
         */
        public SceneBuilder setLights(LinkedList<LightSource> lights) {
            _lights = lights;
            return this;
        }

        /***
         * build function creates the scene using all the information from the setters
         * @return the created scene
         */
        public Scene build(){
            return new Scene(this);
        }

    }

    /***
     * getter
     * @return the name of the scene
     */
    public String getName() {
        return _name;
    }

    /***
     * getter
     * @return the background color
     */
    public Color getBackground() {
        return _background;
    }

    /***
     * getter
     * @return ambient lighting in the scene
     */
    public AmbientLight getAmbientLight() {
        return _ambientLight;
    }

    /***
     * getter
     * @return the list of geometries in the scene
     */
    public Geometries getGeometries() {
        return _geometries;
    }

    /***
     * getter
     * @return list of lights in the scene
     */
    public LinkedList<LightSource> getLights() {
        return _lights;
    }
}

