package com.joshondesign.amino.core;

import com.joshondesign.amino.sdl.SDLCore;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
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
        Core.setImpl(SDLCore.getImpl());
        Core.init(new FillTest());
    }

    public void call(Core core) throws AminoException {
        final Core r = new Core();
        r.setSize(600,400);
        r.setBackground(Color.WHITE);


        //fills are always in the coordinate system of the child node, excluding internal
        //offsets.  So a rect at 50,50 won't be the same as a rect at 0,0 in a transform of 50,50

        //linear gradient
        Paint grad0 = new LinearGradientPaint(0,0,100,0,
                new float[]{0,1},
                new Color[]{Color.RED,Color.BLUE}
        );

        //radial gradient
        Paint grad1 = new RadialGradientPaint(0,0,100,
                new float[]{0,0.5f,1},
                new Color[]{Color.WHITE,Color.BLACK,Color.WHITE}
                );

        //texture fill
        Shape textRect = new Rect().set(0, 0, 100, 40).setFill(Color.YELLOW);

        BufferedImage img = null;
        try {
            img = ImageIO.read(FillTest.class.getResource("checkerboard.png"));
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        textRect.setFill(new TexturePaint(img,new Rectangle(0,0,img.getWidth(),img.getHeight())));

        r.setRoot(new Group()
            //radial gradient
            .add(new Rect().set(0, 0, 100, 40).setFill(grad1))
            .add(new Rect().set(0, 50, 100, 40).setFill(grad1))
            .add(new Transform(new Rect().set(0, 0, 100, 40).setFill(grad1)).setTranslateY(100))

            .add(new Transform(new Rect().set(0, 0, 100, 40).setFill(grad0)).setTranslateX(150))
            .add(new Transform(textRect).setTranslateX(150).setTranslateY(50))

            );

        r.setBackground(Color.GREEN);
        r.start();
    }
}
