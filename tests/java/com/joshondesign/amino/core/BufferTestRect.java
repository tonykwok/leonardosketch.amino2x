package com.joshondesign.amino.core;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 3/24/11
 * Time: 5:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class BufferTestRect {
    public static void main(String ... args) throws Exception {
        final Core runner = new Core();

        runner.setSize(600,400);
        runner.setBackground(Color.WHITE);

        //Node sn = new SaturationNode(new Circle().set(100,100,25).setFill(Color.YELLOW));
        //Shape r2 = new Rect().set(30,40,50,60).setFill(Color.RED);
        //BufferNode bn = new BufferNode(new Rect().set(30, 40, 50, 60).setFill(Color.RED));
        Node sn = new SaturationNode(
                //new Circle().set(100,100,25).setFill("yellow")
                new ImageView(BufferTestRect.class.getResource("venus_large.jpg"))
            )
            .setSaturation(0.5)
            ;


        Transform t = new Transform(sn);

        runner.addAnim(new PropAnim(t,"translateX",0,90,10).setLoop(true).setAutoReverse(true));
        runner.addAnim(new PropAnim(sn,"saturation",0.0,1.0,1).setLoop(true).setAutoReverse(true));
        runner.setRoot(t);
        runner.setBackground(Color.BLUE);
        runner.setFPS(30);
        runner.start();
    }
}