package com.joshondesign.amino.core;


import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 4/6/11
 * Time: 3:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class FillTest implements Core.InitCallback {
    public static void main(String ... args) throws Exception {
        //Core.setImpl(SDLCore.getImpl());
        Core.init(new FillTest());
    }

    public void call(Core core) throws AminoException, IOException {
        Window window = core.createResizableWindow(600, 400);
        //r.setSize(600,400);
        //r.setBackground(Color.WHITE);


        //fills are always in the coordinate system of the child node, excluding internal
        //offsets.  So a rect at 50,50 won't be the same as a rect at 0,0 in a transform of 50,50

        //linear gradient
        AminoPaint grad0 = core.createHorizontalLinearGradient(0,100)
                .addColor(0,AminoColor.RED)
                .addColor(1,AminoColor.BLUE);

        //radial gradient
        AminoPaint grad1 = core.createVerticalLinearGradient(0,100)
                .addColor(0,AminoColor.WHITE)
                .addColor(0.5,AminoColor.BLACK)
                .addColor(1,AminoColor.WHITE);

        //texture fill
        Shape textRect = new Rect().set(0, 0, 100, 40).setFill(AminoColor.YELLOW);

        textRect.setFill(core.loadPattern(FillTest.class.getResource("checkerboard.png")));

        window.setRoot(new Group()
            //radial gradient
            .add(new Rect().set(0, 0, 100, 40).setFill(grad1))
            .add(new Rect().set(0, 50, 100, 40).setFill(grad1))
            .add(new Transform(new Rect().set(0, 0, 100, 40).setFill(grad1)).setTranslateY(100))

            .add(new Transform(new Rect().set(0, 0, 100, 40).setFill(grad0)).setTranslateX(150))
            .add(new Transform(textRect).setTranslateX(150).setTranslateY(50))

            );

        window.setBackgroundFill(AminoColor.GREEN);
    }
}
