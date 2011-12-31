package com.joshondesign.amino.core;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
@class Group A group contains an ordered list of nodes. It doesn't draw anything by itself, but draws all of it's children in the order they are added.
@extends Node
@implements Parent
@category shape
 */
public class Group extends Node implements Parent {
    List<Node> nodes = new ArrayList<Node>();

    public Group() {
        super();
    }

    //@method add a child node
    public Group add(Node node) {
        this.nodes.add(node);
        node.setParent(this);
        markDirty();
        return this;
    }

    //@method remove a child node
    public void remove(Node node) {
        this.nodes.remove(node);
        node.setParent(null);
        markDirty();
    }

    //@method remove all child nodes
    public void clear() {
        for(Node n : nodes) {
            n.setParent(null);
        }
        nodes.clear();
        markDirty();
    }



    @Override
    public void draw(GFX gfx) {
        if(!this.isVisible()) return;
        gfx.translate(this.x,this.y);
        for(Node n : nodes) {
            n.draw(gfx);
        }
        gfx.translate(-this.x, -this.y);
    }



    //@property x An X offset.
    private double x = 0.0;
    public double getX() {
        return this.x;
    }
    public Group setX(double x) {
        this.x = x;
        markDirty();
        return this;
    }

    //@property y A Y offset.
    public double getY() {
        return this.y;
    }
    public Group setY(double y) {
        this.y = y;
        markDirty();
        return this;
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

    public AminoPoint convertToChildCoords(AminoPoint pt) {
        return new AminoPoint(pt.getX()-this.getX(),pt.getY()-this.getY());
    }
}
