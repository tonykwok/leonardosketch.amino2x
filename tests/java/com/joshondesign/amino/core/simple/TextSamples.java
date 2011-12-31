package com.joshondesign.amino.core.simple;

import com.joshondesign.amino.core.*;
import com.joshondesign.amino.core.Window;

import java.awt.*;
import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: josh
 * Date: 9/19/11
 * Time: 1:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class TextSamples implements Core.InitCallback {
    public static void main(String ... args) {
        Core.init(new TextSamples());
    }

    public void call(Core core) throws Exception {
        Window window = core.createResizableWindow(800, 600);
        window.setBackgroundFill(AminoColor.BLACK);
        final AminoFont font = core.loadFont(new File("tests/java/com/joshondesign/amino/core/resources/Junction.ttf"));
        final AminoFont font2  = font.withSize(40);
        window.setRoot(new Node() {

            @Override
            public void draw(GFX gfx) {
                gfx.setPaint(AminoColor.WHITE);
                gfx.fillText(font, "webOS + Java", 50, 200);
                gfx.setPaint(AminoColor.BLUE);
                gfx.fillText(font2, "webOS + Java", 50, 300);
            }
        });
    }

}
