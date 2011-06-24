package com.joshondesign.amino.core;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 @class BlurNode
 @category effect
 */
public class BlurNode extends Node implements Parent {
    Node node;
    protected Buffer buf1;
    protected Buffer buf2;

    public BlurNode(Node n) {
        this.node = n;
        this.buf1 = null;
        this.buf2 = null;
        this.blurRadius = 1;
    }

    //@property blurRadius the radius of the blur
    protected int blurRadius;
    public BlurNode setBlurRadius(int r) {
        this.blurRadius = r;
        return this;
    }
    
    @Override
    public void draw(Graphics2D ctx) {
        Bounds bounds = this.node.getVisualBounds();
        if(this.buf1==null) {
            allocateBuffers(bounds);
        }

        //redraw the child only if it's dirty
        if(this.isDirty()) {
            drawChild(bounds);
        }
        //ctx.save();
        ctx.translate(bounds.getX(),bounds.getY());
        ctx.drawImage(this.buf2.buf, 0, 0, null);
        ctx.translate(-bounds.getX(), -bounds.getY());
        //ctx.restore();

        this.clearDirty();
    }

    protected void allocateBuffers(Bounds bounds) {
        this.buf1 = new Buffer(
            bounds.getWidth()+this.blurRadius*4
            ,bounds.getHeight()+this.blurRadius*4
            );
        this.buf2 = new Buffer(
            bounds.getWidth()+this.blurRadius*4
            ,bounds.getHeight()+this.blurRadius*4
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
        this.node.draw(ctx1);
        //ctx1.restore();
        ctx1.dispose();

        //apply affect from buf1 into buf2
        this.buf2.clear();
        this.applyEffect(this.buf1,this.buf2,this.blurRadius);

        //overdraw
        overdraw(bounds);
    }

    protected void overdraw(Bounds bounds) {
    }

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
                r = r/divisor;
                g = g/divisor;
                b = b/divisor;
                int rgba2 = (a<<24)|(r<<16)|(g<<8)|b;
                buf2.buf.setRGB(x,y,rgba2);
            }
        }

/*
        for(int x = 0+size; x<buf.getWidth()-size; x++) {
            for(int y=0+size; y<buf.getHeight()-size; y++) {
                int rgba = buf.buf.getRGB(x,y);
                buf2.buf.setRGB(x,y,rgba);
                //buf2.setRGBA(data,x,y,r,g,b,a);
            }
        }
  */

        /*
        for(var i = 0; i<buf2.getHeight(); i++) {
            buf2.setRGBA(data,0,i,0xFF,0xFF,0xFF,0xFF);
            buf2.setRGBA(data,buf2.getWidth()-1,i,0xFF,0xFF,0xFF,0xFF);
        }
        for(var i = 0; i<buf2.getWidth(); i++) {
            buf2.setRGBA(data,i,0,0xFF,0xFF,0xFF,0xFF);
            buf2.setRGBA(data,i,buf2.getHeight()-1,i,0xFF,0xFF,0xFF,0xFF);
        }
        */

        //buf2.setData(data);
    }


    public boolean hasChildren() {
        return true;
    }

    public int childCount() {
        return 1;
    }

    public Node getChild(int i) {
        return node;
    }

    public Point2D convertToChildCoords(Point2D pt) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
