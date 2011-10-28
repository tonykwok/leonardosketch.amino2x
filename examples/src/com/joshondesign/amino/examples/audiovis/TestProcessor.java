/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.joshondesign.amino.examples.audiovis;

/**
 *
 * @author josh
 */
class TestProcessor implements FrequenciesDataProcessor {
    private float[] leftData;
    private float[] rightData;
    private final FrequenciesDispatcher dispatcher;

    public TestProcessor() {
        dispatcher = FrequenciesDispatcher.getInstance();
        dispatcher.addProcessor(this);
        leftData = new float[dispatcher.getFrequenciesBandCount()];
        rightData = new float[dispatcher.getFrequenciesBandCount()];
    }

    public float getData(int index) {
        return leftData[index];
    }

    public void process(float[] leftData, float[] rightData) {
        this.leftData = leftData;
        this.rightData = rightData;
   }

}
