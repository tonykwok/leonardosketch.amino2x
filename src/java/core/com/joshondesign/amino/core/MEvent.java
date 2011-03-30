package com.joshondesign.amino.core;

import java.awt.geom.Point2D;

/**
@class MEvent a mouse event. It contains the x and y coordinates of where
the user pressed/moved/dragged the mouse, as well as the node that
was clicked on.
 */
public class MEvent {
    double x;
    double y;
    Node node;

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
    public Point2D getPoint() {
        return new Point2D.Double(x,y);
    }
}
