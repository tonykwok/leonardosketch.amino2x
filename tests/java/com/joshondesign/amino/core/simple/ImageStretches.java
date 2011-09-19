package com.joshondesign.amino.core.simple;

import com.joshondesign.amino.core.*;
import com.joshondesign.amino.core.Window;

import java.awt.*;
import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: josh
 * Date: 9/19/11
 * Time: 1:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageStretches implements Core.InitCallback {
    public static void main(String ... args) {
        Core.init(new ImageStretches());
    }

    public void call(Core core) throws Exception {
        Window window = core.createResizableWindow(800, 600);
        window.setBackgroundFill(Color.WHITE);
        final AminoImage image = core.loadImage(new File("tests/java/com/joshondesign/amino/core/resources/andromeda.jpg"));

        window.setRoot(new Node() {
            @Override
            public void draw(Graphics2D gfx) {
            }

            @Override
            public void draw(GFX gfx) {
                //draw image
                gfx.drawImage(image, 0, 0);
                //draw image translated
                gfx.drawImage(image,500,10);
                //draw internal slice of image
                gfx.drawImage(image,
                        100,100,50,100,
                        50,300,200,200);

                //draw image as 9slice
                gfx.drawImage9Slice(image,
                        20, 20, 20, 20,
                        300, 300, 400, 150);



            }
        });
    }
}
