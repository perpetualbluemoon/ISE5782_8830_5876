package renderer;

import geometries.Geometries;
import geometries.Quadrangle;
import geometries.Sphere;
import geometries.Triangle;
import lighting.*;
import org.junit.jupiter.api.Test;
import primitives.*;
import scene.Scene;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Testing Camera Class
 */
class CameraTest {
    static final Point ZERO_POINT = new Point(0, 0, 0);

    /**
     * Test method for
     * {@link renderer.Camera#constructRay(int, int, int, int)}.
     */
    @Test
    void testConstructRay() {
        Camera camera = new Camera(ZERO_POINT, new Vector(0, 0, -1), new Vector(0, -1, 0)).setVPDistance(10);
        String badRay = "Bad ray";

        // ============ Equivalence Partitions Tests ==============
        // EP01: 4X4 Inside (1,1)
        assertEquals(new Ray(ZERO_POINT, new Vector(1, -1, -10)),
                camera.setVPSize(8, 8).constructRay(4, 4, 1, 1), badRay);

        // =============== Boundary Values Tests ==================
        // BV01: 3X3 Center (1,1)
        assertEquals(new Ray(ZERO_POINT, new Vector(0, 0, -10)),
                camera.setVPSize(6, 6).constructRay(3, 3, 1, 1), badRay);

        // BV02: 3X3 Center of Upper Side (0,1)
        assertEquals(new Ray(ZERO_POINT, new Vector(0, -2, -10)),
                camera.setVPSize(6, 6).constructRay(3, 3, 1, 0), badRay);

        // BV03: 3X3 Center of Left Side (1,0)
        assertEquals(new Ray(ZERO_POINT, new Vector(2, 0, -10)),
                camera.setVPSize(6, 6).constructRay(3, 3, 0, 1), badRay);

        // BV04: 3X3 Corner (0,0)
        assertEquals(new Ray(ZERO_POINT, new Vector(2, -2, -10)),
                camera.setVPSize(6, 6).constructRay(3, 3, 0, 0), badRay);

        // BV05: 4X4 Corner (0,0)
        assertEquals(new Ray(ZERO_POINT, new Vector(3, -3, -10)),
                camera.setVPSize(8, 8).constructRay(4, 4, 0, 0), badRay);

        // BV06: 4X4 Side (0,1)
        assertEquals(new Ray(ZERO_POINT, new Vector(1, -3, -10)),
                camera.setVPSize(8, 8).constructRay(4, 4, 1, 0), badRay);

    }
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
        Camera camera = new Camera(new Point(65, 5, 20), new Vector(-7, -0.5, -2), new Vector(-1, 0, 1))
               .setVPSize(300, 300).setVPDistance(800).setAdaptiveSuperSampling(false);

       //Camera camera = new Camera(new Point(0,0,100), new Vector(0.2,0,-1), new Vector(-1, 0, 1))
            //    .setVPSize(300, 300).setVPDistance(800).setAdaptiveSuperSampling(false);
       // camera.setDepthButton(true, 2, 50);

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
                new Quadrangle(p7, p0, p3, p6).setReflectivity(new Double3(0.5)).setShininess(1000).setColor(Color.GOLD)

                ,new Sphere(new Point(2, 0, 3), 2)
                .setEmission(Color.BLUE)
                .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30).setKt(new Double3(0.7))),
                new Sphere(new Point(2, 0, 3), 1)
                        .setEmission(Color.BLUE)
                        .setMaterial(new Material().setKd(0.2).setKs(0.1).setShininess(30)),



              //BODY OF GREEN
                new Sphere(new Point(16, 4, 1.7), 3)
                        .setEmission(Color.BLUE)
                         .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)),
                //BODY OF BLUE
                new Sphere(new Point(20, 6.5, 1.1), 3)
                        .setEmission(Color.BLUE)
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)),
                //BODY OF RED
                new Sphere(new Point(20, 1.5, 1.1), 3)
                        .setEmission(Color.BLUE)
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)),

                //HEAD OF GREEN
                new Sphere(new Point(16, 4, 4.5), 2)
                        .setEmission(Color.YELLOW)
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)),//.setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30).setKt(new Double3(0.7))),
                //HEAD OF BLUE
                new Sphere(new Point(20,6.5, 3.5), 2)
                        .setEmission(Color.YELLOW)
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)),

                //HEAD OF RED
                new Sphere(new Point(20,1.5, 3.5), 2)
                        .setEmission(Color.YELLOW)
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)),
                //PUPIL OF GREEN
                new Sphere(new Point(17.2, 4, 5.6), 0.5)
                        .setEmission(Color.BLACK)
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)),
                //PUPIL OF BLUE
                new Sphere(new Point(21.2,6.5, 4.6), 0.5)
                        .setEmission(Color.BLACK)
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)),

                //PUPIL OF RED
                new Sphere(new Point(21.2,1.5, 4.6), 0.5)
                        .setEmission(Color.BLACK)
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)),
                //WHITE PART OF EYE
                //WHITE EYE OF GREEN
                new Sphere(new Point(16.7, 4, 5.3), 1)
                        .setEmission(Color.WHITE)
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)),
                //WHITE EYE OF BLUE
                new Sphere(new Point(20.7,6.5, 4.3), 1)
                        .setEmission(Color.WHITE)
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)),

                //WHITE EYE OF RED
                new Sphere(new Point(20.7,1.5, 4.3), 1)
                        .setEmission(Color.WHITE)
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)),
                //goggle part of minion
                //goggle green
//                // new Sphere(new Point(17, 4, 5.3), 1)
//                        .setEmission(Color.DARK_GRAY)
//                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)),
//                //goggle BLUE
//                new Sphere(new Point(21,6.5, 4.3), 1)
//                        .setEmission(Color.DARK_GRAY)
//                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)),
//
//                //goggle RED
//                new Sphere(new Point(21,1.5, 4.3), 1)
//                        .setEmission(Color.DARK_GRAY)
//                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)),


                new Triangle(p16,p17,p19).setEmission(Color.DARK_GRAY),
                new Triangle(p19,p18,p16).setEmission(Color.YELLOW)
        );

        LinkedList<LightSource> lights = new LinkedList<>();
        lights.add(new Spotlight(Color.MAGENTA,
                new Point(40, 40, 200),
                new Vector(-1, -1, -4)).setKl(0.0001).setKq(0.0001));
        lights.add(new PointLight(Color.YELLOW, new Point(100, -100, 100)));
        //????????????move it or change color
        lights.add(new DirectionalLight(Color.GOLD,new Vector(-2, -2, -2)));


/*

        lights.add(new Spotlight(Color.MAGENTA,
                new Point(-50, 40, 200),
                new Vector(5, -1, -4)).setKl(0.0000001).setKq(0.000001));
        lights.add(new PointLight(Color.YELLOW, new Point(-100, -100, 100)).setKq(0.0000000001).setKl(0.000000001));
        //????????????move it or change color
        //lights.add(new DirectionalLight(Color.GOLD,new Vector(0, 1, 0)));
*/

        Scene scene2 = new Scene.SceneBuilder("Test scene").setGeometries(geometries)
                .setLights(lights).build();


        ImageWriter imageWriter = new ImageWriter("our test depth", 600, 600);
        camera.setImageWriter(imageWriter)
                .setRayTracer(new RayTracerBasic(scene2).setSoftShadowsButton(true,20))
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
        lights.add(new DirectionalLight(Color.MAGENTA,new Vector(-1,-1,-1)));

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
        Camera camera = new Camera(new Point(55, 5, 20), new Vector(-7, -0.5, -2), new Vector(-1, 0, 1))
                .setVPSize(300, 300).setVPDistance(1000);


        camera.setJaggedEdgesButton(true, 10);

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
    @Test
    public void startAllOver() {
        Camera camera=new Camera(new Point(100,0,0),new Vector(-1,0,0),new Vector(0,0,1)).setVPSize(300, 300).setVPDistance(1000);
        Geometries geometries = new Geometries(new Sphere(new Point(0,0,3),3).setEmission(Color.RED),
                new Triangle(new Point(0,-5,0.1),new Point(5,-5,-1),new Point(0,5,0)).setEmission(Color.LIGHT_BLUE)
        );
        LinkedList<LightSource> lights = new LinkedList<>();
        lights.add(new DirectionalLight(Color.GOLD,new Vector(-2, -2, -5)));
        Scene scene2 = new Scene.SceneBuilder("Test scene").setGeometries(geometries)
                .setLights(lights)
                .setAmbientLight(new AmbientLight(Color.BROWN, new Double3(0.15)))
                .setBackground(Color.BLACK).build();

        ImageWriter imageWriter = new ImageWriter("start all over", 600, 600);
        camera.setImageWriter(imageWriter)
                .setRayTracer(new RayTracerBasic(scene2))
                .renderImage()
                .writeToImage();
    }
}



