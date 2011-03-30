package com.joshondesign.amino.core;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
@class Buffer A pixel grid you can draw into. Used for storing pixels for later, such as buffering effects to make the be faster.
@category resource
 */
public class Buffer {
    private int width;
    private int height;
    BufferedImage buf;

    //@constructor create a buffer with a given width and height
    public Buffer(int width, int height) {
        this.width = width;
        this.height = height;
        this.buf = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
    }

    //@property width the width of this buffer in pixels
    public int getWidth() {
        return width;
    }

    //@property height the height of this buffer in pixels
    public int getHeight() {
        return height;
    }

    //@method clear the buffer
    public void clear() {

    }

    //@method get a Java2D Graphics2D context to draw into
    public Graphics2D getContext() {
        return this.buf.createGraphics();
    }
}
