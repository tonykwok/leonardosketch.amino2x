package com.joshondesign.amino.core;


/**
@class Node the base class for all nodes
@category shape
 */
public abstract class Node {
    private Parent parent = null;
    private boolean visible = true;
    private boolean mouseBlocked = false;
    private boolean dirty = true;

    public Node() {

    }

    
    //@property parent the parent node of this node. Might be null if this is the root node
    public Parent getParent() {
        return this.parent;
    }
    Node setParent(Parent parent) {
        this.parent = parent;
        return this;
    }

    //@property visible determines if the node is visible or not. Invisible nodes are not drawn and cannot receive events
    public Node setVisible(boolean visible) {
        this.visible = visible;
        markDirty();
        return this;
    }

    public boolean isVisible() {
        return visible;
    }

    //@property mouseBlocked determines if the node blocks the mouse or allows mouse events to go through it to nodes underneath
    public boolean isMouseBlocked() {
        return mouseBlocked;
    }

    public Node setMouseBlocked(boolean mouseBlocked) {
        this.mouseBlocked = mouseBlocked;
        return this;
    }

    //@method dirty indicates if the node is dirty and should be redrawn
    public boolean isDirty() {
        return dirty;
    }

    //@method mark the node as dirty. This schedules it to be completely redrawn
    public void markDirty() {
        this.dirty = true;
        if(this.getParent() != null) {
            this.getParent().markDirty();
        }
    }
    //@method clear the dirty flag. This is should be called by a node after it draws itself
    public void clearDirty() {
        this.dirty = false;
    }



    //@method returns true if the node contains the point. The point should be in local coordinates.
    public boolean contains(AminoPoint pt) {
        return false;
    }


    //@method draw the node. This method is overridden by subclasses
    public abstract void draw(GFX gfx);

    //@property x the x coordinate of this node. Nodes may draw to the left of the 'x' coordinate.
    protected double x;
    //@property y the y coordinate of this node. Nodes may draw to the top of the 'y' coordinate.
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

    //@method get the visual bounds of this node. The node should not draw outside of these bounds.
    public Bounds getVisualBounds() {
        return null;
    }
}
