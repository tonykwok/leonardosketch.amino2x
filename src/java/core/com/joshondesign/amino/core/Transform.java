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
    
    private double rotate;
    private double translateX;
    private double translateY;
    private double scaleX;
    private double scaleY;

    //@constructor create a new Transform node around the child *node* .
    public Transform(Node node) {
        this.node = node;
        node.setParent(this);
        this.rotate = 0.0;
        this.translateX = 0.0;
        this.translateY = 0.0;
        this.scaleX = 1.0;
        this.scaleY = 1.0;
    }

    @Override
    public boolean contains(Point2D pt) {
        return false;
    }

    @Override
    public void draw(GFX gfx) {
        //Graphics2D ctx = (Graphics2D) gfx.create();
        gfx.translate(this.translateX,this.translateY);
        //ctx.rotate(this.rotate * Math.PI / 180.0, 0, 0);
        //ctx.scale(this.scaleX,this.scaleY);
        this.node.draw(gfx);
        //ctx.dispose();
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
        double a = -this.rotate * Math.PI/180;
        double x2 = x1*Math.cos(a) - y1*Math.sin(a);
        double y2 = x1*Math.sin(a) + y1*Math.cos(a);
        x2 = x2/this.scaleX;
        y2 = y2/this.scaleY;
        return new Point2D.Double(x2,y2);
    }

    //@property translateX the X translation of the node
    public Transform setTranslateX(double translateX) {
        this.translateX = translateX;
        markDirty();
        return this;
    }
    public double getTranslateX() {
        return this.translateX;
    }

    //@property translateY the Y translation of the node
    public Transform setTranslateY(double translateY) {
        this.translateY = translateY;
        markDirty();
        return this;
    }
    public double getTranslateY() {
        return this.translateY;
    }

    //@property rotation the clockwise rotation of the node in degrees
    public double getRotate() {
        return this.rotate;
    }
    public Transform setRotate(double r) {
        this.rotate = r;
        markDirty();
        return this;
    }

    //@property scaleX scale in the X direction
    public double getScaleX() {
        return this.scaleX;
    }
    public Transform setScaleX(double scaleX) {
        this.scaleX = scaleX;
        markDirty();
        return this;
    }
    //@property scaleY scale in the Y direction
    public double getScaleY() {
        return this.scaleY;
    }
    public Transform setScaleY(double scaleY) {
        this.scaleY = scaleY;
        markDirty();
        return this;
    }
}
