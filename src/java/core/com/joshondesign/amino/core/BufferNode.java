package com.joshondesign.amino.core;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 3/24/11
 * Time: 5:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class BufferNode extends Node implements Parent {
    private Node node;
    private Buffer buf;

    public BufferNode(Node child) {
        this.node = child;
        child.setParent(this);
    }

    @Override
    public void draw(Graphics2D ctx) {
        Bounds bounds = this.node.getVisualBounds();
        if(this.buf == null) {
            this.buf = new Buffer(bounds.getWidth(),bounds.getHeight());
        }
        //redraw the child only if it's dirty
        if(this.isDirty()) {

            Graphics2D ctx2 = this.buf.getContext();
            ctx2.translate(-bounds.getX(),-bounds.getY());
            this.node.draw(ctx2);
            ctx2.dispose();
        }
        ctx.translate(bounds.getX(),bounds.getY());
        ctx.drawImage(this.buf.buf,0,0,null);
        ctx.translate(-bounds.getX(),-bounds.getY());
        this.clearDirty();
    }

    public boolean hasChildren() {
        return true;
    }

    public int childCount() {
        return 1;
    }

    public Node getChild(int i) {
        return node;
    }

    public Point2D convertToChildCoords(Point2D pt) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
