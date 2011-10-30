package com.joshondesign.amino.examples.jogl;

import com.joshondesign.amino.core.*;
import com.joshondesign.amino.examples.audiovis.AudioProcessor;
import com.joshondesign.amino.examples.audiovis.PlayThread;
import com.joshondesign.amino.jogl.FrameBufferObject;
import com.joshondesign.amino.jogl.JoglGFX;

import javax.media.opengl.GL2;
import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: josh
 * Date: 10/28/11
 * Time: 11:50 AM
 * To change this template use File | Settings | File Templates.
 */
public class JoglBufferStretchTest implements Core.InitCallback {
    private static int WIDTH = 1100;
    private static int HEIGHT = 950;

    private Window window;
    private FrameBufferObject buffer1;
    private FrameBufferObject buffer2;
    private AudioProcessor ap;

    public static void main(String ... args) {
        Core.init(new JoglBufferStretchTest());
    }

    public void call(Core core) throws Exception {
        ap = new AudioProcessor();
        window = core.createFullscreenWindow();
        WIDTH = window.getWidth();
        HEIGHT = window.getHeight();
        window.setBackgroundFill(AminoColor.BLACK);
        window.setRoot(new Node() {
            @Override
            public void draw(GFX gfx) {
                JoglGFX g = (JoglGFX) gfx;
                GL2 gl = g.getGL();
                JoglBufferStretchTest.this.draw(g, gl);
            }
        });

        File file = new File("../forever.mp3");
        final PlayThread thread = new PlayThread(file.toURL().toString());
        core.listen(Core.Events.MOUSE_PRESS,null, new Callback() {
            public void call(Object o) {
                thread.doDie();
                window.close();
            }
        });
        thread.start();
    }

    int counter = 0;
    private void draw(JoglGFX g, GL2 gl) {
        g.setPaint(AminoColor.RED);
        g.drawRect(100,100,50,50);

        if(buffer1 == null) {
            buffer1 = g.createBuffer(WIDTH, HEIGHT);

        }
        if(buffer2 == null) {
            buffer2 = g.createBuffer(WIDTH, HEIGHT);
        }


        // copy buffer2 to buffer1 w/ stretch
        gl.glPushMatrix();
        double cx = WIDTH/2;
        double cy = HEIGHT/2;
        gl.glTranslated(cx,cy,0);
        gl.glScaled(1.005, 1.005, 1);
        //gl.glRotated(0.2,0,0,1);
        gl.glTranslated(-cx, -cy, 0);
        g.copyBuffer(null, buffer2, null, buffer1);
        gl.glPopMatrix();

        // draw shapes to buffer1
        double x = Math.random()*WIDTH-100;
        double y = Math.random()*HEIGHT-100;
        g.setTargetBuffer(buffer1);
        /*
        g.fillRect2(QRect.build((int) x, (int) y, 100, 100)
                , AminoColor.fromRGB((float) Math.random(), 0.1f, 0.1f)
                , null);
                */


        GFX gfx = g;
        gfx.setPaint(AminoColor.fromHSV(counter%360,1,1));
        //gfx.fillRect(150f,200,600,300);
        float[] freq = ap.getLeftData();

        for(int i=0; i<freq.length-1; i++) {
            float h = freq[i]*200;
            float h2 = freq[i+1]*200;
            //gfx.fillRect(i*50+50f,350-h,40,h);
            //gfx.fillRect(i*50+50f,370,40,h);
            int n1 = WIDTH/2-freq.length/2*50+i*50;
            int n2 = WIDTH/2-freq.length/2*50+(i+1)*50;
            int yoff = (int) (Math.sin(Math.toRadians(counter%360)*3)*100);
            gfx.drawLine(n1,(int)(HEIGHT/2-h)-yoff, n2,(int)(HEIGHT/2-h2)-yoff);
            gfx.drawLine(n1,(int)(HEIGHT/2+h)+yoff, n2,(int)(HEIGHT/2+h2)+yoff);
        }
        g.setTargetBuffer(null);


        // copy buffer1 to screen
        g.copyBuffer(null, buffer1, null, null);

        // copy buffer1 to buffer2
        g.copyBuffer(null, buffer1, null, buffer2);

        counter++;
    }



    private static void p(String s) {
        System.out.println(s);
    }

}
