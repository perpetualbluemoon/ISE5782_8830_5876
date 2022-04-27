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


    public String _name; //scene name
    public Color _background;  //background color
    public AmbientLight _ambientLight;//ambient light
    public Geometries _geometries; // all the shapes in the scene
    public LinkedList<LightSource> _lights; //all the light sources in the scene except for ambient light

    private Scene(SceneBuilder built){
        _name=built._name; //scene name
        _background=built._background;  //background color
        _ambientLight=built._ambientLight;//ambient light
        _geometries=built._geometries; // all the shapes in the scene
        _lights=built._lights;

    }

    //why is it static?????????????
    public static class SceneBuilder{
        public String _name;
        public Color _background=Color.BLACK;  //background color
        public AmbientLight _ambientLight=new AmbientLight(_background,new Double3(0));//ambient light
        public Geometries _geometries=new Geometries(); // all the shapes in the scene
        public LinkedList<LightSource> _lights=new LinkedList<>();

        public SceneBuilder(String name) {
            _name = name;
        }

        public SceneBuilder setName(String name) {
            _name = name;
            return this;
        }

        public SceneBuilder setBackground(Color background) {
            _background = background;
            return this;
        }

        public SceneBuilder setAmbientLight(AmbientLight ambientLight) {
            _ambientLight = ambientLight;
            return this;
        }

        public SceneBuilder setGeometries(Geometries geometries) {
            _geometries = geometries;
            return this;
        }

        public SceneBuilder setLights(LinkedList<LightSource> lights) {
            _lights = lights;
            return this;
        }
        public Scene build(){
            return new Scene(this);
        }

    }

}

