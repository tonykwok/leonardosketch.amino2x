package com.joshondesign.amino.jogl;

import com.joshondesign.amino.core.*;
import com.sun.opengl.util.awt.TextRenderer;
import com.sun.opengl.util.texture.Texture;

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
    private double translateX = 0;
    private double translateY = 0;

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
        gl.glBegin(GL2.GL_LINES);
        setColor(this.color);
        gl.glVertex2f(x1,y1);
        gl.glVertex2f(x2,y2);
        gl.glEnd();
    }

    @Override
    public void drawRect(int x, int y, int w, int h) {
        gl.glBegin(GL2.GL_LINES);
        setColor(this.color);
        gl.glVertex2f(x, y);
        gl.glVertex2f(x + w, y);
        gl.glVertex2f(x + w, y);
        gl.glVertex2f(x + w, y + h);
        gl.glVertex2f(x + w, y + h);
        gl.glVertex2f(x, y + h);
        gl.glVertex2f(x,y+h);
        gl.glVertex2f(x,y);
        gl.glEnd();
    }

    @Override
    public void fillRect(int x, int y, int w, int h) {
        fillRect((float)x,(float)y,(float)w,(float)h);
    }

    @Override
    public void fillRect(float x, float y, float w, float h) {
        if(this.paint instanceof TextureProviderPaint) {
            TextureProviderPaint paint = (TextureProviderPaint) this.paint;
            Texture texture = paint.getTexture();
            texture.enable();
            texture.bind();
            gl.glTranslated((double) x, (double) y, 0);

            float mulH = paint.getWrapMultiplierH((int)w, (int)h);
            float mulV = paint.getWrapMultiplierV((int)w, (int)h);
            gl.glBegin(GL2.GL_QUADS);
                gl.glTexCoord2f(0f, 0f); gl.glVertex2f(0, 0);
                gl.glTexCoord2f(0f, mulV); gl.glVertex2f(0, h);
                gl.glTexCoord2f(mulH, mulV); gl.glVertex2f(w, h );
                gl.glTexCoord2f(mulH, 0f); gl.glVertex2f(w, 0);
            gl.glEnd();
            gl.glTranslated(-(double)x,-(double)y,0);

        }

        if(this.paint instanceof AminoColor) {
            gl.glBegin(GL2.GL_QUADS);
                setColor(this.color); gl.glVertex2f(x, y);
                setColor(this.color); gl.glVertex2f(x, y+h);
                setColor(this.color); gl.glVertex2f(x+w, y+h);
                setColor(this.color); gl.glVertex2f(x + w, y);
            gl.glEnd();
        }
    }

    private void setColor(AminoColor color) {
        //gl.glColor4f(0.5f,0.5f,0.5f,0.5f);
        gl.glColor4f(
                color.getRed()/255f,
                color.getGreen()/255f,
                color.getBlue()/255f,
                color.getAlpha()/255f
        );
    }

    @Override
    public void drawRoundRect(int x, int y, int w, int h, int corner) {
        drawRect(x,y,w,h);
    }

    @Override
    public void fillRoundRect(int x, int y, int w, int h, int corner) {
        fillRect(x,y,w,h);
    }

    @Override
    public void drawEllipse(int x, int y, int w, int h) {
        drawRect(x,y,w,h);
    }

    @Override
    public void fillEllipse(int x, int y, int w, int h) {
        fillRect(x,y,w,h);
    }

    @Override
    public void drawCircle(int cx, int cy, int radius) {
        drawRect(cx-radius,cy-radius,radius*2,radius*2);
    }

    @Override
    public void fillCircle(int cx, int cy, int radius) {
        fillRect(cx - radius, cy - radius, radius * 2, radius * 2);
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
        if(!(font instanceof JoglFont)) return;
        JoglFont f = (JoglFont) font;
        TextRenderer rend = f.getText();
        rend.beginRendering(drawable.getWidth(), drawable.getHeight());
        rend.setColor(this.color.getRed()/255.0f,this.color.getGreen()/255.0f,this.color.getBlue()/255.0f,this.color.getAlpha()/255.0f);
        rend.draw(s, (int)(x+translateX), drawable.getHeight() - y);
        rend.endRendering();
    }

    @Override
    public void translate(double x, double y) {
        this.translateX += x;
        this.translateY += y;
        gl.glTranslated(x,y,0);
    }
}
