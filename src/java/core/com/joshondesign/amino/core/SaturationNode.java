package com.joshondesign.amino.core;

import java.awt.*;
import java.awt.geom.Point2D;

/*
@class SaturationNode A parent node which adjusts the saturation of its child. Uses a buffer internally.
*/
public class SaturationNode extends Node implements Parent {
    private Node node;
    private Buffer buf1;
    private Buffer buf2;

    //@constructor create a saturation node which draws it's single child node through a saturation / desaturation filter.
    public SaturationNode(Node node) {
        this.node = node;
        this.node.setParent(this);
        this.buf1 = null;
        this.buf2 = null;

        this.saturation = 0.5;
    }


    //@property saturation value between 0 and 1
    private double saturation;
    public double getSaturation() {
        return saturation;
    }
    public SaturationNode setSaturation(double saturation) {
        this.saturation = saturation;
        markDirty();
        return this;
    }

    public void draw(Graphics2D ctx) {
        Bounds bounds = this.node.getVisualBounds();
        if(this.buf1==null) {
            this.buf1 = new Buffer(
                bounds.getWidth()
                ,bounds.getHeight()
                );
            this.buf2 = new Buffer(
                bounds.getWidth()
                ,bounds.getHeight()
                );
        }

        //redraw the child only if it's dirty
        if(this.isDirty()) {
            //render child into first buffer
            this.buf1.clear();
            Graphics2D ctx1 = this.buf1.getContext();
            //ctx1.save();
            ctx1.translate(
                -bounds.getX()
                ,-bounds.getY());
            this.node.draw(ctx1);
            //ctx1.restore();
            ctx1.dispose();

            //apply affect from buf1 into buf2
            this.buf2.clear();
            this.applyEffect(this.buf1,this.buf2);
            //buf1->buf2
        }
        //ctx.save();
        ctx.translate(bounds.getX(),bounds.getY());
        ctx.drawImage(this.buf2.buf, 0, 0, null);
        ctx.translate(-bounds.getX(), -bounds.getY());
        //ctx.restore();

        this.clearDirty();
    }

    private void applyEffect(Buffer buf, Buffer buf2) {
        int size = 0;
        double scale = 1-this.getSaturation();
        for(int x = 0+size; x<buf.getWidth()-size; x++) {
            for(int y=0+size; y<buf.getHeight()-size; y++) {
                int argb = buf.buf.getRGB(x,y);
                int a = ((argb>>24) &0xFF);
                int r = ((argb>>16) &0xFF);
                int g = ((argb>>8)  &0xFF);
                int b = ((argb>>0)  &0xFF);

                double v = r*0.21+g*0.71+b*0.07;
                r = (int)(r*(1-scale)+v*scale);
                g = (int)(g*(1-scale)+v*scale);
                b = (int)(b*(1-scale)+v*scale);
                int rgba2 = (a<<24)|(r<<16)|(g<<8)|b;
                buf2.buf.setRGB(x, y, rgba2);
            }
        }
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


