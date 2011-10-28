package com.joshondesign.amino.examples.jogl;

import com.joshondesign.amino.core.*;
import com.joshondesign.amino.jogl.OrthoLayer;
import com.joshondesign.amino.jogl.PerspectiveLayer;

/**
 * Created by IntelliJ IDEA.
 * User: josh
 * Date: 10/27/11
 * Time: 8:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class JoglLayerTest implements Core.InitCallback {
    private Window window;

    public static void main(String ... args) {
        Core.init(new JoglLayerTest());
    }

    public void call(Core core) throws Exception {
        final AminoFont font = core.loadFont(this.getClass().getResource("OpenSans-Regular.ttf"))
                .withSize(22);

        window = core.createResizableWindow(1024,700);
        window.setBackgroundFill(AminoColor.BLACK);


        Transform t1 = new Transform(new Rect()
                .set(0, 0, 30, 30)
                .setFill(AminoColor.GREEN)
                ,Transform.Axis.Z)
                .setTranslateX(200)
                .setTranslateY(200);
        Transform t2 = new Transform(new Rect()
                .set(-10,-10,20,20)
                .setFill(AminoColor.RED)
                ,Transform.Axis.Y
                )
                ;

        Group g2 = new Group()
                .add(t1)
                .add(new Text()
                        .setFont(font)
                        .setText("demo mixing orthographic and perspective transforms")
                        .setFill(AminoColor.WHITE)
                        .setX(100)
                        .setY(100)
                );
        window.setRoot(new Group()
                .add(new PerspectiveLayer().setChild(t2))
                .add(new OrthoLayer().setChild(g2))
        );

        core.addAnim(new PropAnim(t1, "rotate", 0, 360, 3).setAutoReverse(false).setLoop(true));
        core.addAnim(new PropAnim(t2, "rotate", 0, 360,3).setAutoReverse(false).setLoop(true));
    }

}
