package com.joshondesign.amino.examples.audiovis;

import com.joshondesign.amino.core.*;
import com.joshondesign.amino.jogl.JoglGFX;

import java.io.File;

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
        AudioProcessor ap = new AudioProcessor();
        window = core.createResizableWindow(1024,800);
        window.setBackgroundFill(AminoColor.BLACK);
        window.setRoot(new Group()
                //.add(new Rect().set(100,100,500,300).setFill(AminoColor.CYAN))
                .add(new AudioVisualizerNode(ap))
        );

        File file = new File("../forever.mp3");
        PlayThread thread = new PlayThread(file.toURL().toString());
        //"http://projects.joshy.org/MaiTai/b1/Samples/Forever_Young.mp3");
        thread.start();
    }

    private static class AudioProcessor implements FrequenciesDataProcessor {
        private FrequenciesDispatcher dispatcher;
        private float[] leftData;
        private float[] rightData;

        private AudioProcessor() {
            dispatcher = FrequenciesDispatcher.getInstance();
            dispatcher.addProcessor(this);
            leftData = new float[dispatcher.getFrequenciesBandCount()];
            rightData = new float[dispatcher.getFrequenciesBandCount()];
        }

        public void process(float[] leftData, float[] rightData) {
            this.leftData = leftData;
            this.rightData = rightData;
        }

        public float getData(int index) {
            return leftData[index];
        }
    }

    private static class AudioVisualizerNode extends Node {
        private float[] latestFreq = new float[16*2];
        float count = 0;
        private AudioProcessor ap;

        private AudioVisualizerNode(AudioProcessor ap) {
            this.ap = ap;
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
            gfx2.getGL().glTranslatef(500, 400, 0);
            gfx2.getGL().glRotatef(degree, 0, 0, 1);
            gfx2.getGL().glTranslatef(-500, -400, 0);

            gfx.setPaint(AminoColor.RED);
            //gfx.fillRect(150f,200,600,300);
            float[] freq = ap.leftData;

            for(int i=0; i<freq.length; i++) {
                float h = freq[i]*300;
                gfx.fillRect(i*50+50f,350-h,40,h);
                gfx.fillRect(i*50+50f,370,40,h);
            }
            gfx2.getGL().glPopMatrix();
            count += 0.2;
        }

        public float[] getLatestFreq() {
            for(int i=0; i<latestFreq.length/2; i++) {
                latestFreq[i] = latestFreq[i] + (float)(Math.random()-0.5)/5f;
                if(latestFreq[i] < 0) latestFreq[i] = 1f;
                if(latestFreq[i] > 1f) latestFreq[i] = 0f;
                latestFreq[i+16] = latestFreq[i];
            }
            for(int i=0; i<ap.leftData.length; i++) {
                //u.p("data = " + ap.leftData[i]);
            }
            return ap.leftData;
            //return latestFreq;
        }
    }
}
