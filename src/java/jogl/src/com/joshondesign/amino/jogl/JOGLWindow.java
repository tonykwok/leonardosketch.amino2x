package com.joshondesign.amino.jogl;

import com.joshondesign.amino.core.Core;
import com.joshondesign.amino.core.GFX;
import com.joshondesign.amino.core.Window;
import com.joshondesign.amino.core.u;
import com.sun.opengl.util.Animator;

import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by IntelliJ IDEA.
 * User: josh
 * Date: 9/20/11
 * Time: 12:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class JOGLWindow extends Window {
    private GLCanvas canvas;
    private Frame frame;
    private Core core;
    private int width;
    private int height;
    private Animator animator;

    public JOGLWindow(Core core, int width, int height) {
        this.core = core;
        this.width = width;
        this.height = height;
        GLProfile glp = GLProfile.get(GLProfile.GL2);
        GLCapabilities caps = new GLCapabilities(glp);
        canvas = new GLCanvas(caps);

        frame = new Frame("AWT Frame");
        frame.setSize(this.width,this.height);
        frame.add(canvas);
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });



    }

    public void start() {
        canvas.addGLEventListener(new JoglHandler(this));
        //use a regular animator to let the OS handle frame rate throttling for us
        animator = new Animator(canvas);
        animator.add(canvas);
        animator.start();
    }

    private static void viewOrtho(GL2 gl, int w, int h) {
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glPushMatrix();
        gl.glLoadIdentity();
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glPushMatrix();
        gl.glLoadIdentity();
        gl.glOrtho(0, w, h, 0, -1, 1);
        gl.glTranslated(2,0,0);
    }

    @Override
    public int getWidth() {
        return this.frame.getWidth();
    }

    @Override
    public int getHeight() {
        return this.frame.getHeight();
    }

    private static class JoglHandler implements GLEventListener {
        private JOGLWindow window;

        private JoglHandler(JOGLWindow joglWindow) {
            this.window = joglWindow;
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


            //width = drawable.getWidth();
            //height = drawable.getHeight();
            //fx = new DelegatingFullGfx(new JoglGfx(this, gl));
            //gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            //gl.glEnable(GL2.GL_BLEND);
            //gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_DST_ALPHA);
            //gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);

            u.p("jogl window created");
        }

        public void dispose(GLAutoDrawable glAutoDrawable) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        int counter = 0;
        public void display(GLAutoDrawable drawable) {
            GL2 gl = drawable.getGL().getGL2();
            int width = drawable.getWidth();
            int height = drawable.getHeight();

            float r = window.backgroundFill.getRed()/255.0f;
            float g = window.backgroundFill.getGreen()/255.0f;
            float b = window.backgroundFill.getBlue()/255.0f;

            //clear the background
            gl.glClearColor(r,g,b, 1.0f);
            //gl.glClearColor(1f,0.5f,0f,1f);
            gl.glClear(GL2.GL_COLOR_BUFFER_BIT);


            //gl.glDepthMask(false);
            //gl.glDisable(GL2.GL_LIGHTING);
            gl.glViewport(0, 0, width, height);


            gl.glMatrixMode(GL2.GL_MODELVIEW);
            gl.glPushMatrix();
            gl.glLoadIdentity();
            gl.glMatrixMode(GL2.GL_PROJECTION);
            gl.glPushMatrix();
            gl.glLoadIdentity();
            gl.glOrtho(0, width, height, 0, -1, 1);
            gl.glTranslated(2, 0, 0);


            /*
            float right = 50+counter;
            gl.glBegin(GL2.GL_QUADS);
                gl.glColor3f(1,0,0); gl.glVertex2f(0, 0);
                gl.glColor3f(1,0,0); gl.glVertex2f(0, 100);
                gl.glColor3f(1,0,0); gl.glVertex2f(right, 100);
                gl.glColor3f(1,0,0); gl.glVertex2f(right, 0);
            gl.glEnd();
            */

            //gl.glPushMatrix();
            //cb.draw(fx);
            //gl.glPopMatrix();
            //JoglFrame.viewPerspective(gl);

            GFX gfx = new JoglGFX(drawable);
            window.root.draw(gfx);
            gfx.dispose();
            counter++;
        }

        public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }
}
