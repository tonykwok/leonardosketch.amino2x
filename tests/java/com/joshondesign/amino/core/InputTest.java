package com.joshondesign.amino.core;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 4/6/11
 * Time: 1:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class InputTest {
    private static class Button extends Shape {

        private String txt;

        public Button(Core r, String s) {
            this.txt = s;
            this.setFill(Color.RED);
            r.listen("MOUSE_PRESS",this,new Callback() {
                public void call(Object o) {
                    if(getFill() == Color.RED) {
                        setFill(Color.CYAN);
                    } else {
                        setFill(Color.RED);
                    }
                }
            });
        }

        public boolean contains(Point2D pt) {
            double x= pt.getX();
            double y = pt.getY();
            if(x >= this.x && x <= this.x + 80) {
                if(y >= this.y && y<=this.y + 30) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public void draw(Graphics2D gfx) {
            gfx.setPaint(this.getFill());
            gfx.fillRect(0,0,80,30);
            gfx.setPaint(Color.BLACK);
            gfx.drawString(this.txt,5,20);
        }
    }
    public static void main(String ... args) throws Exception {
        final Core r = new Core();
        r.setSize(600,400);
        r.setBackground(Color.WHITE);



        Button b1 = new Button(r,"no trans");

        Transform tran = new Transform(new Button(r, "tx = 100")).setTranslateX(100);
        Transform tran2 = new Transform(new Button(r, "100,100, rot45")).setTranslateX(100).setTranslateY(100).setRotate(45);
        Transform tran3 = new Transform(
            new Transform(new Button(r, "nested trans"))
                .setTranslateX(50)
                .setTranslateY(50)
                .setRotate(30)
                )
            .setTranslateX(200)
            .setTranslateY(50).setRotate(30);
        Transform tran4 = new Transform(new Button(r, "scale"))
            .setTranslateX(300)
            .setTranslateY(50)
            .setScaleX(1.5).setScaleY(0.5);

        Group group1 = new Group().add(new Button(r, "in group")).setX(20).setY(150);


        r.setRoot(new Group().add(b1)
            .add(tran).add(tran2).add(tran3).add(tran4).add(group1)
            );
        r.setBackground(Color.GREEN);
        r.start();
    }
}
