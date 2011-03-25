package com.joshondesign.amino.core;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 3/24/11
 * Time: 8:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class ShadowNode extends BlurNode {
    private double offsetX;
    private double offsetY;

    public ShadowNode(Node n) {
        super(n);
        this.offsetX = 5.0;
        this.offsetY = 5.0;
    }

    @Override
    protected void allocateBuffers(Bounds bounds) {
        this.buf1 = new Buffer(
            (int)(bounds.getWidth()+this.offsetX+this.blurRadius*4)
            ,(int)(bounds.getHeight()+this.offsetY+this.blurRadius*4)
            );
        this.buf2 = new Buffer(
            (int)(bounds.getWidth()+this.offsetX+this.blurRadius*4)
            ,(int)(bounds.getHeight()+this.offsetY+this.blurRadius*4)
            );
    }

    protected void drawChild(Bounds bounds) {
        //render child into first buffer
        this.buf1.clear();
        Graphics2D ctx1 = this.buf1.getContext();
        ctx1.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        //ctx1.save();
        ctx1.translate(
            -bounds.getX()+this.blurRadius*2
            ,-bounds.getY()+this.blurRadius*2);
        ctx1.translate(this.offsetX,this.offsetY);
        this.node.draw(ctx1);
        //ctx1.restore();
        ctx1.dispose();

        //apply affect from buf1 into buf2
        this.buf2.clear();
        this.applyEffect(this.buf1,this.buf2,this.blurRadius);

        //overdraw
        Graphics2D ctx2 = this.buf2.getContext();
        ctx2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        ctx2.translate(
                -bounds.getX() + this.blurRadius * 2
                , -bounds.getY() + this.blurRadius * 2);
        this.node.draw(ctx2);
        ctx2.dispose();
    }

    @Override
    protected void applyEffect(Buffer buf, Buffer buf2, int radius) {
        int s = radius*2+1;
        int size = radius;
        for(int x = 0+size; x<buf.getWidth()-size; x++) {
            for(int y = 0+size; y<buf.getHeight()-size; y++) {
                int r = 0;
                int g = 0;
                int b = 0;
                int a = 0;
                int count =0;
                for(int ix=x-size; ix<=x+size; ix++) {
                    for(int iy=y-size;iy<=y+size;iy++) {
                        int argb = buf.buf.getRGB(ix,iy);
                        a += ((argb>>24) &0xFF);
                        r += ((argb>>16) &0xFF);
                        g += ((argb>>8)  &0xFF);
                        b += ((argb>>0)  &0xFF);
                        count++;
                    }
                }
                int divisor = s*s;
                a = a/divisor;
                r = 0;
                g = 0;
                b = 0;
                int rgba2 = (a<<24)|(r<<16)|(g<<8)|b;
                buf2.buf.setRGB(x,y,rgba2);
            }
        }
    }

    @Override
    protected void overdraw(Bounds bounds) {
    }
}
