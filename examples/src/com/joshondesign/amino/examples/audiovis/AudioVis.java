package com.joshondesign.amino.examples.audiovis;

import com.joshondesign.amino.core.*;
import com.joshondesign.amino.jogl.JoglGFX;

/**
 * Created by IntelliJ IDEA.
 * User: josh
 * Date: 10/27/11
 * Time: 4:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class AudioVis implements Core.InitCallback {

    private Window window;

    public static void main(String ... args) {
        Core.init(new AudioVis());
    }

    public void call(Core core) throws Exception {
        window = core.createResizableWindow(1024,800);
        window.setBackgroundFill(AminoColor.BLACK);
        window.setRoot(new Group()
                //.add(new Rect().set(100,100,500,300).setFill(AminoColor.CYAN))
                .add(new AudioVisualizerNode())
        );

    }

    private static class AudioVisualizerNode extends Node {
        private float[] latestFreq = new float[16*2];
        int count = 0;

        private AudioVisualizerNode() {
            for(int i=0; i<latestFreq.length/2; i++) {
                latestFreq[i] = (float) Math.random();
                latestFreq[i+16] = latestFreq[i];
            }
        }

        @Override
        public void draw(GFX gfx) {
            JoglGFX gfx2 = (JoglGFX) gfx;
            float degree = count % 360;
            gfx2.getGL().glPushMatrix();
            gfx2.getGL().glTranslatef(500,400,0);
            gfx2.getGL().glRotatef(degree,0,0,1);
            gfx2.getGL().glTranslatef(-500,-400,0);

            gfx.setPaint(AminoColor.RED);
            //gfx.fillRect(150f,200,600,300);
            float[] freq = getLatestFreq();

            for(int i=0; i<freq.length/2; i++) {
                float h = freq[i]*300;
                gfx.fillRect(i*50+50f,350-h,40,h);
                gfx.fillRect(i*50+50f,370,40,h);
            }
            gfx2.getGL().glPopMatrix();
            count++;
        }

        public float[] getLatestFreq() {
            for(int i=0; i<latestFreq.length/2; i++) {
                latestFreq[i] = latestFreq[i] + (float)(Math.random()-0.5)/5f;
                if(latestFreq[i] < 0) latestFreq[i] = 1f;
                if(latestFreq[i] > 1f) latestFreq[i] = 0f;
                latestFreq[i+16] = latestFreq[i];
            }
            return latestFreq;
        }
    }
}
