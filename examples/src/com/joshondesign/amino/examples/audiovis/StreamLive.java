/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javazoom.audio;

import javax.sound.sampled.AudioFileFormat.Type;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

/**
 *
 * @author josh
 */
public class StreamLive {
    public static void main(String ... args) throws LineUnavailableException {
        Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
        for(Mixer.Info info : mixerInfos) {
            p("info = " + info + " - " + info.getDescription());
            Mixer mx = AudioSystem.getMixer(info);
            for(Line.Info li : mx.getSourceLineInfo()) {
                p(" sources = " + li);
            }
            for(Line.Info ol : mx.getTargetLineInfo()) {
                p(" targs = " + ol);
            }
        }
        for(Type type : AudioSystem.getAudioFileTypes()) {
            p("type = " + type);
        }
        p("using the microphone");

        // specify a hard coded format:
        float fFrameRate = 44100.0F;
        AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, fFrameRate, 16, 2, 4, fFrameRate, false);
        int bufferSize = 1024*5;//40960;  I'm not sure how the buffer size affects things

        //guess for the mic, and get the mixer for it
        Mixer.Info out_info = mixerInfos[0];
        Mixer.Info in_info =  mixerInfos[1];
        Mixer in_mixer = AudioSystem.getMixer(in_info);
        Mixer out_mixer = AudioSystem.getMixer(out_info);

        // get the line infos
        Info targetInfo = new DataLine.Info(TargetDataLine.class, audioFormat, bufferSize);
        Info sourceInfo = new DataLine.Info(SourceDataLine.class, audioFormat, bufferSize);
        // open the lines
        TargetDataLine targetLine = (TargetDataLine) in_mixer.getLine(targetInfo);
        SourceDataLine sourceLine = (SourceDataLine) out_mixer.getLine(sourceInfo);
        targetLine.open(audioFormat,bufferSize);
        sourceLine.open(audioFormat,bufferSize);

        // start the lines
        targetLine.start();
        sourceLine.start();

        // copy from input to output
        byte[] buffer = new byte[bufferSize];
        int sz = buffer.length;
        long startTime = System.currentTimeMillis();
        while(true) {
            int n = targetLine.read(buffer, 0, sz);
            sourceLine.write(buffer, 0, n);
            // quit after 5 sec
            if(System.currentTimeMillis() - startTime > 5000) {
               p("breaking");
               break;
            }
        }

        p("shutting down");
        //targetLine.stop();
        sourceLine.stop();
        p("here");
        //sourceLine.drain();
        p("here");
        sourceLine.close();
        p("here");
        System.exit(0);
        p("here");

        //Mixer wMixer = AudioSystem.getMixer(wMixerInfo[0]);
        /*
        wSdl = (SourceDataLine) wMixer.getLine(wLi);
        wSdl.open(wTrackFormat, 128 * 1024);
        wSdl.start();
        wSdl.write(realBuffer.getBuffer(), 0, realBuffer.getBufferLength());
        wSdl.write(output.getBuffer(), 0, output.getBufferLength());
        audioDevice.flush();
        audioDevice.close();
        wSdl.stop();
        wSdl.close();
            */
    }

    private static void p(String string) {
        System.err.println(string);
    }
}
