package com.joshondesign.amino.core;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 3/10/11
 * Time: 2:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class DragNodes {
    private static Dragger dragger;

    public static void main(String ... args) {
        Core runner = new Core();
        //runner.setSize(700,400);
        //runner.setBackground(Color.WHITE);

        Group g = new Group();
        runner.root = g;

        Rect r1 = new Rect().set(200, 10, 100, 100).setCorner(10);
        r1.setFill(AminoColor.fromRGB(0xccddff)).setStrokeWidth(4);
        Rect r2 = new Rect().set(40, 200, 100, 100).setCorner(10);
        r2.setFill(AminoColor.fromRGB(0xffccdd)).setStrokeWidth(4);
        Rect r3 = new Rect().set(300, 200, 100, 100).setCorner(10);
        r3.setFill(AminoColor.fromRGB(0xccffdd)).setStrokeWidth(4);

        Connector c1 = new Connector(r1,r2);
        Connector c2 = new Connector(r1,r3);
        g.add(c1).add(c2);
        g.add(r1).add(r2).add(r3);


        dragger = new Dragger(runner);


        runner.start();

    }
}


class Dragger {
    private Core runner;
    double sx = 0;
    double sy = 0;


    public Dragger(Core runner) {
        this.runner = runner;
        runner.listen("MOUSE_PRESS",null,new Callback<MEvent>() {
            public void call(MEvent e) {
                sx = e.getX();
                sy = e.getY();
            }
        });
        runner.listen("MOUSE_DRAG",null,new Callback<MEvent>(){
            public void call(MEvent e) {
                if(e.getNode() != null) {
                    double dx = e.getX()-sx;
                    double dy = e.getY()-sy;
                    sx = e.getX();
                    sy = e.getY();
                    e.getNode().setX(e.getNode().getX()+dx);
                    e.getNode().setY(e.getNode().getY()+dy);
                }
            }
        });
        runner.listen("MOUSE_RELEASE",null,new Callback<MEvent>(){
            public void call(MEvent e) {
                sx = e.getX();
                sy = e.getY();
            }
        });

    }
}

class Connector extends Shape {
    private Rect r1;
    private Rect r2;

    public Connector(Rect r1, Rect r2) {
        this.r1 = r1;
        this.r2 = r2;
    }

    @Override
    public void draw(GFX gfx) {
        gfx.setPaint(AminoColor.BLACK);
        //gfx.setStroke(new BasicStroke(3));
        gfx.drawLine((int)(r1.getX()+r1.getWidth()/2),
                (int)(r1.getY()+r1.getHeight()/2),
                (int)(r2.getX()+r2.getWidth()/2),
                (int)(r2.getY()+r2.getHeight()/2)
        );
    }
};
