package com.joshondesign.amino.core;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 3/10/11
 * Time: 2:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class Group extends Node implements Parent {
    List<Node> nodes = new ArrayList<Node>();

    public Group() {
        super();
    }

    public Group add(Node node) {
        this.nodes.add(node);
        node.setParent(this);
        markDirty();
        return this;
    }

    public void remove(Node node) {
        this.nodes.remove(node);
        node.setParent(null);
        markDirty();
    }

    public void clear() {
        for(Node n : nodes) {
            n.setParent(null);
        }
        nodes.clear();
        markDirty();
    }



    @Override
    public void draw(Graphics2D gfx) {
        if(!this.isVisible()) return;
        for(Node n : nodes) {
            n.draw(gfx);
        }
    }



    public boolean hasChildren() {
        return true;
    }

    public int childCount() {
        return nodes.size();
    }

    public Node getChild(int i) {
        return nodes.get(i);
    }

    public Point2D convertToChildCoords(Point2D pt) {
        return new Point2D.Double(pt.getX(),pt.getY());
    }
}
