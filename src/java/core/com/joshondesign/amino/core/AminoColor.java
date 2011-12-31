package com.joshondesign.amino.core;

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

    public static AminoPaint fromRGBA(int r, int g, int b, int a) {
        return new AminoColor(r,g,b,a);
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

    public int getAlpha() {
        return this.a;
    }

    public AminoColor withAlpha(double alpha) {
        return new AminoColor(getRed(),getGreen(),getBlue(),(int)(alpha*255));
    }



    @Override
    public String toString() {
        return "AminoColor{" +
                "r=" + r +
                ", g=" + g +
                ", b=" + b +
                ", a=" + a +
                '}';
    }


    //adapted from http://www.cs.rit.edu/~ncs/color/t_convert.html
    public static AminoPaint fromHSV(int h, float s, float v) {
        if(s==0) {
            return AminoColor.fromRGB(v,v,v);
        }
        h/=60;
        int i = (int) Math.floor(h);
        float f = h-i;
        float p = v*(1-s);
        float q = v*(1-s*f);
        float t = v*(1-s*(1-f));
        switch (i) {
            case 0: return AminoColor.fromRGB(v,t,p);
            case 1: return AminoColor.fromRGB(q,v,p);
            case 2: return AminoColor.fromRGB(p,v,t);
            case 3: return AminoColor.fromRGB(p,q,v);
            case 4: return AminoColor.fromRGB(t,p,v);
            default: return AminoColor.fromRGB(v,p,q);
        }
    }
}
