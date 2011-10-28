package com.joshondesign.amino.jogl.test;

import com.joshondesign.amino.core.*;
import com.joshondesign.amino.jogl.FrameBufferObject;
import com.joshondesign.amino.jogl.JoglGFX;
import com.joshondesign.amino.jogl.QRect;
import com.joshondesign.amino.jogl.Shader;

import javax.media.opengl.GL2;
import java.io.File;
import java.net.MalformedURLException;

/**
 * Created by IntelliJ IDEA.
 * User: josh
 * Date: 10/28/11
 * Time: 11:50 AM
 * To change this template use File | Settings | File Templates.
 */
public class JoglBufferStretchTest implements Core.InitCallback {
    private static final int WIDTH = 1100;
    private static final int HEIGHT = 950;

    private Window window;
    private Shader copyBufferShader;
    private FrameBufferObject buffer1;
    private FrameBufferObject buffer2;
    private Object defaultBuffer = new String("DEFAULT_BUFFER");
    private static final boolean BUFFER_DEBUG = false;

    public static void main(String ... args) {
        Core.init(new JoglBufferStretchTest());
    }

    public void call(Core core) throws Exception {
        window = core.createResizableWindow(WIDTH,HEIGHT+20);
        window.setBackgroundFill(AminoColor.BLACK);
        window.setRoot(new Node() {
            @Override
            public void draw(GFX gfx) {
                JoglGFX g = (JoglGFX) gfx;
                GL2 gl = g.getGL();
                JoglBufferStretchTest.this.draw(g,gl);
            }
        });

    }

    private void draw(JoglGFX g, GL2 gl) {
        if(copyBufferShader == null) {
            copyBufferShader = Shader.load(gl,
                    JoglGFX.class.getResource("shaders/PassThrough.vert"),
                    JoglGFX.class.getResource("shaders/SimpleTexture.frag")
            );
        }
        g.setPaint(AminoColor.RED);
        g.drawRect(100,100,50,50);

        if(buffer1 == null) {
            buffer1 = createBuffer(gl,WIDTH,HEIGHT);

        }
        if(buffer2 == null) {
            buffer2 = createBuffer(gl,WIDTH,HEIGHT);
        }


        // copy buffer2 to buffer1 w/ stretch
        gl.glPushMatrix();
        double cx = WIDTH/2;
        double cy = HEIGHT/2;
        gl.glTranslated(cx,cy,0);
        gl.glScaled(1.01, 1.01, 1);
        gl.glRotated(0.3,0,0,1);
        gl.glTranslated(-cx, -cy, 0);
        copyBuffer(gl,null,buffer2,null,buffer1);
        gl.glPopMatrix();

        // draw shapes to buffer1
        double x = Math.random()*WIDTH-100;
        double y = Math.random()*HEIGHT-100;
        fillRect(gl
                , QRect.build((int)x, (int)y, 100, 100)
                , AminoColor.fromRGB((float)Math.random(),0.1f,0.1f)
                , buffer1, null);

        // copy buffer1 to screen
        copyBuffer(gl, null, buffer1, null, null);

        // copy buffer1 to buffer2
        copyBuffer(gl,null,buffer1,null,buffer2);


    }
    public void fillRect(GL2 gl, QRect rect, AminoPaint fill, FrameBufferObject target, QRect clip) {
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

    public Shader loadShader(GL2 gl, File file) throws MalformedURLException {
        return Shader.load(gl,
        getClass().getResource("shaders/PassThrough.vert"),
                file.toURL()
                );
    }

    public void copyBuffer(GL2 gl, Rect sourceRect, FrameBufferObject sourceBuffer, Rect targetRect, FrameBufferObject targetBuffer) {
        gl.glPushMatrix();
        //gl.glTranslated(targetRect.x,targetRect.y,0);
        renderBufferWithShader(gl, copyBufferShader, sourceBuffer, targetBuffer);
        gl.glPopMatrix();
    }

    public static FrameBufferObject createBuffer(GL2 gl, int width, int height) {
        p("buffer created");
        FrameBufferObject buffer1 = FrameBufferObject.create(gl, width, height, false);
        buffer1.bind(gl);
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        buffer1.clear(gl);
        gl.glBindFramebuffer(GL2.GL_FRAMEBUFFER, 0);
        return buffer1;
    }

    private void renderBufferWithShader(GL2 gl, Shader shader, FrameBufferObject source, FrameBufferObject target) {
        //p("rendering from a buffer with a shader");


        //set the render target, if needed
        if(target != null && target != getDefaultBuffer()) {
            //p("rendering to a buffer");
            gl.glPushMatrix();
            gl.glTranslated(-2,HEIGHT-1.75,0);
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

        if(BUFFER_DEBUG) {
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

    private static void p(String s) {
        System.out.println(s);
    }

    public Object getDefaultBuffer() {
        return defaultBuffer;
    }
}
