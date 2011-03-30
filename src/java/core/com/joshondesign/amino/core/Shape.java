package com.joshondesign.amino.core;

import java.awt.*;

/**
@class Shape The base class of all shape *Node*s.
 */
public abstract class Shape extends Node {
    protected Color fill = Color.GRAY;
    protected double strokeWidth;
    protected Color stroke = Color.BLACK;

    //@property strokeWidth the width of the stroke for this shape. If set to *0* the shape will not be stroked.
    public Node setStrokeWidth(double strokeWidth) {
        this.strokeWidth = strokeWidth;
        return this;
    }
    //@property fill the color to fill the shape with
    public Shape setFill(Color fill) {
        this.fill = fill;
        return this;
    }
    //@property stroke the color to draw the shape's stroke with
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
