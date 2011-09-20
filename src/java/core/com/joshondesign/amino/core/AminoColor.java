package com.joshondesign.amino.core;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: josh
 * Date: 9/19/11
 * Time: 8:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class AminoColor implements AminoPaint {
    private int r;
    private int g;
    private int b;
    private int a;


    public static final AminoColor BLACK = AminoColor.fromRGB(0, 0, 0);
    public static final AminoColor GRAY = AminoColor.fromRGB(200, 200, 200);
    public static final AminoColor WHITE = AminoColor.fromRGB(255, 255, 255);

    public static final AminoColor RED =     AminoColor.fromRGB(255,   0,  0);
    public static final AminoColor YELLOW =  AminoColor.fromRGB(255, 255,  0);
    public static final AminoColor MAGENTA = AminoColor.fromRGB(255,   0,  255);
    public static final AminoColor GREEN =   AminoColor.fromRGB(0,   255,  0);
    public static final AminoPaint CYAN =    AminoColor.fromRGB(0,   255,  255);
    public static final AminoColor BLUE =    AminoColor.fromRGB(0,     0,  255);
    public static final AminoColor ORANGE =  AminoColor.fromRGB(255, 165,  0);

    AminoColor(int r, int g, int b, int a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public static AminoColor fromRGB(int r, int g, int b) {
        return new AminoColor(r,g,b,255);
    }
    public static AminoColor fromRGB(float r, float g, float b) {
        return new AminoColor((int)(r*255),(int)(g*255),(int)(b*255),255);
    }

    public static AminoColor fromRGB(int color) {
        return new AminoColor(
                (color&0x00ff0000)>>16,
                (color&0x0000ff00)>>8,
                (color&0x000000FF)>>0,
                255
        );
    }

    public int getRed() {
        return r;
    }

    public int getGreen() {
        return g;
    }

    public int getBlue() {
        return b;
    }
}
