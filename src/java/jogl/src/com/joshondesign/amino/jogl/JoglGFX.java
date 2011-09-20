package com.joshondesign.amino.jogl;

import com.joshondesign.amino.core.*;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

/**
 * Created by IntelliJ IDEA.
 * User: josh
 * Date: 9/20/11
 * Time: 12:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class JoglGFX extends GFX {
    private GLAutoDrawable drawable;
    private AminoPaint paint = AminoColor.WHITE;
    private AminoColor color = AminoColor.WHITE;
    private GL2 gl;

    public JoglGFX(GLAutoDrawable drawable) {
        super();
        this.drawable = drawable;
        gl = drawable.getGL().getGL2();
    }

    @Override
    public void dispose() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setPaint(AminoPaint backgroundFill) {
        this.paint = backgroundFill;
        if(backgroundFill instanceof AminoColor) {
            this.color = (AminoColor) backgroundFill;
        }
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void drawRect(int x, int y, int w, int h) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void fillRect(int x, int y, int w, int h) {

        gl.glBegin(GL2.GL_QUADS);
            setColor(this.color); gl.glVertex2i(x, y);
            setColor(this.color); gl.glVertex2f(x, y+h);
            setColor(this.color); gl.glVertex2f(x+w, y+h);
            setColor(this.color); gl.glVertex2f(x+w, y);
        gl.glEnd();

    }

    private void setColor(AminoColor color) {
        //gl.glColor3f(0.5f,0.5f,0.5f);
        gl.glColor3f(
                color.getRed()/255f,
                color.getGreen()/255f,
                color.getBlue()/255f);
    }

    @Override
    public void drawRoundRect(int x, int y, int w, int h, int corner) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void fillRoundRect(int x, int y, int w, int h, int corner) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void drawEllipse(int x, int y, int w, int h) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void fillEllipse(int x, int y, int w, int h) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void drawCircle(int cx, int cy, int radius) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void fillCircle(int cx, int cy, int radius) {
        //To change body of implemented methods use File | Settings | File Templates.
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
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void translate(double x, double y) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
