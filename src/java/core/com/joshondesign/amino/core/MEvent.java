package com.joshondesign.amino.core;

import java.awt.geom.Point2D;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 3/10/11
 * Time: 3:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class MEvent {
    double x;
    double y;
    Node node;

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Node getNode() {
        return node;
    }

    public Point2D getPoint() {
        return new Point2D.Double(x,y);
    }
}
