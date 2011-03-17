package com.joshondesign.amino.core;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 3/10/11
 * Time: 2:56 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Shape extends Node {
    protected Color fill = Color.GRAY;
    protected double strokeWidth;
    protected Color stroke = Color.BLACK;

    public Node setStrokeWidth(double strokeWidth) {
        this.strokeWidth = strokeWidth;
        return this;
    }
    public Shape setFill(Color fill) {
        this.fill = fill;
        return this;
    }
    public Shape setStroke(Color stroke) {
        this.stroke = stroke;
        return this;
    }

    public Color getFill() {
        return fill;
    }

    public double getStrokeWidth() {
        return strokeWidth;
    }

    public Color getStroke() {
        return stroke;
    }
}
