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
    private AminoFont font;

    public static void main(String ... args) {
        Core.init(new FilledShapes());
    }

    public void call(Core core) throws Exception {
        Window window = core.createResizableWindow(1000, 600);
        window.setBackgroundFill(AminoColor.BLACK);

        font = core.loadFont(new File("tests/java/com/joshondesign/amino/core/resources/Junction.ttf"));

        final LinearGradient gradient = core.createVerticalLinearGradient(0,50)
                .addColor(0.0,AminoColor.BLUE)
                .addColor(0.3,AminoColor.WHITE)
                .addColor(0.7, AminoColor.RED)
                .addColor(1.0, AminoColor.WHITE);

        final LinearGradient gradient2 = core.createHorizontalLinearGradient(0,50)
                .addColor(0.0,AminoColor.BLUE)
                .addColor(0.3,AminoColor.WHITE)
                .addColor(0.7,AminoColor.RED)
                .addColor(1.0,AminoColor.WHITE);

        final PatternPaint pattern = core.loadPattern(new File("tests/java/com/joshondesign/amino/core/resources/checkerboard.png"));

        window.setRoot(new Node() {


            @Override
            public void draw(GFX gfx) {

                gfx.setPaint(AminoColor.RED);
                doDrawing(gfx);

                gfx.translate(250,0);
                gfx.setPaint(gradient);
                doDrawing(gfx);


                gfx.setPaint(gradient2);
                gfx.translate(250, 0);
                doDrawing(gfx);


                gfx.setPaint(pattern);
                gfx.translate(250,0);
                doDrawing(gfx);

                gfx.translate(-250*3,0);
            }
        });

        core.start();
    }

    private void doDrawing(GFX gfx) {
        gfx.drawLine(       0,  0,      200,    40);
        gfx.drawRect(       0,  50,     200,    40);
        gfx.fillRect(       0,  100,    200,    40);
        gfx.drawRoundRect(  0,  150,    200,    40,     10);
        gfx.fillRoundRect(  0,  200,    200,    40,     10);
        gfx.drawEllipse(    0,  250,    200,    40);
        gfx.fillEllipse(    0,  300,    200,    40);
        gfx.fillText(font,"Some FILLED text!",0,400);
        gfx.drawCircle(     50, 500, 50);
        gfx.fillCircle(    150, 500, 50);
    }
}
