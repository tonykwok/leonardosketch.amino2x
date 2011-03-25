package com.joshondesign.amino.core;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 3/14/11
 * Time: 11:36 AM
 * To change this template use File | Settings | File Templates.
 */
public class ImageView extends Node {
    private URL src;
    private BufferedImage img;
    private int width;
    private int height;
    private boolean loaded;

    ImageView(URL url) throws IOException {
        this.src = url;
        this.img = ImageIO.read(url);
        this.loaded = true;
        this.x = 0.0;
        this.y = 0.0;
        this.width = img.getWidth();
        this.height = img.getHeight();
    }

    @Override
    public void draw(Graphics2D gfx) {
        gfx.drawImage(img,(int)x,(int)y,null);
        clearDirty();
    }

    @Override
    public boolean contains(Point2D pt) {
        double x = pt.getX();
        double y = pt.getY();
        if(x >= this.x && x <= this.x + this.width) {
            if(y >= this.y && y<=this.y + this.height) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Bounds getVisualBounds() {
        return new Bounds((int)getX(),(int)getY(),this.width,this.height);
    }
}
