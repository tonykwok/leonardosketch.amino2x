package com.joshondesign.amino.jogl;

/**
 * Created by IntelliJ IDEA.
 * User: josh
 * Date: 10/28/11
 * Time: 1:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class QRect {
    public int x;
    public int y;
    public int w;
    public int h;

    QRect(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public static QRect build(int x, int y, int width, int height) {
        return new QRect(x,y,width,height);
    }


}
