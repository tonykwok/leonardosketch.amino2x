package com.joshondesign.amino.core.simple;

import com.joshondesign.amino.core.*;
import com.joshondesign.amino.core.Window;

import java.awt.*;

/**
 * A very basic test that draws random rects to the screen. This lets us
 * test that everything is set up and working.
 */
public class RandomRects implements Core.InitCallback {
    public static void main(String ... args) {
        Core.init(new RandomRects());
    }
    public void call(Core core) throws AminoException {
        Window window = core.createResizableWindow(800, 600);
        window.setBackgroundFill(AminoColor.ORANGE);
        window.setRoot(new Node() {

            @Override
            public void draw(GFX gfx) {
                for(int i=0; i<100; i++) {
                    gfx.setPaint(AminoColor.fromRGB((float)Math.random(),(float)Math.random(), (float) Math.random()));
                    gfx.fillRect(
                        (int)(Math.random()*800),
                        (int)(Math.random()*600),
                        (int)(Math.random()*600),
                        (int)(Math.random()*400)
                    );
                }
            }
        });
        core.start();
    }
}
