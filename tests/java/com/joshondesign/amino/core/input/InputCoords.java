package com.joshondesign.amino.core.input;

import com.joshondesign.amino.core.*;

import java.awt.geom.Point2D;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 4/6/11
 * Time: 1:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class InputCoords implements Core.InitCallback {

    private static class Button extends Shape {

        private String txt;
        private Core core;
        private AminoFont font;

        public Button(Core r, String s) {
            this.txt = s;
            this.core = r;
            this.setFill(AminoColor.RED);
            try {
                font = core.loadFont("Arial").withSize(12);
            } catch (IOException e1) {
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            r.listen("MOUSE_PRESS",this,new Callback() {
                public void call(Object o) {
                    if(getFill() == AminoColor.RED) {
                        setFill(AminoColor.CYAN);
                    } else {
                        setFill(AminoColor.RED);
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
        public void draw(GFX gfx) {
            gfx.setPaint(this.getFill());
            gfx.fillRect(0, 0, 80, 30);
            gfx.setPaint(AminoColor.BLACK);
            gfx.fillText(font, this.txt, 5, 20);
        }
    }

    public static void main(String ... args) throws Exception {
        Core.init(new InputCoords());
    }
    public void call(Core core) throws Exception {
        Window window = core.createResizableWindow(600,400);
        window.setBackgroundFill(AminoColor.WHITE);


        Button b1 = new Button(core,"no trans");

        Transform tran = new Transform(new Button(core, "tx = 100")).setTranslateX(100);
        Transform tran2 = new Transform(new Button(core, "100,100, rot45")).setTranslateX(100).setTranslateY(100).setRotate(45);
        Transform tran3 = new Transform(
            new Transform(new Button(core, "nested trans"))
                .setTranslateX(50)
                .setTranslateY(50)
                .setRotate(30)
                )
            .setTranslateX(200)
            .setTranslateY(50).setRotate(30);
        Transform tran4 = new Transform(new Button(core, "scale"))
            .setTranslateX(300)
            .setTranslateY(50)
            .setScaleX(1.5).setScaleY(0.5);

        Group group1 = new Group().add(new Button(core, "in group")).setX(20).setY(150);


        window.setRoot(new Group().add(b1)
            .add(tran).add(tran2).add(tran3).add(tran4).add(group1)
            );
    }
}
