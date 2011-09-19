package com.joshondesign.amino.core;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: josh
 * Date: 9/19/11
 * Time: 12:41 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class GFX {

    // ========= init and cleanup ==========
    public abstract void dispose();



    // ========= state changes =============
    public abstract void setPaint(Color backgroundFill);



    // ========= integer drawing functions =========
    public abstract void drawLine(int x1, int y1, int x2, int y2);
    public abstract void fillRect(int x, int y, int w, int h);
    public abstract void drawRect(int x, int y, int w, int h);



    // ========= image drawing functions ==========
    public abstract void drawImage(AminoImage image, int x, int y);
    public abstract void drawImage(AminoImage image, int sx, int sy, int sw, int sh, int dx, int dy, int dw, int dh);
    public abstract void drawImage9Slice(AminoImage image, int left, int right, int top, int bottom, int x, int y, int w, int h);



    // ========= text drawing functions
    public abstract void fillText(AminoFont font, String s, int x, int y);



    // ========= floating point drawing functions =========
    public abstract void translate(double x, double y);


}
