package com.joshondesign.amino.jogl.test;

import com.joshondesign.amino.core.AminoColor;
import com.joshondesign.amino.core.GFX;
import com.joshondesign.amino.core.u;
import com.joshondesign.amino.jogl.JoglGFX;
import com.sun.opengl.util.Animator;

import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by IntelliJ IDEA.
 * User: josh
 * Date: 10/27/11
 * Time: 7:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class JoglMixedTest implements GLEventListener {
    private static GLCanvas canvas;
    private static Frame frame;
    private static Animator animator;
    private AminoColor backgroundFill;
    int counter = 0;
    private GLU glu;

    public static void main(String ... args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                init();
            }
        });

    }

    private static void init() {
        GLProfile glp = GLProfile.get(GLProfile.GL2);
        GLCapabilities caps = new GLCapabilities(glp);

        //turn on fullscreen antialiasing
        caps.setSampleBuffers(true);
        caps.setNumSamples(4);
        canvas = new GLCanvas(caps);
        frame = new Frame("AWT Frame");
        frame.setSize(800,600);
        frame.add(canvas);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });

        canvas.addGLEventListener(new JoglMixedTest());
        //use a regular animator to let the OS handle frame rate throttling for us
        animator = new Animator(canvas);
        //animator = new FPSAnimator(canvas,60);
        animator.add(canvas);
        animator.start();
    }

    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        //use vsync to let the OS handle the framerate and reduce tearing
        gl.setSwapInterval(1);
        //various screen setup parameters
        //gl.glShadeModel(GL2.GL_SMOOTH);
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        gl.glClearDepth(1.0f);
        //gl.glEnable(GL2.GL_DEPTH_TEST);
        //gl.glDepthFunc(GL2.GL_LEQUAL);
        //gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
        //gl.glEnable(GL2.GL_TEXTURE_2D );

        //disable face culling, this lets us have clockwise and counter clockwise polys
        //gl.glEnable( GL_CULL_FACE );
        //gl.glCullFace(GL_BACK);


        //gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        gl.glEnable(GL2.GL_BLEND);
        gl.glBlendFunc(GL2.GL_SRC_ALPHA,GL2.GL_ONE_MINUS_SRC_ALPHA);


        u.p("jogl window created");
        glu = new GLU();
    }

    public void dispose(GLAutoDrawable drawable) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        int width = drawable.getWidth();
        int height = drawable.getHeight();

        backgroundFill = AminoColor.BLUE;
        float r = backgroundFill.getRed()/255.0f;
        float g = backgroundFill.getGreen()/255.0f;
        float b = backgroundFill.getBlue()/255.0f;

        //clear the background
        gl.glClearColor(r,g,b, 1.0f);
        //gl.glClearColor(1f,0.5f,0f,1f);
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);


        //gl.glDepthMask(false);
        //gl.glDisable(GL2.GL_LIGHTING);
        gl.glViewport(0, 0, width, height);


        //setup ortho view
        setupOrtho(gl, width, height);
        float size = 100;
        gl.glPushMatrix();
        gl.glTranslatef(400, 289, 0);
        gl.glRotatef((counter/5.0f)%360,0,0,1);
        drawGradientRect(gl, size);
        gl.glPopMatrix();


        //setup perspective view
        setupPerspective(gl,glu,width,height,100);
        size = (size/7);
        gl.glPushMatrix();
        gl.glRotatef((counter/5.0f)%360,0,1,1);
        drawGradientRect(gl,size);
        gl.glPopMatrix();






        //gl.glPushMatrix();
        //cb.draw(fx);
        //gl.glPopMatrix();
        //JoglFrame.viewPerspective(gl);

        GFX gfx = new JoglGFX(drawable,width,height);
        //window.root.draw(gfx);
        gfx.dispose();
        counter++;
    }

    private void drawGradientRect(GL2 gl, float size) {
        //gl.glPushMatrix();
        //gl.glRotatef(counter%360,0,0,1);

        gl.glBegin(GL2.GL_QUADS);
        gl.glColor3f(1,0,0);
        gl.glVertex2f(-size, -size);
        gl.glColor3f(1,1,0);
        gl.glVertex2f(-size,  size);
        gl.glColor3f(1,0,0);
        gl.glVertex2f( size,  size);
        gl.glColor3f(1,1,0);
        gl.glVertex2f( size, -size);
        gl.glEnd();
        //gl.glPopMatrix();
    }

    private void setupPerspective(GL2 gl, GLU glu, int width, int height, float distance) {
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        float widthHeightRatio = (float) width / (float) height;
        glu.gluPerspective(45, widthHeightRatio, 1, 1000);
        glu.gluLookAt(0, 0, distance, 0, 0, 0, 0, 1, 0);
    }

    private void setupOrtho(GL2 gl, int width, int height) {
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glPushMatrix();
        gl.glLoadIdentity();
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glPushMatrix();
        gl.glLoadIdentity();
        gl.glOrtho(0, width, height, 0, -1, 1);
        gl.glTranslated(2, 0, 0);
    }

    public void reshape(GLAutoDrawable drawable, int i, int i1, int i2, int i3) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
