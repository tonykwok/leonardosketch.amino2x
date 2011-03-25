package com.joshondesign.amino.core;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 3/10/11
 * Time: 2:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class Rect extends Shape {
    private double width;
    private double height;
    private double corner = 0;

    public Rect set(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        return this;
    }


    public Rect setCorner(double corner) {
        this.corner = corner;
        return this;
    }

    @Override
    public void draw(Graphics2D gfx) {
        gfx.setPaint(fill);
        gfx.fillRect((int)x,(int)y,(int)width, (int)height);

        java.awt.Shape r = null;
        if(corner > 0) {
            r = new RoundRectangle2D.Double(x, y, width, height, corner*2, corner*2);
        } else {
            r = new Rectangle2D.Double(x, y, width, height);
        }
        gfx.fill(r);
        if(strokeWidth > 0) {
            gfx.setPaint(stroke);
            gfx.setStroke(new BasicStroke((float) strokeWidth));
            gfx.draw(r);
            gfx.setStroke(new BasicStroke(1));
        }
        this.clearDirty();
    }

    public double getWidth() {
        return width;
    }

    public Rect setWidth(double width) {
        this.width = width;
        return this;
    }

    public double getHeight() {
        return height;
    }

    public Rect setHeight(double height) {
        this.height = height;
        return this;
    }

    public boolean contains(Point2D pt) {
        double x= pt.getX();
        double y = pt.getY();
        //console.log("comparing: " + this.x + " " + this.y + " " + this.width + " " + this.height + " --- " + x + " " + y);
        if(x >= this.x && x <= this.x + this.width) {
            if(y >= this.y && y<=this.y + this.height) {
                //console.log("returning true");
                return true;
            }
        }
        //console.log("returning false");
        return false;
    }

    @Override
    public Bounds getVisualBounds() {
        return new Bounds((int)getX(),(int)getY(),(int)getWidth(),(int)getHeight());
    }
}
