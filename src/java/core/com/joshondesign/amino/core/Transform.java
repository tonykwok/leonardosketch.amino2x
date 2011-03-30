package com.joshondesign.amino.core;

import java.awt.*;
import java.awt.geom.Point2D;

/**
@class Transform A parent node which transforms it's child with rotation, translation, and scaling.
@extends Node
@implements Parent
@category misc
 */
public class Transform extends Node implements Parent {
    private Node node;
    
    private double rotation;
    private double translateX;
    private double translateY;

    //@constructor create a new Transform node around the child *node* .
    public Transform(Node node) {
        this.node = node;
        node.setParent(this);
        this.rotation = 0.0;
        this.translateX = 0.0;
        this.translateY = 0.0;
    }

    @Override
    public boolean contains(Point2D pt) {
        return false;
    }

    @Override
    public void draw(Graphics2D gfx) {
        Graphics2D ctx = (Graphics2D) gfx.create();
        ctx.translate(this.translateX,this.translateY);
        ctx.rotate(this.rotation * Math.PI / 180.0, 0, 0);
        this.node.draw(ctx);
        ctx.dispose();
        this.clearDirty();
    }

    public boolean hasChildren() {
        return true;
    }

    public int childCount() {
        return 1;
    }

    public Node getChild(int i) {
        return this.node;
    }

    public Point2D convertToChildCoords(Point2D pt) {
        double x = pt.getX();
        double y = pt.getY();
        double x1 = x-this.translateX;
        double y1 = y-this.translateY;
        double a = -this.rotation * Math.PI/180;
        double x2 = x1*Math.cos(a) - y1*Math.sin(a);
        double y2 = x1*Math.sin(a) + y1*Math.cos(a);
        return new Point2D.Double(x2,y2);
    }

    //@property translateX the X translation of the node
    public Transform setTranslateX(double translateX) {
        this.translateX = translateX;
        return this;
    }
    public double getTranslateX() {
        return this.translateX;
    }

    //@property translateY the Y translation of the node
    public Transform setTranslateY(double translateY) {
        this.translateY = translateY;
        return this;
    }
    public double getTranslateY() {
        return this.translateY;
    }

    //@property rotation the clockwise rotation of the node in degrees
    public double getRotation() {
        return this.rotation;
    }
    public Transform setRotation(double r) {
        this.rotation = r;
        return this;
    }
}
