package com.joshondesign.amino.examples.audiovis;

/**
* Created by IntelliJ IDEA.
* User: josh
* Date: 10/28/11
* Time: 3:47 PM
* To change this template use File | Settings | File Templates.
*/
public class AudioProcessor implements FrequenciesDataProcessor {
    private FrequenciesDispatcher dispatcher;
    float[] leftData;
    float[] rightData;

    public AudioProcessor() {
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

    public float[] getLeftData() {
        return leftData;
    }
}
