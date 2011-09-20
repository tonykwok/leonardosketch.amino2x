package com.joshondesign.amino.core;

import java.awt.*;
import java.awt.List;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: josh
 * Date: 9/19/11
 * Time: 3:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class LinearGradient implements AminoPaint {
    protected java.util.List<Double> values = new ArrayList<Double>();
    protected java.util.List<AminoColor> colors = new ArrayList<AminoColor>();

    public LinearGradient addColor(double value, AminoColor color) {
        this.values.add(value);
        this.colors.add(color);
        return this;
    }


}
