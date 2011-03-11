package com.joshondesign.amino.core;

import java.awt.geom.Point2D;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 3/10/11
 * Time: 2:39 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Parent {
    public void markDirty();
    public boolean hasChildren();
    public int childCount();
    public Node getChild(int i);
    public Point2D convertToChildCoords(Point2D pt);
}
