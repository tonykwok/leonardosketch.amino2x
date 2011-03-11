package com.joshondesign.amino.core;

import java.awt.*;
import java.awt.geom.Point2D;

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
    private Color fill = Color.GRAY;
    private double strokeWidth;

    public Rect set(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        return this;
    }

    public Rect setFill(Color fill) {
        this.fill = fill;
        return this;
    }

    public Node setStrokeWidth(double strokeWidth) {
        this.strokeWidth = strokeWidth;
        return this;
    }

    @Override
    public void draw(Graphics2D gfx) {
        gfx.setPaint(fill);
        gfx.fillRect((int)x,(int)y,(int)width, (int)height);
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
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
}
