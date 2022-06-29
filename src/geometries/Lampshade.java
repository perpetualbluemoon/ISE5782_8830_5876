package geometries;

import primitives.Point;
import primitives.Vector;

import java.util.LinkedList;

public class Lampshade {

    private Point _center = new Point(0,0,2);

    //Starting points for lamp creation. Letters indicate height and numbers indicate a direction (or angle)

    private Point a1 = new Point(0.5, 0, 1.5);
    private Point b1 = new Point(1, 0, 1);
    private Point c1 = new Point(1.5,0,0.5);
    private Point d1 = new Point(2,0,0);

    private Point a2 = new Point(0.25, 0.433,1.5);
    private Point b2 = new Point(0.5, 0.866, 1);
    private Point c2 = new Point(0.75, 1.2999, 0.5);
    private Point d2 = new Point(1, 1.732, 0);

    private Point a3 = new Point(-0.25, 0.433,1.5);
    private Point b3 = new Point(-0.5, 0.866, 1);
    private Point c3 = new Point(-0.75, 1.2999, 0.5);
    private Point d3 = new Point(-1, 1.732, 0);

    private Point a4 = new Point(-0.5, 0, 1.5);
    private Point b4 = new Point(-1, 0, 1);
    private Point c4 = new Point(-1.5,0,0.5);
    private Point d4 = new Point(-2,0,0);

    private Point a5 = new Point(-0.25, -0.433,1.5);
    private Point b5 = new Point(-0.5, -0.866, 1);
    private Point c5 = new Point(-0.75, -1.2999, 0.5);
    private Point d5 = new Point(-1, -1.732, 0);

    private Point a6 = new Point(0.25, -0.433,1.5);
    private Point b6 = new Point(0.5, -0.866, 1);
    private Point c6 = new Point(0.75, -1.2999, 0.5);
    private Point d6 = new Point(1, -1.732, 0);

    private Quadrangle A1 = new Quadrangle(a1,a2, b2, b1);
    private Quadrangle A2 = new Quadrangle(a2, a3, b3, b2);
    private Quadrangle A3 = new Quadrangle(a3, a4, b4, b3);
    private Quadrangle A4 = new Quadrangle(a4, a5, b5, b4);
    private Quadrangle A5 = new Quadrangle(a5, a6, b6, b5);
    private Quadrangle A6 = new Quadrangle(a6, a1, b1, b6);

    private Quadrangle B1 = new Quadrangle(b1,b2, c2, c1);
    private Quadrangle B2 = new Quadrangle(b2, b3, c3, c2);
    private Quadrangle B3 = new Quadrangle(b3, b4, c4, c3);
    private Quadrangle B4 = new Quadrangle(b4, b5, c5, c4);
    private Quadrangle B5 = new Quadrangle(b5, b6, c6, c5);
    private Quadrangle B6 = new Quadrangle(b6, b1, c1, c6);

    private Quadrangle C1 = new Quadrangle(c1, c2, d2, d1);
    private Quadrangle C2 = new Quadrangle(c2, c3, d3, d2);
    private Quadrangle C3 = new Quadrangle(c3, c4, d4, d3);
    private Quadrangle C4 = new Quadrangle(c4, c5, d5, d4);
    private Quadrangle C5 = new Quadrangle(c5, c6, d6, d5);
    private Quadrangle C6 = new Quadrangle(c6, c1, d1, d6);


    private LinkedList<Point> allPoints = new LinkedList<>();

    public Lampshade(Point center){
        Vector move_direction = new Point(0,0,0).subtract(center);
        _center = center;

        a1 = new Point(0.5, 0, 1.5).add(move_direction);
        a2 = new Point(0.25, 0.433,1.5).add(move_direction);
        a3 = new Point(-0.25, 0.433,1.5).add(move_direction);
        a4 = new Point(-0.5, 0, 1.5).add(move_direction);
        a5 = new Point(-0.25, -0.433,1.5).add(move_direction);
        a6 = new Point(0.25, -0.433,1.5).add(move_direction);

        b1 = new Point(1, 0, 1).add(move_direction);
        b2 = new Point(0.5, 0.866, 1).add(move_direction);
        b3 = b3.add(move_direction);
        b4 = b4.add(move_direction);
        b5 = b5.add(move_direction);
        b6 = b6.add(move_direction);

        c1 = c1.add(move_direction);
        c2 = c2.add(move_direction);
        c3 = c3.add(move_direction);
        c4 = c4.add(move_direction);
        c5 = c5.add(move_direction);
        c6 = c6.add(move_direction);

        d1 = d1.add(move_direction);
        d2 = d2.add(move_direction);
        d3 = d3.add(move_direction);
        d4 = d4.add(move_direction);
        d5 = d5.add(move_direction);
        d6 = d6.add(move_direction);

         Quadrangle A1 = new Quadrangle(a1,a2, b2, b1);
         Quadrangle A2 = new Quadrangle(a2, a3, b3, b2);
         Quadrangle A3 = new Quadrangle(a3, a4, b4, b3);
         Quadrangle A4 = new Quadrangle(a4, a5, b5, b4);
         Quadrangle A5 = new Quadrangle(a5, a6, b6, b5);
         Quadrangle A6 = new Quadrangle(a6, a1, b1, b6);

         Quadrangle B1 = new Quadrangle(b1,b2, c2, c1);
         Quadrangle B2 = new Quadrangle(b2, b3, c3, c2);
         Quadrangle B3 = new Quadrangle(b3, b4, c4, c3);
         Quadrangle B4 = new Quadrangle(b4, b5, c5, c4);
         Quadrangle B5 = new Quadrangle(b5, b6, c6, c5);
         Quadrangle B6 = new Quadrangle(b6, b1, c1, c6);

         Quadrangle C1 = new Quadrangle(c1, c2, d2, d1);
         Quadrangle C2 = new Quadrangle(c2, c3, d3, d2);
         Quadrangle C3 = new Quadrangle(c3, c4, d4, d3);
         Quadrangle C4 = new Quadrangle(c4, c5, d5, d4);
         Quadrangle C5 = new Quadrangle(c5, c6, d6, d5);
         Quadrangle C6 = new Quadrangle(c6, c1, d1, d6);
    }

}
