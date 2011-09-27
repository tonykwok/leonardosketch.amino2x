package com.joshondesign.amino.core;


/**
@class MEvent a mouse event. It contains the x and y coordinates of where
the user pressed/moved/dragged the mouse, as well as the node that
was clicked on.
@category misc
 */
public class MEvent {
    public double x;
    public double y;
    public Node node;

    //@property x the x coordinate of the mouse event
    public double getX() {
        return x;
    }

    //@property y the y coordinate of the mouse event
    public double getY() {
        return y;
    }

    //@property node the original node that the event happened on
    public Node getNode() {
        return node;
    }

    //@property point a Point2D containing the x and y coordinates
    public AminoPoint getPoint() {
        return new AminoPoint(x,y);
    }

    @Override
    public String toString() {
        return "MEvent{" +
                "x=" + x +
                ", y=" + y +
                ", node=" + node +
                '}';
    }
}
