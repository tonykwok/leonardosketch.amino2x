package com.joshondesign.amino.core;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 3/24/11
 * Time: 5:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class Buffer {
    private int width;
    private int height;
    BufferedImage buf;

    public Buffer(int width, int height) {
        this.width = width;
        this.height = height;
        this.buf = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void clear() {

    }

    public Graphics2D getContext() {
        return this.buf.createGraphics();
    }
}
