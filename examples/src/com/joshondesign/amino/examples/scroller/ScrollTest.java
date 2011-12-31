package com.joshondesign.amino.examples.scroller;

import com.joshondesign.amino.core.*;

import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: josh
 * Date: 9/27/11
 * Time: 4:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class ScrollTest implements Core.InitCallback {
    private AminoImage tile;
    private int counter;
    private boolean leftDown = false;
    private boolean rightDown = false;

    public static void main(String ... args) {
        Core.init(new ScrollTest());
    }

    public void call(Core core) throws Exception {
        Window window = core.createResizableWindow(1024, 768);
        URL url = ScrollTest.class.getResource("Block-Normal.png");
        tile = core.loadImage(url);
        window.setRoot(new Node() {
            @Override
            public void draw(GFX gfx) {
                doit(gfx);
            }
        });
        core.listen(Core.Events.KEY_PRESSED, null, new Callback<KEvent>() {
            public void call(KEvent e) {
                switch(e.key) {
                    case LEFT: leftDown = true; break;
                    case RIGHT: rightDown = true; break;
                }
            }
        });
        core.listen(Core.Events.KEY_RELEASED, null, new Callback<KEvent>() {
            public void call(KEvent e) {
                switch(e.key) {
                    case LEFT: leftDown = false; break;
                    case RIGHT: rightDown = false; break;
                }
            }
        });
    }

    private void doit(GFX gfx) {
        double inc = 15.5;
        //counter += 3;
        if(leftDown) {
            counter += inc;
        }
        if(rightDown) {
            counter -= inc;
        }
        int w = 1024;
        int sw = w/70+1;
        for(int j=0; j<13; j++) {
            for(int i=0; i<sw; i++) {
                gfx.drawImage(tile,(i*70+counter)%(w+70)-70,j*60);
            }
        }
    }
}
