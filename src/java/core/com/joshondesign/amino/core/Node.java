package com.joshondesign.amino.core;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 3/10/11
 * Time: 2:35 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Node {
    private Parent parent = null;
    private boolean visible = true;
    private boolean mouseBlocked = false;
    private boolean dirty = true;

    public Node() {

    }

    Parent getParent() {
        return this.parent;
    }
    Node setParent(Parent parent) {
        this.parent = parent;
        return this;
    }

    Node setVisible(boolean visible) {
        this.visible = visible;
        markDirty();
        return this;
    }

    public boolean isVisible() {
        return visible;
    }

    public boolean isMouseBlocked() {
        return mouseBlocked;
    }

    public Node setMouseBlocked(boolean mouseBlocked) {
        this.mouseBlocked = mouseBlocked;
        return this;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void markDirty() {
        this.dirty = true;
        if(this.getParent() != null) {
            this.getParent().markDirty();
        }
    }
    public void clearDirty() {
        this.dirty = false;
    }



    public boolean contains(Point2D pt) {
        return false;
    }


    public abstract void draw(Graphics2D gfx);

    protected double x;
    protected double y;

    public double getX() {
        return x;
    }

    public Node setX(double x) {
        this.x = x;
        markDirty();
        return this;
    }

    public double getY() {
        return y;
    }

    public Node setY(double y) {
        this.y = y;
        markDirty();
        return this;
    }

    public Bounds getVisualBounds() {
        return null;
    }
}
