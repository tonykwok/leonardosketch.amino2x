/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.joshondesign.amino.examples.audiovis;

import javax.sound.sampled.*;
import javax.sound.sampled.Mixer.Info;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author josh
 */
public class LiveAudioHelper {
    private static Info currentInput;
    private static SourceDataLine sourceLine;
    private static KJDigitalSignalProcessingAudioDataConsumer dsp;
    private static int bufferSize = 128 * 1024;
    private static TargetDataLine targetLine;
    private static BooleanControl muteControl;

    public static Mixer.Info[] findInputs() {
        Mixer.Info[] infos = AudioSystem.getMixerInfo();
        List<Mixer.Info> list = new ArrayList<Info>();
        for(Mixer.Info info : infos) {
            Mixer mx = AudioSystem.getMixer(info);
            Line.Info[] targets = mx.getTargetLineInfo();
            if(targets != null && targets.length > 0) {
                list.add(info);
            }
        }
        return list.toArray(new Mixer.Info[0]);
    }
    private static Info[] findOutputs() {
        Mixer.Info[] infos = AudioSystem.getMixerInfo();
        List<Mixer.Info> list = new ArrayList<Info>();
        for(Mixer.Info info : infos) {
            Mixer mx = AudioSystem.getMixer(info);
            Line.Info[] sources = mx.getSourceLineInfo();
            if(sources != null && sources.length > 0) {
                list.add(info);
            }
        }
        return list.toArray(new Mixer.Info[0]);
    }

    public static void setInput(Mixer.Info inputx) {
        currentInput = inputx;
    }

    public static void setMute(boolean mute) {
        if(muteControl != null) {
            muteControl.setValue(mute);
        }
    }
    public static void startProcessing() throws LineUnavailableException {
        p("available outputs = ");
        Mixer.Info[] outputs = findOutputs();
        for(Mixer.Info info : outputs) {
            p("   name = " + info.getName());
        }
        
        float fFrameRate = 44100.0F;
        AudioFormat audioFormat = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                fFrameRate,//sample rate
                16,//sample size
                2,//channels
                4,//frame size
                fFrameRate,//frame rate
                false//big endian
                );
        //int bufferSize = 1024*5;//40960;  I'm not sure how the buffer size affects things
        //use the first input and outputs
        Mixer in_mixer = AudioSystem.getMixer(currentInput);
        Mixer out_mixer = AudioSystem.getMixer(outputs[0]);

        // get the line infos
        DataLine.Info targetInfo = new DataLine.Info(TargetDataLine.class, audioFormat, bufferSize);
        DataLine.Info sourceInfo = new DataLine.Info(SourceDataLine.class, audioFormat, bufferSize);
        // open the lines
        targetLine = (TargetDataLine) in_mixer.getLine(targetInfo);
        sourceLine = (SourceDataLine) out_mixer.getLine(sourceInfo);
        targetLine.open(audioFormat,bufferSize);
        sourceLine.open(audioFormat,bufferSize);

        // start the lines
        targetLine.start();
        sourceLine.start();

        //get the mute control
        muteControl = (BooleanControl) sourceLine.getControl(BooleanControl.Type.MUTE);
        p("mute control = " + muteControl);
        //muteControl.setValue(true);

        //set up the dsp
        dsp = new KJDigitalSignalProcessingAudioDataConsumer(bufferSize, 30);
        dsp.add(FrequenciesDispatcher.getInstance());
        /*
        FrequenciesDispatcher.getInstance().addProcessor(new FrequenciesDataProcessor() {
            public void process(float[] leftData, float[] rightData) {
                //System.out.println("got left and right");
                for(float f : leftData) {
                    //System.out.print(f+" ");

                    if(f < 0.1) System.out.print("0");
                    else if (f < 0.2) System.out.print("1");
                    else if (f < 0.3) System.out.print("2");
                    else if (f < 0.4) System.out.print("3");
                    else if (f < 0.5) System.out.print("4");
                    else System.out.print("X");
                }
                System.out.println();
            }
        });*/
        dsp.start(sourceLine);

        new Thread(new Runnable() {
            public void run() {
                shouldRun = true;
                loop();
            }
        }).start();
    }

    private static boolean shouldRun = false;
    private static void loop() {
        // copy from input to output for 5 seconds
        byte[] buffer = new byte[bufferSize];
        int sz = buffer.length;
        long startTime = System.currentTimeMillis();
        while(shouldRun) {
            int n = targetLine.read(buffer, 0, sz);
            sourceLine.write(buffer, 0, n);
            dsp.writeAudioData(buffer);
        }
        p("shutting down");
        //targetLine.stop();
        sourceLine.stop();
        p("here");
        //sourceLine.drain();
        p("here");
        sourceLine.close();
        dsp.stop();
    }
    
    public static void stopProcessing() {
        shouldRun = false;
    }

    private static void p(String string) {
        System.out.println(string);
    }

}
