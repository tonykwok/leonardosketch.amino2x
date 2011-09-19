package com.joshondesign.amino.java2d;

import com.joshondesign.amino.core.AminoFont;
import com.joshondesign.amino.core.AminoImage;
import com.joshondesign.amino.core.GFX;
import com.joshondesign.amino.core.u;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: josh
 * Date: 9/19/11
 * Time: 1:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class Java2DGFX extends GFX {
    private Graphics2D g;

    public Java2DGFX(Graphics2D graphics2D) {
        super();
        this.g = graphics2D;
    }

    @Override
    public void dispose() {
        g.dispose();
    }

    @Override
    public void setPaint(Color backgroundFill) {
        g.setPaint(backgroundFill);
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        g.drawLine(x1,y1,x2,y2);
    }

    @Override
    public void fillRect(int x, int y, int w, int h) {
        g.fillRect(x,y,w,h);
    }

    @Override
    public void drawRect(int x, int y, int w, int h) {
        g.drawRect(x,y,w,h);
    }

    @Override
    public void drawImage(AminoImage image, int x, int y) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void drawImage(AminoImage image, int sx, int sy, int sw, int sh, int dx, int dy, int dw, int dh) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void drawImage9Slice(AminoImage image, int left, int right, int top, int bottom, int x, int y, int w, int h) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void fillText(AminoFont font, String s, int x, int y) {
        g.setFont(((Java2DFont)font)._font);
        g.drawString(s,x,y);
    }

    @Override
    public void translate(double x, double y) {
        this.g.translate(x,y);
    }
}
