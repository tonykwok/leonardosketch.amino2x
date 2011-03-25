package com.joshondesign.amino.core;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 3/24/11
 * Time: 5:38 PM
 * To change this template use File | Settings | File Templates.
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

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
