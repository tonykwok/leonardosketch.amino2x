package com.joshondesign.amino.core;

import java.awt.geom.Point2D;

/**
@class Parent the interface that all parents must implement.
 */
public interface Parent {
    //@method mark this parent as dirty
    public void markDirty();
    //@method return true if the parent has children
    public boolean hasChildren();
    //@method return the number of children
    public int childCount();
    //@method get the child at index *i*
    public Node getChild(int i);
    //@method convert the coordinates into the child's coordinate system
    public Point2D convertToChildCoords(Point2D pt);
}
