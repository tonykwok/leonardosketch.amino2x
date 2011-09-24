package com.joshondesign.amino.core;

import java.awt.geom.Point2D;

/**
 @class Bounds An immutable class representing the bounds of a node. It is usually used to represent visual bounds.
 @category resource
 */
public class Bounds {
    private int x;
    private int y;
    private int width;
    private int height;

    public Bounds(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    //@property x the readonly left edge of the bounds
    public int getX() {
        return x;
    }

    //@property y the readonly top edge of the bounds
    public int getY() {
        return y;
    }

    //@property width the readonly width of the bounds
    public int getWidth() {
        return width;
    }

    //@property height the readonly height of the bounds
    public int getHeight() {
        return height;
    }

    public boolean inside(Point2D point) {
        if(point.getX() >= x && point.getX() <= x + width) {
            if(point.getY() >=y && point.getY()<= y+height) {
                return true;
            }
        }
        return false;
    }


}
