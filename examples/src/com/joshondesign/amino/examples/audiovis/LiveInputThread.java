/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.joshondesign.amino.examples.audiovis;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Decoder;
import javazoom.jl.decoder.Header;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.JavaSoundAudioDevice;

import javax.sound.sampled.*;
import java.io.InputStream;
import java.net.URL;

/**
 *
 * @author josh
 */
public class LiveInputThread extends Thread{

    private boolean analyzerInitialized = false;

    private boolean die = false;
    private boolean finished = false;
    private boolean pause = false;
    private AudioDevice audioDevice;
    private SourceDataLine wSdl;
    private KJDigitalSignalProcessingAudioDataConsumer dsp;
    private Object readWriteLock = new Object();

    public LiveInputThread() {
    }
    public void doit() {
        start();
    }

    public boolean loop = false;
    public void run() {
        startAudioLoop();
        System.out.println("done with the loop");
        while(loop) {
            System.out.println("looping again");
            startAudioLoop();
        }
    }

    public void pausePlay() {
        synchronized (readWriteLock) {
            wSdl.stop();
            pause = true;
        }
    }

    private void resumePlay() {
        synchronized (readWriteLock) {
            wSdl.start();
            pause = false;
        }
    }

    public void doDie() {
        this.die = true;
        this.loop = false;
    }

    private void startAudioLoop() {
        try {
            Decoder decoder = new Decoder();
            //URL songUrl = model.getSongUrl(songIndex);
            //File file = new File("/Users/josh/Desktop/MaiTai Samples/Forever_Young.mp3");
            //System.out.println("file exists = " + file.exists());
            URL songUrl = new URL("http://projects.joshy.org/MaiTai/b1/Samples/Forever_Young.mp3");
            System.out.println("attempting to play " + songUrl);
            InputStream is = songUrl.openStream();
            Bitstream bitstream = new Bitstream(is);
            audioDevice = new JavaSoundAudioDevice();
            audioDevice.open(decoder);

            wSdl = null;
            dsp = new KJDigitalSignalProcessingAudioDataConsumer(2048, 30);
            dsp.add(FrequenciesDispatcher.getInstance());

            // Gfx: the analyzer needs init
            analyzerInitialized = false;

            while (!die && !finished) {
                //check to see if I am suppose to pause this thread
                while (pause) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                    }
                }

                try {
                    Header h = bitstream.readFrame();
                    if (h == null) {
                        finished = true;
                        continue;
                    }

                    // Gfx: init analyzer
                    if (!analyzerInitialized) {
                        KJSampleBuffer sampleBuffer = new KJSampleBuffer(h.frequency(),
                                                                         h.mode() == Header.SINGLE_CHANNEL ? 1 : 2 );
                        decoder.setOutputBuffer(sampleBuffer);
                        KJSampleBuffer realBuffer = (KJSampleBuffer) decoder.decodeFrame(h, bitstream);
                        AudioFormat wTrackFormat = new AudioFormat(decoder.getOutputFrequency(),
                                                                   16,
                                                                   decoder.getOutputChannels(),
                                                                   true,
                                                                   false);
                        DataLine.Info wLi = new DataLine.Info(SourceDataLine.class, wTrackFormat);
                        Mixer.Info[] wMixerInfo = AudioSystem.getMixerInfo();
                        Mixer wMixer = AudioSystem.getMixer(wMixerInfo[0]);
                        wSdl = (SourceDataLine) wMixer.getLine(wLi);
                        wSdl.open(wTrackFormat, 128 * 1024);
                        wSdl.start();
                        dsp.start(wSdl);
                        
                        wSdl.write(realBuffer.getBuffer(), 0, realBuffer.getBufferLength());
                        dsp.writeAudioData(realBuffer.getBuffer());

                        bitstream.closeFrame();

                        h = bitstream.readFrame();
                        if (h == null) {
                            finished = true;
                            continue;
                        }

                        analyzerInitialized = true;
                    }

                    // Gfx: process the sound frame
                    synchronized (readWriteLock) {
                        KJSampleBuffer output = (KJSampleBuffer) decoder.decodeFrame(h, bitstream);
                        wSdl.write(output.getBuffer(), 0, output.getBufferLength());
                        dsp.writeAudioData(output.getBuffer());
                    }

                    bitstream.closeFrame();
                }
                catch (RuntimeException ex) {
                    System.out.println(ex);
                }
            }

            audioDevice.flush();
            audioDevice.close();
            wSdl.stop();
            wSdl.close();
            dsp.stop();
            analyzerInitialized = false;

            try {
                bitstream.close();
            } catch (BitstreamException ex) {
                ex.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (finished) {
            } else if (die) {
            }
        }
    }
}
