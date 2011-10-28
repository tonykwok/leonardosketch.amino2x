package com.joshondesign.amino.jogl.test;

import com.joshondesign.amino.core.*;
import com.joshondesign.amino.jogl.FrameBufferObject;
import com.joshondesign.amino.jogl.JoglGFX;
import com.joshondesign.amino.jogl.QRect;

import javax.media.opengl.GL2;

/**
 * Created by IntelliJ IDEA.
 * User: josh
 * Date: 10/28/11
 * Time: 11:50 AM
 * To change this template use File | Settings | File Templates.
 */
public class JoglBufferStretchTest implements Core.InitCallback {
    private static final int WIDTH = 1100;
    private static final int HEIGHT = 950;

    private Window window;
    private FrameBufferObject buffer1;
    private FrameBufferObject buffer2;
    private Object defaultBuffer = new String("DEFAULT_BUFFER");
    private static final boolean BUFFER_DEBUG = false;

    public static void main(String ... args) {
        Core.init(new JoglBufferStretchTest());
    }

    public void call(Core core) throws Exception {
        window = core.createResizableWindow(WIDTH,HEIGHT+20);
        window.setBackgroundFill(AminoColor.BLACK);
        window.setRoot(new Node() {
            @Override
            public void draw(GFX gfx) {
                JoglGFX g = (JoglGFX) gfx;
                GL2 gl = g.getGL();
                JoglBufferStretchTest.this.draw(g,gl);
            }
        });

    }

    private void draw(JoglGFX g, GL2 gl) {
        g.setPaint(AminoColor.RED);
        g.drawRect(100,100,50,50);

        if(buffer1 == null) {
            buffer1 = g.createBuffer(WIDTH,HEIGHT);

        }
        if(buffer2 == null) {
            buffer2 = g.createBuffer(WIDTH,HEIGHT);
        }


        // copy buffer2 to buffer1 w/ stretch
        gl.glPushMatrix();
        double cx = WIDTH/2;
        double cy = HEIGHT/2;
        gl.glTranslated(cx,cy,0);
        gl.glScaled(1.01, 1.01, 1);
        gl.glRotated(0.3,0,0,1);
        gl.glTranslated(-cx, -cy, 0);
        g.copyBuffer(gl,null,buffer2,null,buffer1);
        gl.glPopMatrix();

        // draw shapes to buffer1
        double x = Math.random()*WIDTH-100;
        double y = Math.random()*HEIGHT-100;
        g.fillRect(QRect.build((int)x, (int)y, 100, 100)
                , AminoColor.fromRGB((float)Math.random(),0.1f,0.1f)
                , buffer1, null);

        // copy buffer1 to screen
        g.copyBuffer(gl, null, buffer1, null, null);

        // copy buffer1 to buffer2
        g.copyBuffer(gl,null,buffer1,null,buffer2);
    }



    private static void p(String s) {
        System.out.println(s);
    }

}
