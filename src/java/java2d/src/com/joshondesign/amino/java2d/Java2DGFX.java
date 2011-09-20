package com.joshondesign.amino.java2d;

import com.joshondesign.amino.core.*;

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
    private AminoPaint paint;

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
        paint = null;
    }

    @Override
    public void setPaint(AminoPaint backgroundFill) {
        this.paint = backgroundFill;
        if(backgroundFill instanceof Graphics2DPaint) {
            g.setPaint(((Graphics2DPaint)backgroundFill).getGraphics2DPaint());
        }
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
    public void drawRoundRect(int x, int y, int w, int h, int corner) {
        g.drawRoundRect(x,y,w,h,corner,corner);
    }

    @Override
    public void fillRoundRect(int x, int y, int w, int h, int corner) {
        g.fillRoundRect(x,y,w,h,corner, corner);
    }

    @Override
    public void drawEllipse(int x, int y, int w, int h) {
        g.drawOval(x,y,w,h);
    }

    @Override
    public void fillEllipse(int x, int y, int w, int h) {
        g.fillOval(x,y,w,h);
    }

    @Override
    public void drawRect(int x, int y, int w, int h) {
        g.drawRect(x,y,w,h);
    }

    @Override
    public void drawImage(AminoImage image, int x, int y) {
        g.drawImage(((Java2DImage)image)._image,x,y,null);
    }

    @Override
    public void drawImage(AminoImage image, int sx, int sy, int sw, int sh, int dx, int dy, int dw, int dh) {
        g.drawImage(((Java2DImage)image)._image,
                dx,dy,dx+dw,dy+dh, //note that src & dest are swapped from the amino form
                sx,sy,sx+sw,sy+sh,
                null);
    }

    @Override
    public void drawImage9Slice(AminoImage image, int left, int right, int top, int bottom, int x, int y, int w, int h) {
        //upper left
        drawImage(image,
                0, 0, left, top,
                x, y, left, top);
        //upper center
        drawImage(image,
                left,0,image.getWidth()-left-right,top,
                x+left,y,w-left-right,top
                );
        //upper right
        drawImage(image,
                image.getWidth()-right,0,left,top,
                x+w-right,y,right,top
                );


        //center left
        drawImage(image,
                0,top,left,image.getHeight()-top-bottom,
                x,y+top,left,h-top-bottom);
        //upper center
        drawImage(image,
                left,top,image.getWidth()-left-right,image.getHeight()-top-bottom,
                x+left,y+top,w-left-right,h-top-bottom
                );
        //upper right
        drawImage(image,
                image.getWidth()-right,top,left,image.getHeight()-top-bottom,
                x+w-right,y+top,right,h-top-bottom
                );

        //bottom left
        drawImage(image,
                0,image.getHeight()-bottom,left,bottom,
                x,y+h-bottom,left,bottom
                );
        //bottom center
        drawImage(image,
                left,image.getHeight()-bottom,image.getWidth()-left-right,bottom,
                x+left,y+h-bottom,w-left-right,bottom
                );
        //bottom right
        drawImage(image,
                left,image.getHeight()-bottom,image.getWidth()-left-right,bottom,
                x+w-right,y+h-bottom,right,bottom
                );
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
