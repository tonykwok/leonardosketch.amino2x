package com.joshondesign.amino.jogl;


import com.joshondesign.amino.core.*;
import com.sun.opengl.util.awt.TextRenderer;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureCoords;

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
    private static Shader copyBufferShader;
    private Object defaultBuffer = new String("DEFAULT_BUFFER");
    private int WIDTH = 0;
    private int HEIGHT = 0;

    public JoglGFX(GLAutoDrawable drawable, int width, int height) {
        super();
        this.WIDTH = width;
        this.HEIGHT = height;
        this.drawable = drawable;
        gl = drawable.getGL().getGL2();
        if(copyBufferShader == null) {
            copyBufferShader = Shader.load(gl,
                    JoglGFX.class.getResource("shaders/PassThrough.vert"),
                    JoglGFX.class.getResource("shaders/SimpleTexture.frag")
            );
        }
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
        fillRect((float) x, (float) y, (float) w, (float) h);
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
        drawRect(x, y, w, h);
    }

    @Override
    public void fillRoundRect(int x, int y, int w, int h, int corner) {
        fillRect(x, y, w, h);
    }

    @Override
    public void drawEllipse(int x, int y, int w, int h) {
        drawRect(x, y, w, h);
    }

    @Override
    public void fillEllipse(int x, int y, int w, int h) {
        fillRect(x, y, w, h);
    }

    @Override
    public void drawCircle(int cx, int cy, int radius) {
        drawRect(cx - radius, cy - radius, radius * 2, radius * 2);
    }

    @Override
    public void fillCircle(int cx, int cy, int radius) {
        fillRect(cx - radius, cy - radius, radius * 2, radius * 2);
    }

    @Override
    public void drawImage(AminoImage image, int x, int y) {
        setColor(AminoColor.WHITE);
        Texture texture = ((JoglImage)image).getTexture();
        texture.enable();
        texture.bind();
        gl.glTranslated((double) x, (double) y, 0);

        TextureCoords tc = texture.getImageTexCoords();
        float mulH = tc.right();//paint.getWrapMultiplierH((int)w, (int)h);
        float mulV = tc.bottom();//paint.getWrapMultiplierV((int)w, (int)h);
        //u.p("drawing at: " + x + " " + y + " " + image.getWidth() + " " + image.getHeight());
        //u.p("mul = " + mulH + " " + mulV);
        gl.glBegin(GL2.GL_QUADS);
            gl.glTexCoord2f(0f, 0f);      gl.glVertex2f(0, 0);
            gl.glTexCoord2f(0f, mulV);    gl.glVertex2f(0, image.getHeight());
            gl.glTexCoord2f(mulH, mulV);  gl.glVertex2f(image.getWidth(), image.getHeight());
            gl.glTexCoord2f(mulH, 0f);    gl.glVertex2f(image.getWidth(), 0);
        gl.glEnd();
        gl.glTranslated(-(double)x,-(double)y,0);
        texture.disable();
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
        //this.color = AminoColor.WHITE;
        rend.setColor(this.color.getRed()/255.0f,this.color.getGreen()/255.0f,this.color.getBlue()/255.0f,this.color.getAlpha()/255.0f);
        rend.draw(s, (int)(x+translateX), drawable.getHeight() - y);
        rend.endRendering();
    }

    @Override
    public void translate(double x, double y) {
        this.translateX += x;
        this.translateY += y;
        gl.glTranslated(x, y, 0);
    }

    @Override
    public void rotate(double rotate, double anchorX, double anchorY, Transform.Axis axis) {
        switch (axis) {
            case X: gl.glRotated(rotate*180f/Math.PI,1,0,0); break;
            case Y: gl.glRotated(rotate*180f/Math.PI,0,1,0); break;
            case Z: gl.glRotated(rotate*180f/Math.PI,0,0,1); break;
        }
        //gl.glRotated(rotate*180f/Math.PI,0,0,1);
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void scale(double x, double y) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public GL2 getGL() {
        return gl;
    }

    public void copyBuffer(Rect sourceRect, FrameBufferObject sourceBuffer, Rect targetRect, FrameBufferObject targetBuffer) {
        gl.glPushMatrix();
        //gl.glTranslated(targetRect.x,targetRect.y,0);
        renderBufferWithShader(copyBufferShader, sourceBuffer, targetBuffer);
        gl.glPopMatrix();
    }

    private void renderBufferWithShader(Shader shader, FrameBufferObject source, FrameBufferObject target) {
        //p("rendering from a buffer with a shader");


        //set the render target, if needed
        if(target != null && target != getDefaultBuffer()) {
            //p("rendering to a buffer");
            gl.glPushMatrix();
            gl.glTranslated(-2,HEIGHT,0);
            //gl.glTranslated(0,0,0);
            gl.glScaled(1,-1,1);
            FrameBufferObject tbuf = target;
            tbuf.bind(gl);
        }

        //turn on the shader
        shader.use(gl);
        gl.glActiveTexture(GL2.GL_TEXTURE0);
        gl.glBindTexture(GL2.GL_TEXTURE_2D, source.colorId);
        shader.setIntParameter(gl, "tex0", 0);

        //draw
        gl.glBegin(GL2.GL_QUADS);
            gl.glTexCoord2f(0f, 0f); gl.glVertex2f(0, 0);
            gl.glTexCoord2f(0f, 1f); gl.glVertex2f(0, source.getHeight());
            gl.glTexCoord2f(1f, 1f); gl.glVertex2f( source.getWidth(), source.getHeight() );
            gl.glTexCoord2f( 1f, 0f ); gl.glVertex2f(source.getWidth(), 0);
        gl.glEnd();


        //turn off the shader again
        gl.glUseProgramObjectARB(0);
        gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
        gl.glActiveTexture(GL2.GL_TEXTURE0);
        gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);


        ///debugging

        if(false) {
            QRect rect = QRect.build(0, 0, 100, 100);
            float r = 0; float g = 1.0f; float b = 0.5f; float a = 0.2f;
            gl.glBegin(GL2.GL_QUADS);
                gl.glColor4f(r,g,b,a); gl.glVertex2f(rect.x, rect.y);
                gl.glColor4f(r,g,b,a); gl.glVertex2f(rect.x, rect.y+rect.h);
                gl.glColor4f(r,g,b,a); gl.glVertex2f(rect.x+rect.w, rect.y+rect.h);
                gl.glColor4f(r,g,b,a); gl.glVertex2f(rect.x+rect.w, rect.y);
            gl.glEnd();
        }


        //restore the render target, if needed
        if(target != null && target != getDefaultBuffer()) {
            gl.glPopMatrix();
            gl.glBindFramebuffer(GL2.GL_FRAMEBUFFER, 0);
            gl.glViewport(0, 0, WIDTH, HEIGHT);
        }


    }

    public FrameBufferObject createBuffer(int width, int height) {
        FrameBufferObject buffer1 = FrameBufferObject.create(gl, width, height, false);
        buffer1.bind(gl);
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        buffer1.clear(gl);
        gl.glBindFramebuffer(GL2.GL_FRAMEBUFFER, 0);
        return buffer1;
    }

    public Object getDefaultBuffer() {
        return defaultBuffer;
    }

    public void fillRect(QRect rect, AminoPaint fill, FrameBufferObject target, QRect clip) {
        float r = 0;
        float g = 0;
        float b = 0;
        if(fill instanceof AminoColor) {
            AminoColor c = (AminoColor) fill;
            r = (float) c.getRed()/255f;
            g = (float) c.getGreen()/255f;
            b = (float) c.getBlue()/255f;
        }

        //turn on alt buffer
        if(target != null && target != getDefaultBuffer()) {
            gl.glPushMatrix();
            gl.glTranslated(0,HEIGHT,0);
            gl.glScaled(1,-1,1);
            FrameBufferObject tbuf = target;
            tbuf.bind(gl);
        }


        gl.glBegin(GL2.GL_QUADS);
            gl.glColor3f(r,g,b); gl.glVertex2f(rect.x, rect.y);
            gl.glColor3f(r,g,b); gl.glVertex2f(rect.x, rect.y + rect.h);
            gl.glColor3f(r,g,b); gl.glVertex2f(rect.x + rect.w, rect.y + rect.h);
            gl.glColor3f(r, g, b); gl.glVertex2f(rect.x+rect.w, rect.y);
        gl.glEnd();


        //turn off alt buffer
        if(target != null && target != getDefaultBuffer()) {
            gl.glPopMatrix();
            gl.glBindFramebuffer(GL2.GL_FRAMEBUFFER, 0);
            gl.glViewport(0, 0, WIDTH, HEIGHT);
        }

    }

    public void fillRect2(QRect rect, AminoPaint fill, QRect clip) {
        float r = 0;
        float g = 0;
        float b = 0;
        if(fill instanceof AminoColor) {
            AminoColor c = (AminoColor) fill;
            r = (float) c.getRed()/255f;
            g = (float) c.getGreen()/255f;
            b = (float) c.getBlue()/255f;
        }

        gl.glBegin(GL2.GL_QUADS);
            gl.glColor3f(r,g,b); gl.glVertex2f(rect.x, rect.y);
            gl.glColor3f(r,g,b); gl.glVertex2f(rect.x, rect.y + rect.h);
            gl.glColor3f(r,g,b); gl.glVertex2f(rect.x + rect.w, rect.y + rect.h);
            gl.glColor3f(r, g, b); gl.glVertex2f(rect.x+rect.w, rect.y);
        gl.glEnd();


    }

    public void setTargetBuffer(FrameBufferObject target) {
        if(target != null) {
            //set to real target buffer
            gl.glPushMatrix();
            gl.glTranslated(0,HEIGHT,0);
            gl.glScaled(1,-1,1);
            FrameBufferObject tbuf = target;
            tbuf.bind(gl);
        } else {
            //set back to the screen
            gl.glPopMatrix();
            gl.glBindFramebuffer(GL2.GL_FRAMEBUFFER, 0);
            gl.glViewport(0, 0, WIDTH, HEIGHT);
        }
    }
}
