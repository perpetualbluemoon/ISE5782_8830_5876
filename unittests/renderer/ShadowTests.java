package renderer;

import geometries.*;
import lighting.*;
import org.junit.jupiter.api.Test;
import primitives.*;
import scene.Scene;

import java.util.LinkedList;

import static java.awt.Color.BLUE;

/**
 * Testing basic shadows
 *
 * @author Dan
 */
public class ShadowTests {
    private Intersectable sphere = new Sphere(new Point(0, 0, -200), 60d) //
            .setEmission(new Color(BLUE)) //
            .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30));
    private Material trMaterial = new Material().setKd(0.5).setKs(0.5).setShininess(30);

    /**
     * Helper function for the tests in this module
     */
    void sphereTriangleHelper(String pictName, Triangle triangle, Point spotLocation) {

        Geometries geometries = new Geometries(sphere, triangle.setEmission(new Color(BLUE)).setMaterial(trMaterial));
        LinkedList<LightSource> lights = new LinkedList<>();
        lights.add( //
                new Spotlight(new Color(400, 240, 0), spotLocation, new Vector(1, 1, -3)) //
                        .setKl(1E-5).setKq(1.5E-7));

        Scene scene1 = new Scene.SceneBuilder("Test scene").setGeometries(geometries)
                .setLights(lights).build();

        Camera camera = new Camera(new Point(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
                .setVPSize(200, 200).setVPDistance(1000) //
                .setRayTracer(new RayTracerBasic(scene1));

        camera.setImageWriter(new ImageWriter(pictName, 400, 400)) //
                .renderImage() //
                .writeToImage();
    }

    /**
     * Produce a picture of a sphere and triangle with point light and shade
     */
    @Test
    public void sphereTriangleInitial() {
        sphereTriangleHelper("shadowSphereTriangleInitial", //
                new Triangle(new Point(-70, -40, 0), new Point(-40, -70, 0), new Point(-68, -68, -4)), //
                new Point(-100, -100, 200));
    }

    /**
     * Sphere-Triangle shading - move triangle up-right
     */
    @Test
    public void sphereTriangleMove1() {
        sphereTriangleHelper("shadowSphereTriangleMove2", //
                new Triangle(new Point(-62, -32, 0), new Point(-32, -62, 0), new Point(-60, -60, -4)), //
                new Point(-100, -100, 200));
    }

    /**
     * Sphere-Triangle shading - move triangle upper-righter
     */
    @Test
    public void sphereTriangleMove2() {
        sphereTriangleHelper("shadowSphereTriangleMove1", //
                new Triangle(new Point(-49, -19, 0), new Point(-19, -49, 0), new Point(-47, -47, -4)), //
                new Point(-100, -100, 200));
    }

    /**
     * Sphere-Triangle shading - move spot closer
     */
    @Test
    public void sphereTriangleSpot1() {
        sphereTriangleHelper("shadowSphereTriangleSpot1", //
                new Triangle(new Point(-70, -40, 0), new Point(-40, -70, 0), new Point(-68, -68, -4)), //
                new Point(-88, -88, 120));
    }

    /**
     * Sphere-Triangle shading - move spot even more close
     */
    @Test
    public void sphereTriangleSpot2() {
        sphereTriangleHelper("shadowSphereTriangleSpot2", //
                new Triangle(new Point(-70, -40, 0), new Point(-40, -70, 0), new Point(-68, -68, -4)), //
                new Point(-76, -76, 70));
    }

    /**
     * Produce a picture of a two triangles lighted by a spot light with a Sphere
     * producing a shading
     */
    @Test
    public void trianglesSphere() {
        Geometries geometries = new Geometries(
                new Triangle(new Point(-150, -150, -115), new Point(150, -150, -135), new Point(75, 75, -150)) //
                        .setMaterial(new Material().setKs(0.8).setShininess(60)), //
                new Triangle(new Point(-150, -150, -115), new Point(-70, 70, -140), new Point(75, 75, -150)) //
                        .setMaterial(new Material().setKs(0.8).setShininess(60)), //
                new Sphere(new Point(0, 0, -11), 30d) //
                        .setEmission(new Color(java.awt.Color.BLUE)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30))
        );
        LinkedList<LightSource> lights = new LinkedList<>();


        lights.add( //
                new Spotlight(new Color(700, 400, 400), new Point(40, 40, 115), new Vector(-1, -1, -4)) //
                        .setKl(4E-4).setKq(2E-5));

        Scene scene2 = new Scene.SceneBuilder("Test scene").setGeometries(geometries)
                .setAmbientLight(new AmbientLight(new Color(java.awt.Color.WHITE), new Double3(0.15)))
                .setLights(lights).build();

        Camera camera = new Camera(new Point(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
                .setVPSize(200, 200).setVPDistance(1000) //
                .setRayTracer(new RayTracerBasic(scene2).setSoftShadowsButton(true));

        camera.setImageWriter(new ImageWriter("shadowTrianglesSphere", 600, 600)) //
                .renderImage() //
                .writeToImage();
    }

    /**
     * Produce a picture of a two triangles lighted by a spot light with a Sphere
     * producing a shading
     */
    @Test
    public void ourTestSoftShadows1() {
        Geometries geometries = new Geometries(
                new Triangle(new Point(-150, -150, -115), new Point(150, -150, -135), new Point(75, 75, -150)) //
                        .setMaterial(new Material().setKs(0.8).setShininess(60)), //
                new Triangle(new Point(-150, -150, -115), new Point(-70, 70, -140), new Point(75, 75, -150)) //
                        .setMaterial(new Material().setKs(0.8).setShininess(60)), //
                new Sphere(new Point(0, 0, -11), 30d) //
                        .setEmission(new Color(java.awt.Color.BLUE)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30))
        );
        LinkedList<LightSource> lights = new LinkedList<>();


        lights.add( //
                new PointLight(new Color(700, 400, 400), new Point(40, 40, 115)) //
                        .setKl(4E-4).setKq(2E-5));

        Scene scene2 = new Scene.SceneBuilder("Test scene").setGeometries(geometries)
                .setAmbientLight(new AmbientLight(new Color(java.awt.Color.WHITE), new Double3(0.15)))
                .setLights(lights).build();

        Camera camera = new Camera(new Point(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
                .setVPSize(200, 200).setVPDistance(1000) //
                .setRayTracer(new RayTracerBasic(scene2).setSoftShadowsButton(true, 10));

        camera.setImageWriter(new ImageWriter("shadowTrianglesSphere soft shadows", 600, 600)) //
                .renderImage() //
                .writeToImage();
    }

    /**
     * Produce a picture of a two triangles lighted by a spot light with a Sphere
     * producing a shading
     */
    @Test
    public void ourTestShadowAll() {
        Geometries geometries = new Geometries(
                new Triangle(new Point(-150, -150, -115), new Point(150, -150, -135), new Point(75, 75, -150)) //
                        .setMaterial(new Material().setKs(0.8).setShininess(60)), //
                new Triangle(new Point(-150, -150, -115), new Point(-70, 70, -140), new Point(75, 75, -150)) //
                        .setMaterial(new Material().setKs(0.8).setShininess(60)), //
                new Sphere(new Point(0, 0, -11), 30d) //
                        .setEmission(new Color(java.awt.Color.BLUE)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30))
        );
        LinkedList<LightSource> lights = new LinkedList<>();


        lights.add( //
                new PointLight(new Color(700, 400, 400), new Point(40, 40, 115)) //
                        .setKl(4E-4).setKq(2E-5));

        Scene scene2 = new Scene.SceneBuilder("Test scene").setGeometries(geometries)
                .setAmbientLight(new AmbientLight(new Color(java.awt.Color.WHITE), new Double3(0.15)))
                .setLights(lights).build();

        Camera camera = new Camera(new Point(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
                .setVPSize(200, 200).setVPDistance(1000) //
                .setRayTracer(new RayTracerBasic(scene2).setSoftShadowsButton(true)).setAdaptiveSuperSampling(false);

        //camera.setJaggedEdgesButton(true, 9);

        camera.setImageWriter(new ImageWriter("shadowTrianglesSphere soft shadows off", 600, 600)) //
                .renderImage() //
                .writeToImage();
    }

    @Test
    public void ourImageWithShadowss() {
        Camera camera = new Camera(new Point(65, 5, 20), new Vector(-7, -0.5, -2), new Vector(-1, 0, 1))
                .setVPSize(300, 300).setVPDistance(800);


        //camera.setDepthButton(true, 2, 50);

        Point p0 = new Point(0, -3, 5);
        Point p1 = new Point(0, 3, 5);
        Point p2 = new Point(0, 3, 0);
        Point p3 = new Point(0, -3, 0);
        Point p4 = new Point(9, 6, 5);
        Point p5 = new Point(9, 6, 0);
        Point p6 = new Point(9, -6, 0);
        Point p7 = new Point(9, -6, 5);

        Point p8 = new Point(1, 0, 0);
        Point p9 = new Point(2, 1, 0);
        Point p10 = new Point(3, 0, 0);
        Point p11 = new Point(2, -1, 0);
        Point p12 = new Point(1, 0, 2);
        Point p13 = new Point(2, 1, 2);
        Point p14 = new Point(3, 0, 2);
        Point p15 = new Point(2, -1, 2);

        Point p16 = new Point(0, -10, 0.5);
        Point p17 = new Point(0, 10, 0.5);
        Point p18 = new Point(100, -10, 0.5);
        Point p19 = new Point(100, 10, 0.5);

        Geometries geometries = new Geometries(
                //window
                new Quadrangle(p0, p1, p2, p3).setTransparency(new Double3(0.7)).setColor(Color.LIGHT_BLUE),
                //mirrors
                new Quadrangle(p1, p2, p5, p4).setReflectivity(new Double3(0.5)).setShininess(1000).setColor(Color.GOLD),
                new Quadrangle(p7, p0, p3, p6).setReflectivity(new Double3(0.5)).setShininess(1000).setColor(Color.GOLD)

                , new Sphere(new Point(2, 0, 3), 2)
                .setEmission(Color.BLUE)
                .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30).setKt(new Double3(0.7))),
                new Sphere(new Point(2, 0, 3), 1)
                        .setEmission(Color.BLUE)
                        .setMaterial(new Material().setKd(0.2).setKs(0.1).setShininess(30)),

                //close to window

                //close to camera bigger


                new Sphere(new Point(20, 2, 1.7), 1)
                        .setEmission(Color.GREEN)
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)),
                //close to camera smaller
                new Sphere(new Point(16, 3, 1.1), 1)
                        .setEmission(Color.BLUE)
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)),

                //heads
                new Sphere(new Point(20, 2, 2.8), 0.5)
                        .setEmission(Color.GREEN)
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)),
                //close to camera smaller
                new Sphere(new Point(16, 3, 2.25), 0.5)
                        .setEmission(Color.BLUE)
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)),
                new Triangle(p16, p17, p19).setEmission(Color.DARK_GRAY),
                new Triangle(p19, p18, p16).setEmission(Color.YELLOW)
        );

        LinkedList<LightSource> lights = new LinkedList<>();


        lights.add(new Spotlight(Color.MAGENTA,
                new Point(40, 40, 200),
                new Vector(-1, -1, -4)).setKl(0.0000001).setKq(0.0000001));
        lights.add(new PointLight(Color.YELLOW, new Point(100, -100, 100)));
        //????????????move it or change color
        lights.add(new DirectionalLight(Color.GOLD, new Vector(-2, -2, -5)));

        Scene scene2 = new Scene.SceneBuilder("Test scene").setGeometries(geometries)
                .setLights(lights).build();


        ImageWriter imageWriter = new ImageWriter("our test shadows chayaaaa", 600, 600);
        camera.setImageWriter(imageWriter)
                .setRayTracer(new RayTracerBasic(scene2).setSoftShadowsButton(true, 20))
                .renderImage()
                .writeToImage();
    }

    @Test
    public void makingATriangle() {

        Camera camera = new Camera(new Point(65, 1, 20), new Vector(-7, -0.5, -2), new Vector(-1, 0, 1))
                .setVPSize(300, 300).setVPDistance(800);

        Geometries geometries = new Geometries(new Triangle(new Point(0, 0, 0), new Point(0, 1, 0), new Point(1, 0, 0)).setEmission(Color.BROWN),
                new Sphere(new Point(0.5, 0.5, 0), 0.2).setEmission(Color.BLUE));

        LinkedList<LightSource> lights = new LinkedList<>();

        lights.add(new Spotlight(Color.MAGENTA,
                new Point(0.5, 0.5, 3),
                new Vector(0, 0, -1)).setKl(0.0000001).setKq(0.0000001));

        Scene scene2 = new Scene.SceneBuilder("Test scene").setGeometries(geometries)
                .setLights(lights).build();


        ImageWriter imageWriter = new ImageWriter("our test shadows nech", 600, 600);
        camera.setImageWriter(imageWriter)
                .setRayTracer(new RayTracerBasic(scene2).setSoftShadowsButton(true, 10))
                .renderImage()
                .writeToImage();
    }

    @Test
    public void lampTest1() {
        Point a1 = new Point(0.5, 0, 1.5);
        Point b1 = new Point(1, 0, 1);
        Point c1 = new Point(1.5, 0, 0.5);
        Point d1 = new Point(2, 0, 0);

        Point a2 = new Point(0.25, 0.433, 1.5);
        Point b2 = new Point(0.5, 0.866, 1);
        Point c2 = new Point(0.75, 1.2999, 0.5);
        Point d2 = new Point(1, 1.732, 0);

        Point a3 = new Point(-0.25, 0.433, 1.5);
        Point b3 = new Point(-0.5, 0.866, 1);
        Point c3 = new Point(-0.75, 1.2999, 0.5);
        Point d3 = new Point(-1, 1.732, 0);

        Point a4 = new Point(-0.5, 0, 1.5);
        Point b4 = new Point(-1, 0, 1);
        Point c4 = new Point(-1.5, 0, 0.5);
        Point d4 = new Point(-2, 0, 0);

        Point a5 = new Point(-0.25, -0.433, 1.5);
        Point b5 = new Point(-0.5, -0.866, 1);
        Point c5 = new Point(-0.75, -1.2999, 0.5);
        Point d5 = new Point(-1, -1.732, 0);

        Point a6 = new Point(0.25, -0.433, 1.5);
        Point b6 = new Point(0.5, -0.866, 1);
        Point c6 = new Point(0.75, -1.2999, 0.5);
        Point d6 = new Point(1, -1.732, 0);

        Geometries geometries = new Geometries(

                new Quadrangle(a1, a2, b2, b1).setColor(Color.BLUE).setTransparency(new Double3(0.9)),
                new Quadrangle(a2, a3, b3, b2).setColor(Color.BLUE).setTransparency(new Double3(0.9)),
                new Quadrangle(a3, a4, b4, b3).setColor(Color.BLUE).setTransparency(new Double3(0.9)),
                new Quadrangle(a4, a5, b5, b4).setColor(Color.BLUE).setTransparency(new Double3(0.9)),
                new Quadrangle(a5, a6, b6, b5).setColor(Color.BLUE).setTransparency(new Double3(0.9)),
                new Quadrangle(a6, a1, b1, b6).setColor(Color.BLUE).setTransparency(new Double3(0.9)),

                new Quadrangle(b1, b2, c2, c1).setColor(Color.GREEN).setTransparency(new Double3(0.9)),
                new Quadrangle(b2, b3, c3, c2).setColor(Color.GREEN).setTransparency(new Double3(0.9)),
                new Quadrangle(b3, b4, c4, c3).setColor(Color.GREEN).setTransparency(new Double3(0.9)),
                new Quadrangle(b4, b5, c5, c4).setColor(Color.GREEN).setTransparency(new Double3(0.9)),
                new Quadrangle(b5, b6, c6, c5).setColor(Color.GREEN).setTransparency(new Double3(0.9)),
                new Quadrangle(b6, b1, c1, c6).setColor(Color.GREEN).setTransparency(new Double3(0.9)),

                new Quadrangle(c1, c2, d2, d1).setColor(Color.RED).setTransparency(new Double3(0.9)),
                new Quadrangle(c2, c3, d3, d2).setColor(Color.RED).setTransparency(new Double3(0.9)),
                new Quadrangle(c3, c4, d4, d3).setColor(Color.RED).setTransparency(new Double3(0.9)),
                new Quadrangle(c4, c5, d5, d4).setColor(Color.RED).setTransparency(new Double3(0.9)),
                new Quadrangle(c5, c6, d6, d5).setColor(Color.RED).setTransparency(new Double3(0.9)),
                new Quadrangle(c6, c1, d1, d6).setColor(Color.RED).setTransparency(new Double3(0.9)),
                //new Sphere(new Point(0,2,2),1).setEmission(Color.BLUE).setMaterial(new Material().setKt(new Double3(0.9))),
                new Sphere(new Point(0,0,0),1).setEmission(Color.BLUE).setMaterial(new Material().setKt(new Double3(0.9)))
        );

        Camera camera = new Camera(new Point(4, 0, 0.5), new Vector(-1, 0, 0), new Vector(0, 0, 1))
                .setVPSize(200, 200).setVPDistance(100);

        LinkedList<LightSource> lights = new LinkedList<>();

        lights.add(new PointLight(Color.WHITE,
                new Point(4,0,2)).setKl(0.0000001).setKq(0.0000001));
       // lights.add(new PointLight(Color.YELLOW, new Point(0, 0, 1)));

        Scene scene = new Scene.SceneBuilder("Test scene").setGeometries(geometries)
                .setLights(lights).build();


        ImageWriter imageWriter = new ImageWriter("our test lamp", 600, 600);
        camera.setImageWriter(imageWriter)
                .setRayTracer(new RayTracerBasic(scene))
                .renderImage()
                .writeToImage();
    }

}
