package com.joshondesign.amino.core;


/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 3/14/11
 * Time: 5:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class PathTest implements Core.InitCallback {
    public static void main(String ... args) throws Exception {
        Core.init(new PathTest());

    }

    public void call(Core core) throws Exception {
        com.joshondesign.amino.core.Window window = core.createResizableWindow(800, 600);
        Path path = Path
                .moveTo(30,30)
                .lineTo(80,50)
                //.lineTo(60,90)
                .curveTo(120,70, 80,100, 60,90)
                .closeTo()
                .build();
        Node p = new PathNode().setPath(path).setFill(AminoColor.YELLOW).setStroke(AminoColor.MAGENTA).setStrokeWidth(5);

        Shape c = new Circle().set(200,200,10).setFill(AminoColor.BLUE);
        PathAnim anim = new PathAnim(c,path,10.0).setLoop(true);

        Group g = new Group().add(p).add(c);
        window.setRoot(g);
        //runner.addAnim(anim);
    }
}
