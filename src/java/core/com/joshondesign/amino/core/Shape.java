package com.joshondesign.amino.core;

/**
@class Shape The base class of all shape *Node*s.
@category shape
 */
public abstract class Shape extends Node {
    protected AminoPaint fill = AminoColor.GRAY;
    protected double strokeWidth;
    protected AminoPaint stroke = AminoColor.BLACK;

    //@property strokeWidth the width of the stroke for this shape. If set to *0* the shape will not be stroked.
    public Node setStrokeWidth(double strokeWidth) {
        this.strokeWidth = strokeWidth;
        return this;
    }
    //@property fill the color to fill the shape with
    public Shape setFill(AminoPaint fill) {
        this.fill = fill;
        return this;
    }
    //@property stroke the color to draw the shape's stroke with
    public Shape setStroke(AminoPaint stroke) {
        this.stroke = stroke;
        return this;
    }

    public AminoPaint getFill() {
        return fill;
    }

    public double getStrokeWidth() {
        return strokeWidth;
    }

    public AminoPaint getStroke() {
        return stroke;
    }
}
