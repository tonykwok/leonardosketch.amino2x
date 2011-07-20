package com.joshondesign.amino.core;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 3/14/11
 * Time: 12:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class SimpleAnimTest {
    public static void main(String ... args) throws Exception {
        final Core runner = new Core();
        runner.setSize(600,400);
        runner.setBackground(Color.BLACK);

        Group g = new Group();
        g.add(new Rect().setWidth(100).setHeight(50).setFill(Color.GREEN))
         .add(new Rect().setWidth(50).setHeight(50).setFill(Color.YELLOW).setY(100))
        ;

        Rect r = new Rect().set(-25, -25, 50, 50);
        r.setFill(Color.WHITE);
        Transform t = new Transform(r).setTranslateX(100).setTranslateY(100);
        g.add(t);
        runner.addAnim(new PropAnim(t,"rotate",0,90,1).setLoop(true).setAutoReverse(false));
        runner.addAnim(new PropAnim(t,"translateX",100,500,4).setLoop(true).setAutoReverse(true));
        runner.addAnim(new PropAnim(t,"translateY",100,150,0.5).setLoop(true).setAutoReverse(true));

        runner.root = g;
        runner.start();
    }
}
