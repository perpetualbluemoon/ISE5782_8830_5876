/**
 *
 */
package renderer;

import org.junit.jupiter.api.Test;

import static java.awt.Color.*;

import lighting.*;
import geometries.*;
import primitives.*;
import scene.Scene;

import java.util.LinkedList;

/**
 * Tests for reflection and transparency functionality, test for partial shadows
 * (with transparency)
 *
 * @author dzilb
 */
public class ReflectionRefractionTests {
    private Scene scene = new Scene.SceneBuilder("Test scene").build();

    /**
     * Produce a picture of a sphere lighted by a spot light
     */
    @Test
    public void twoSpheres() {
        Geometries geometries = new Geometries(
                new Sphere(new Point(0, 0, -50), 50d).setEmission(new Color(BLUE)) //
                        .setMaterial(new Material().setKd(0.4).setKs(0.3).setShininess(100).setKt(new Double3(0.3))),
                new Sphere(new Point(0, 0, -50), 25d).setEmission(new Color(RED)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(100))
        );
        LinkedList<LightSource> lights = new LinkedList<>();
        lights.add(
                new Spotlight(new Color(1000, 600, 0), new Point(-100, -100, 500), new Vector(-1, -1, -2)) //
                        .setKl(0.0004).setKq(0.0000006));
        Scene scene2 = new Scene.SceneBuilder("Test scene").setGeometries(geometries)
                .setLights(lights).build();

        Camera camera = new Camera(new Point(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
                .setVPSize(150, 150).setVPDistance(1000);

        camera.setImageWriter(new ImageWriter("refractionTwoSpheres", 500, 500)) //
                .setRayTracer(new RayTracerBasic(scene2)) //
                .renderImage() //
                .writeToImage();
    }

    /**
     * Produce a picture of a sphere lighted by a spot light
     */
    @Test
    public void twoSpheresOnMirrors() {
        Camera camera = new Camera(new Point(0, 0, 10000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
                .setVPSize(2500, 2500).setVPDistance(10000); //

        Geometries geometries = new Geometries(
                new Sphere(new Point(-950, -900, -1000), 400d).setEmission(new Color(0, 0, 100)) //
                .setMaterial(new Material().setKd(0.25).setKs(0.25).setShininess(20).setKt(new Double3(0.5))),
                new Sphere(new Point(-950, -900, -1000), 200d).setEmission(new Color(100, 20, 20)) //
                        .setMaterial(new Material().setKd(0.25).setKs(0.25).setShininess(20)),
                new Triangle(new Point(1500, -1500, -1500), new Point(-1500, 1500, -1500), new Point(670, 670, 3000)) //
                        .setEmission(new Color(20, 20, 20)) //
                        .setMaterial(new Material().setKr(new Double3(1))),
                new Triangle(new Point(1500, -1500, -1500), new Point(-1500, 1500, -1500),
                        new Point(-1500, -1500, -2000)) //
                        .setEmission(new Color(20, 20, 20)) //
                        .setMaterial(new Material().setKr(new Double3(0.5))));

        LinkedList<LightSource> lights = new LinkedList<>();

        lights.add(new Spotlight(new Color(1020, 400, 400), new Point(-750, -750, -150), new Vector(-1, -1, -4)) //
                .setKl(0.00001).setKq(0.000005));


        Scene scene1 = new Scene.SceneBuilder("reflectionTwoSpheresMirrored").setGeometries(geometries).setLights(lights)
                .setAmbientLight(new AmbientLight(new Color(255, 255, 255), new Double3(0.1))).build();


        ImageWriter imageWriter = new ImageWriter("reflectionTwoSpheresMirrored", 500, 500);
        camera.setImageWriter(imageWriter) //
                .setRayTracer(new RayTracerBasic(scene1)) //
                .renderImage() //
                .writeToImage();
    }

    /**
     * Produce a picture of a two triangles lighted by a spot light with a partially
     * transparent Sphere producing partial shadow
     */
    @Test
    public void trianglesTransparentSphere() {
        Camera camera = new Camera(new Point(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
                .setVPSize(200, 200).setVPDistance(1000);

        Geometries geometries = new Geometries(
                new Triangle(new Point(-150, -150, -115), new Point(150, -150, -135), new Point(75, 75, -150)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60)), //
                new Triangle(new Point(-150, -150, -115), new Point(-70, 70, -140), new Point(75, 75, -150)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60)), //
                new Sphere(new Point(60, 50, -50), 30d).setEmission(new Color(BLUE)) //
                        .setMaterial(new Material().setKd(0.2).setKs(0.2).setShininess(30).setKt(new Double3(0.6)))
        );
        LinkedList<LightSource> lights = new LinkedList<>();

        lights.add(new Spotlight(new Color(700, 400, 400), new Point(60, 50, 0), new Vector(0, 0, -1)) //
                .setKl(4E-5).setKq(2E-7));

        Scene scene2 = new Scene.SceneBuilder("Test scene").setGeometries(geometries)
                .setLights(lights).build();

        ImageWriter imageWriter = new ImageWriter("refractionShadow", 600, 600);
        camera.setImageWriter(imageWriter) //
                .setRayTracer(new RayTracerBasic(scene2)) //
                .renderImage() //
                .writeToImage();
    }

    /**
     * our test
     */
    @Test
    public void imageWithTenShapes() {
        Camera camera = new Camera(new Point(80, 0, 2), new Vector(-1, 0, 0), new Vector(0, 0, 1))
                .setVPSize(200, 200).setVPDistance(1000);

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

        Point p16 = new Point(0,-10,0.5);
        Point p17 = new Point(0,10,0.5);
        Point p18 = new Point(100, -10, 0.5);
        Point p19 = new Point(100, 10, 0.5);

        Geometries geometries = new Geometries(
                //window
                new Quadrangle(p0, p1, p2, p3).setTransparency(new Double3(0.7)).setColor(Color.LIGHT_BLUE),
                //mirrors
                new Quadrangle(p1, p2, p5, p4).setReflectivity(new Double3(0.5)).setShininess(1000).setColor(Color.GOLD),
                new Quadrangle(p7, p0, p3, p6).setReflectivity(new Double3(0.5)).setShininess(1000).setColor(Color.GOLD),
                //sun
                new Sphere(new Point(-100, 0, 2), 2d)
                        .setEmission(Color.RED)
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)),
                //close to window
                new Sphere(new Point(1, -0.75, 2), 0.5)
                        .setEmission(Color.RED)
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)),
                //close to camera bigger
                new Sphere(new Point(63, 0.75, 1.1), 0.5)
                        .setEmission(Color.GREEN)
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)),
                new Sphere(new Point(63, 0.75, 1.7), 0.2)
                        .setEmission(Color.GREEN)
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)),
                //close to camera smaller
                new Sphere(new Point(59, 0, 1.1), 0.4)
                        .setEmission(Color.BLUE)
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)),
                new Sphere(new Point(59, 0, 1.6), 0.15)
                        .setEmission(Color.BLUE)
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)),
                //floor
                //new Quadrangle(p16, p17, p19, p18).setColor(Color.YELLOW)
                new Triangle(p16,p17,p19).setEmission(Color.DARK_GRAY),
                new Triangle(p19,p18,p16).setEmission(Color.YELLOW)
        );
        LinkedList<LightSource> lights = new LinkedList<>();

        lights.add(new Spotlight(new Color(700, 400, 400),
                new Point(40, 40, 115),
                new Vector(-1, -1, -4)));
        lights.add(new PointLight(Color.BLUE, new Point(100, -100, 100)));
        lights.add(new DirectionalLight(Color.MAGENTA,new Vector(-1,0,0)));


        Scene scene2 = new Scene.SceneBuilder("Test scene").setGeometries(geometries)
                .setLights(lights).build();

        ImageWriter imageWriter = new ImageWriter("our test shapes", 600, 600);
        camera.setImageWriter(imageWriter)
                .setRayTracer(new RayTracerBasic(scene2))
                .renderImage()
                .writeToImage();
    }

    /**
     * our test
     */
    @Test
    public void ourImageWithDepth() {
        Camera camera = new Camera(new Point(80, 0, 2), new Vector(-1, 0, 0), new Vector(0, 0, 1))
                .setVPSize(200, 200).setVPDistance(1000);

        camera.setDepthButton(true, 0.02, 13);

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

        Point p16 = new Point(0,-10,0.5);
        Point p17 = new Point(0,10,0.5);
        Point p18 = new Point(100, -10, 0.5);
        Point p19 = new Point(100, 10, 0.5);

        Geometries geometries = new Geometries(
                //window
                new Quadrangle(p0, p1, p2, p3).setTransparency(new Double3(0.7)).setColor(Color.LIGHT_BLUE),
                //mirrors
                new Quadrangle(p1, p2, p5, p4).setReflectivity(new Double3(0.5)).setShininess(1000).setColor(Color.GOLD),
                new Quadrangle(p7, p0, p3, p6).setReflectivity(new Double3(0.5)).setShininess(1000).setColor(Color.GOLD),
                //sun
                new Sphere(new Point(-100, 0, 2), 2d)
                        .setEmission(Color.RED)
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)),
                //close to window
                new Sphere(new Point(1, -0.75, 2), 0.5)
                        .setEmission(Color.RED)
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)),
                //close to camera bigger
                new Sphere(new Point(63, 0.75, 1.1), 0.5)
                        .setEmission(Color.GREEN)
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)),
                new Sphere(new Point(63, 0.75, 1.7), 0.2)
                        .setEmission(Color.GREEN)
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)),
                //close to camera smaller
                new Sphere(new Point(59, 0, 1.1), 0.4)
                        .setEmission(Color.BLUE)
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)),
                new Sphere(new Point(59, 0, 1.6), 0.15)
                        .setEmission(Color.BLUE)
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)),
                //floor
                //new Quadrangle(p16, p17, p19, p18).setColor(Color.YELLOW)
                new Triangle(p16,p17,p19).setEmission(Color.DARK_GRAY),
                new Triangle(p19,p18,p16).setEmission(Color.YELLOW)
        );

        LinkedList<LightSource> lights = new LinkedList<>();


        lights.add(new Spotlight(new Color(700, 400, 400),
                new Point(40, 40, 115),
                new Vector(-1, -1, -4)));
        lights.add(new PointLight(Color.BLUE, new Point(100, -100, 100)));
        lights.add(new DirectionalLight(Color.MAGENTA,new Vector(-1,0,0)));

        Scene scene2 = new Scene.SceneBuilder("Test scene").setGeometries(geometries)
                .setLights(lights).build();


        ImageWriter imageWriter = new ImageWriter("our test depth", 600, 600);
        camera.setImageWriter(imageWriter)
                .setRayTracer(new RayTracerBasic(scene2))
                .renderImage()
                .writeToImage();
    }

    /**
     * our test
     */
    @Test
    public void ourJaggedEdgesOn() {
        Camera camera = new Camera(new Point(80, 0, 2), new Vector(-1, 0, 0), new Vector(0, 0, 1))
                .setVPSize(200, 200).setVPDistance(1000);

        camera.setJaggedEdgesButton(true, 9);


        Geometries geometries = new Geometries(//close to camera bigger
                new Sphere(new Point(40, 0, 2.5), 3)
                        .setEmission(Color.GREEN)
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)));
        LinkedList<LightSource> lights = new LinkedList<>();

//        lights.add(new Spotlight(new Color(700, 400, 400),
//                new Point(40, 40, 115),
//                new Vector(-1, -1, -4)));
//        lights.add(new PointLight(Color.BLUE, new Point(100, -100, 100)));
        lights.add(new DirectionalLight(Color.MAGENTA,new Vector(-1,-1,0)));

        Scene scene2 = new Scene.SceneBuilder("Test scene").setGeometries(geometries)
                .setLights(lights).build();

        ImageWriter imageWriter = new ImageWriter("jagged edges on - 9", 600, 600);
        camera.setImageWriter(imageWriter)
                .setRayTracer(new RayTracerBasic(scene2))
                .renderImage()
                .writeToImage();
    }
    @Test
    public void jaggedEdgesOff() {
        Camera camera = new Camera(new Point(80, 0, 2), new Vector(-1, 0, 0), new Vector(0, 0, 1))
                .setVPSize(200, 200).setVPDistance(1000);

        camera.setJaggedEdgesButton(false, 9);


        Geometries geometries = new Geometries(//close to camera bigger
                new Sphere(new Point(40, 0, 2.5), 3)
                        .setEmission(Color.GREEN)
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)));
        LinkedList<LightSource> lights = new LinkedList<>();

//        lights.add(new Spotlight(new Color(700, 400, 400),
//                new Point(40, 40, 115),
//                new Vector(-1, -1, -4)));
//        lights.add(new PointLight(Color.BLUE, new Point(100, -100, 100)));
        lights.add(new DirectionalLight(Color.MAGENTA,new Vector(-1,-1,0)));

        Scene scene2 = new Scene.SceneBuilder("Test scene").setGeometries(geometries)
                .setLights(lights).build();

        ImageWriter imageWriter = new ImageWriter("jagged edges off 9", 600, 600);
        camera.setImageWriter(imageWriter)
                .setRayTracer(new RayTracerBasic(scene2))
                .renderImage()
                .writeToImage();
    }


    /**
     * our test
     */
    @Test
    public void ourImageWithSmoothEdges() {
        Camera camera = new Camera(new Point(80, 0, 2), new Vector(-1, 0, 0), new Vector(0, 0, 1))
                .setVPSize(200, 200).setVPDistance(1000);

        camera.setJaggedEdgesButton(true, 3);

        camera.setJaggedEdgesButton(true);
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

        Point p16 = new Point(0,-10,0.5);
        Point p17 = new Point(0,10,0.5);
        Point p18 = new Point(100, -10, 0.5);
        Point p19 = new Point(100, 10, 0.5);

        Geometries geometries = new Geometries(
                //window
                new Quadrangle(p0, p1, p2, p3).setTransparency(new Double3(0.7)).setColor(Color.LIGHT_BLUE),
                //mirrors
                new Quadrangle(p1, p2, p5, p4).setReflectivity(new Double3(0.5)).setShininess(1000).setColor(Color.GOLD),
                new Quadrangle(p7, p0, p3, p6).setReflectivity(new Double3(0.5)).setShininess(1000).setColor(Color.GOLD),
                //sun
                new Sphere(new Point(-100, 0, 2), 2d)
                        .setEmission(Color.RED)
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)),
                //close to window
                new Sphere(new Point(1, -0.75, 2), 0.5)
                        .setEmission(Color.RED)
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)),
                //close to camera bigger
                new Sphere(new Point(63, 0.75, 1.1), 0.5)
                        .setEmission(Color.GREEN)
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)),
                new Sphere(new Point(63, 0.75, 1.7), 0.2)
                        .setEmission(Color.GREEN)
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)),
                //close to camera smaller
                new Sphere(new Point(59, 0, 1.1), 0.4)
                        .setEmission(Color.BLUE)
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)),
                new Sphere(new Point(59, 0, 1.6), 0.15)
                        .setEmission(Color.BLUE)
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)),
                //floor
               // new Quadrangle(p16, p17, p19, p18).setColor(Color.YELLOW),
                new Triangle(p16,p17,p19).setEmission(Color.DARK_GRAY),
                new Triangle(p19,p18,p16).setEmission(Color.YELLOW)
        );
        LinkedList<LightSource> lights = new LinkedList<>();

        lights.add(new Spotlight(new Color(700, 400, 400),
                new Point(40, 40, 115),
                new Vector(-1, -1, -4)));
        lights.add(new PointLight(Color.BLUE, new Point(100, -100, 100)));
        lights.add(new DirectionalLight(Color.MAGENTA,new Vector(-1,0,0)));


        Scene scene2 = new Scene.SceneBuilder("Test scene").setGeometries(geometries)
                .setLights(lights).build();

        ImageWriter imageWriter = new ImageWriter("our test shapes smooth edges", 600, 600);
        camera.setImageWriter(imageWriter)
                .setRayTracer(new RayTracerBasic(scene2))
                .renderImage()
                .writeToImage();
    }

    @Test
    public void complexImageWithTenShapes() {
        Camera camera = new Camera(new Point(55, 5, 20), new Vector(-7, -0.5, -2), new Vector(-1, 0, 1))
                .setVPSize(300, 300).setVPDistance(1000);

        camera.setJaggedEdgesButton(true, 3);


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

        Point p16 = new Point(0,-10,0.5);
        Point p17 = new Point(0,10,0.5);
        Point p18 = new Point(100, -10, 0.5);
        Point p19 = new Point(100, 10, 0.5);

        Geometries geometries = new Geometries(
                //window
                new Quadrangle(p0, p1, p2, p3).setTransparency(new Double3(0.7)).setColor(Color.LIGHT_BLUE),
                //mirrors
                new Quadrangle(p1, p2, p5, p4).setReflectivity(new Double3(0.5)).setShininess(1000).setColor(Color.GOLD),
                new Quadrangle(p7, p0, p3, p6).setReflectivity(new Double3(0.5)).setShininess(1000).setColor(Color.GOLD),

                //close to camera smaller- body
                new Sphere(new Point(2, 0, 3), 2)
                        .setEmission(Color.BLUE)
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30).setKt(new Double3(0.7))),
                new Sphere(new Point(2, 0, 3), 1)
                        .setEmission(Color.BLUE)
                        .setMaterial(new Material().setKd(0.2).setKs(0.1).setShininess(30)),

                //floor
                //new Quadrangle(p16, p17, p19, p18).setColor(Color.YELLOW)
                new Triangle(p16,p17,p19).setEmission(Color.DARK_GRAY),
                new Triangle(p19,p18,p16).setEmission(Color.DARK_GRAY)

                //cube
                // new Quadrangle(p8, p9, p10, p11),
                //new Quadrangle(p12, p13, p14, p15),
                //new Quadrangle(p13, p9, p10, p14),
                //new Quadrangle(p14, p10, p11, p15),
                //new Quadrangle(p15, p12, p8, p11),
                //new Quadrangle(p12, p13, p9, p8)
        );
        LinkedList<LightSource> lights = new LinkedList<>();

        lights.add(new Spotlight(Color.MAGENTA,
                new Point(40, 40, 200),
                new Vector(-1, -1, -4)).setKl(2E-5).setKq(2E-5));
        lights.add(new PointLight(Color.YELLOW, new Point(100, -100, 100)));
        //????????????move it or change color
        lights.add(new DirectionalLight(Color.GOLD,new Vector(-2, -2, -5)));

        Scene scene2 = new Scene.SceneBuilder("Test scene").setGeometries(geometries)
                .setLights(lights)
                .setAmbientLight(new AmbientLight(Color.BROWN, new Double3(0.15)))
                .setBackground(Color.BLACK).build();

        ImageWriter imageWriter = new ImageWriter("our test shapes chaya", 600, 600);
        camera.setImageWriter(imageWriter)
                .setRayTracer(new RayTracerBasic(scene2))
                .renderImage()
                .writeToImage();
    }
}

