package com.joshondesign.amino.jogl.test;

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
        window.setRoot(new Group()
                .add(new PerspectiveLayer().setChild(t2))
                .add(new OrthoLayer().setChild(t1))
        );
        core.addAnim(new PropAnim(t1, "rotate", 0, 360, 3).setAutoReverse(false).setLoop(true));
        core.addAnim(new PropAnim(t2,"rotate",0,360,3).setAutoReverse(false).setLoop(true));
    }

}
