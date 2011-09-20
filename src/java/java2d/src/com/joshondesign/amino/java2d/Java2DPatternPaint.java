package com.joshondesign.amino.java2d;

import com.joshondesign.amino.core.PatternPaint;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: josh
 * Date: 9/19/11
 * Time: 6:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class Java2DPatternPaint extends PatternPaint implements Graphics2DPaint {
    private BufferedImage image;
    private TexturePaint _paint;

    public Java2DPatternPaint(File file) throws IOException {
        this.image = ImageIO.read(file);
    }

    public Java2DPatternPaint(URL url) throws IOException {
        this.image = ImageIO.read(url);
    }

    public Paint getGraphics2DPaint() {
        if(_paint == null) {
            _paint = new TexturePaint(this.image, new Rectangle(0,0,this.image.getWidth(),this.image.getHeight()));
        }
        return _paint;
    }
}
