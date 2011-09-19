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

    // ========= double drawing functions =========

    public abstract void translate(double x, double y);


}
