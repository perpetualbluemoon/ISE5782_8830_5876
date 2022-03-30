package scene;

import elements.AmbientLight;
import geometries.Geometries;
import geometries.Intersectable;
import primitives.Color;

import java.util.LinkedList;

/***
 * Scene class: PDS holder for all the data about the 3D scene: objects, ambient light, colors, etc
 * this will be used by the renderer
 */
public class Scene {

    public String _name; //scene name
    public Color _background; //background color
    public AmbientLight _ambientLight; //ambient light
    public Geometries _geometries; // all the shapes in the scene

    public Scene(String name){
        _name = name;
        _geometries = new Geometries();
    }

    public Scene setBackground(Color background) {
        _background = background;
        return this;
    }

    public Scene setAmbientLight(AmbientLight ambientLight) {
        _ambientLight = ambientLight;
        return this;
    }

    public Scene setGeometries(Geometries geometries) {
        _geometries = geometries;
        return this;
    }
}

