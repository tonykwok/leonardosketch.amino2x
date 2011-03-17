package com.joshondesign.amino.core;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 3/14/11
 * Time: 5:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class PathTest {
    public static void main(String ... args) throws Exception {
        final Core runner = new Core();
        runner.setSize(600,400);
        runner.setBackground(Color.WHITE);

        Path path = Path
                .moveTo(30,30)
                .lineTo(80,50)
                //.lineTo(60,90)
                .curveTo(120,70, 80,100, 60,90)
                .closeTo()
                .build();
        Node p = new PathNode().setPath(path).setFill(Color.YELLOW).setStroke(Color.MAGENTA).setStrokeWidth(5);

        Shape c = new Circle().set(10,200,200).setFill(Color.BLUE);
        PathAnim anim = new PathAnim(c,path,10.0).setLoop(true);

        Group g = new Group().add(p).add(c);
        runner.root = g;
        runner.addAnim(anim);

        runner.start();
    }
}
