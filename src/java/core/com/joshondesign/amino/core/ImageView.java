package com.joshondesign.amino.core;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
@class ImageView a node which draws an image
@category shape
 */
public class ImageView extends Node {
    private URL src;
    private BufferedImage img;
    private int width;
    private int height;
    private boolean loaded;

    
    //@constructor create an imageview from a URL
    public ImageView(URL url) throws IOException {
        this.src = url;
        this.img = ImageIO.read(url);
        this.loaded = true;
        this.x = 0.0;
        this.y = 0.0;
        this.width = img.getWidth();
        this.height = img.getHeight();
    }

    @Override
    public void draw(GFX gfx) {
        //gfx.drawImage(img,(int)x,(int)y,null);
        clearDirty();
    }

    @Override
    public boolean contains(AminoPoint pt) {
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
