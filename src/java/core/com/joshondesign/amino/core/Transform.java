package com.joshondesign.amino.core;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 3/14/11
 * Time: 12:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class Transform extends Node implements Parent {
    private Node node;
    private double rotation;
    private double translateX;
    private double translateY;

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

    public Transform setTranslateX(double translateX) {
        this.translateX = translateX;
        return this;
    }

    public Transform setTranslateY(double translateY) {
        this.translateY = translateY;
        return this;
    }

    public double getRotation() {
        return this.rotation;
    }
    public Transform setRotation(double r) {
        this.rotation = r;
        return this;
    }
    public double getTranslateX() {
        return this.translateX;
    }
    public double getTranslateY() {
        return this.translateY;
    }
}
