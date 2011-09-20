package com.joshondesign.amino.java2d;

import com.joshondesign.amino.core.LinearGradient;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: josh
 * Date: 9/19/11
 * Time: 3:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class Java2DVerticalLinearGradient extends LinearGradient implements Graphics2DPaint{
    private int start;
    private int end;
    private LinearGradientPaint _paint;

    public Java2DVerticalLinearGradient(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public Paint getGraphics2DPaint() {
        if(_paint == null) {
            float[] vs = new float[this.values.size()];
            Color[] cs = new Color[this.values.size()];

            for(int i=0; i<vs.length; i++) {
                vs[i] = this.values.get(i).floatValue();
                cs[i] = this.colors.get(i);
            }
            _paint = new LinearGradientPaint(new Point(0,start), new Point(0,end),vs,cs, MultipleGradientPaint.CycleMethod.REPEAT);
        }
        return _paint;
    }
}
