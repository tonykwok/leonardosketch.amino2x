package com.joshondesign.amino.core.simple;

import com.joshondesign.amino.core.*;
import com.joshondesign.amino.core.Window;

import java.awt.*;
import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: josh
 * Date: 9/19/11
 * Time: 1:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class FilledShapes implements Core.InitCallback {
    public static void main(String ... args) {
        Core.init(new FilledShapes());
    }

    public void call(Core core) throws AminoException {
        Window window = core.createResizableWindow(800, 600);
        window.setBackgroundFill(Color.BLACK);

        /*
        final LinearGradient gradient = new VerticalLinearGradient(0,50)
                .addColor(0.0,SColor.BLUE)
                .addColor(0.3,SColor.WHITE)
                .addColor(0.7, SColor.RED)
                .addColor(1.0, SColor.WHITE);

        final LinearGradient gradient2 = new HorizontalLinearGradient(0,50)
                .addColor(0.0,SColor.BLUE)
                .addColor(0.3,SColor.WHITE)
                .addColor(0.7,SColor.RED)
                .addColor(1.0,SColor.WHITE);
        final SPattern pattern = new SPattern(new File("src/resources/checkerboard.png"));
        */
        window.setRoot(new Node() {

            @Override
            public void draw(Graphics2D gfx) {
            }

            @Override
            public void draw(GFX gfx) {

                gfx.setPaint(Color.RED);
                doDrawing(gfx);

                /*
                gfx.translate(250,0);
                gfx.setPaint(gradient);
                doDrawing(gfx);

                gfx.setPaint(gradient2);
                gfx.translate(250, 0);
                doDrawing(gfx);

                gfx.setPaint(pattern);
                gfx.translate(250,0);
                doDrawing(gfx);


                */
                gfx.translate(-250*0,0);
            }
        });

        core.start();
    }

    private void doDrawing(GFX gfx) {
        gfx.drawLine(       0,  0,      200,    40);
        gfx.drawRect(       0,  50,     200,    40);
        gfx.fillRect(       0,  100,    200,    40);
        //gfx.drawRoundRect(  0,  150,    200,    40,     10);
        //gfx.fillRoundRect(  0,  200,    200,    40,     10);
        //gfx.drawEllipse(    0,  250,    200,    40);
        //gfx.fillEllipse(    0,  300,    200,    40);
        //gfx.fillText("Some FILLED text!",0,400);
    }
}
