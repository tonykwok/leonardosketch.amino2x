package com.joshondesign.amino.core;

/**
@class Parent the interface that all parents must implement.
@category misc
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
    public AminoPoint convertToChildCoords(AminoPoint pt);
}
