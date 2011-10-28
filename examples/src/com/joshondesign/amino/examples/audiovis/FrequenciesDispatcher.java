package com.joshondesign.amino.examples.audiovis;

import java.util.LinkedList;
import java.util.List;


public class FrequenciesDispatcher implements KJDigitalSignalProcessor {
    private static FrequenciesDispatcher instance = null;

    public static final int DEFAULT_SPECTRUM_ANALYSER_FFT_SAMPLE_SIZE = 512;
    public static final int DEFAULT_SPECTRUM_ANALYSER_BAND_COUNT = 16;
    public static final float DEFAULT_SPECTRUM_ANALYSER_DECAY = 0.03f;

    private KJFFT fft;
    private float[] old_FFT;

    private int saFFTSampleSize;
    private int saBands;
    private float saMultiplier;
    private float saDecay = DEFAULT_SPECTRUM_ANALYSER_DECAY;
    
    private List<FrequenciesDataProcessor> processors;

    private FrequenciesDispatcher() {
        processors = new LinkedList<FrequenciesDataProcessor>();
        initialize();
    }
    
    private void computeSAMultiplier() {
        saMultiplier = (saFFTSampleSize / 2) / saBands;
    }
    
    public int getFrequenciesBandCount() {
        return saBands;
    }

    public float getFrequenciesDecay() {
        return saDecay;
    }

    private void initialize() {
        setSpectrumAnalyserBandCount(DEFAULT_SPECTRUM_ANALYSER_BAND_COUNT);
        setSpectrumAnalyserFFTSampleSize(DEFAULT_SPECTRUM_ANALYSER_FFT_SAMPLE_SIZE);
    }

    public synchronized void process(float[] pLeft, float[] pRight,
                                     float pFrameRateRatioHint) {
        pLeft = process(pLeft, pFrameRateRatioHint);
        pRight = process(pRight, pFrameRateRatioHint);
        dispatch(pLeft, pRight);
    }
    
    public void addProcessor(FrequenciesDataProcessor processor) {
        if (processor == null) {
            return;
        }
        processors.add(processor);
    }
    
    public void removeProcessor(FrequenciesDataProcessor processor) {
        if (processor == null) {
            return;
        }
        processors.remove(processor);
    }
    
    private void dispatch(float[] leftData, float[] rightData) {
        for (FrequenciesDataProcessor processor: processors) {
            processor.process(leftData, rightData);
        }
    }
    
    private float[] process(float[] pSample, float pFrrh) {
        float[] wFFT = null;
        if (pSample != null) {
            wFFT = fft.calculate(pSample);
        }

        float wSadfrr = (saDecay * pFrrh);
        float[] data = new float[saBands];

        for (int a = 0, bd = 0; bd < saBands; a += saMultiplier, bd++) {
            if (pSample == null) {
                data[bd] = 0.0f;
            } else {
                float wFs = 0;
                for (int b = 0; b < saMultiplier; b++) {
                    wFs += wFFT[a + b];
                }
    
                wFs = (wFs * (float) Math.log(bd + 2));
                if (wFs > 1.0f) {
                    wFs = 1.0f;
                }
    
                if (wFs >= (old_FFT[a] - wSadfrr)) {
                    old_FFT[a] = wFs;
                } else {
                    old_FFT[a] -= wSadfrr;
                    if (old_FFT[a] < 0) {
                        old_FFT[a] = 0;
                    }
    
                    wFs = old_FFT[a];
                }

                data[bd] = wFs;
            }
        }
        return data;
    }

    public synchronized void setSpectrumAnalyserBandCount(int pCount) {
        saBands = pCount;
        computeSAMultiplier();
    }

    public synchronized void setSpectrumAnalyserDecay(float pDecay) {
        saDecay = pDecay;
    }

    public synchronized void setSpectrumAnalyserFFTSampleSize(int pSize) {
        saFFTSampleSize = pSize;
        fft = new KJFFT(saFFTSampleSize);
        old_FFT = new float[saFFTSampleSize];
        computeSAMultiplier();
    }

    public static FrequenciesDispatcher getInstance() {
        if (instance == null) {
            instance = new FrequenciesDispatcher();
        }
        
        return instance;
    }
}
