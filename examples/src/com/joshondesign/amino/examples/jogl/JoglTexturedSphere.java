package com.joshondesign.amino.examples.jogl;

import com.joshondesign.amino.core.*;
import com.joshondesign.amino.jogl.JoglGFX;
import com.joshondesign.amino.jogl.JoglPatternPaint;
import com.joshondesign.amino.jogl.PerspectiveLayer;
import com.sun.opengl.util.texture.Texture;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

import static javax.media.opengl.GL2.*;

/**
 * Created by IntelliJ IDEA.
 * User: josh
 * Date: 10/29/11
 * Time: 7:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class JoglTexturedSphere implements Core.InitCallback {
    private Window window;
    private GLU glu;
    private JoglPatternPaint img;

    public static void main(String ... args) {
        Core.init(new JoglTexturedSphere());
    }

    public void call(Core core) throws Exception {
        img = (JoglPatternPaint) core.loadPattern(JoglTexturedSphere.class.getResource("earthmap1k.jpg"));
        //u.p("pattern = " + img);
        window = core.createResizableWindow(1024,700);
        window.setBackgroundFill(AminoColor.BLACK);
        window.setRoot(new PerspectiveLayer().setChild(new Node() {
            @Override
            public void draw(GFX gfx) {
                doDraw((JoglGFX)gfx);
            }
        }));
    }

    double counter = 0;
    private void doDraw(JoglGFX gfx) {
        GL2 gl = gfx.getGL();
        if(glu == null) {
            glu = new GLU();
        }
        //turn on depth testing
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glDepthFunc(GL.GL_LEQUAL);

        float SHINE_ALL_DIRECTIONS = 1;
        float[] lightPos = {-300, 80, 200, SHINE_ALL_DIRECTIONS};
        float[] lightColorAmbient = {0.2f, 0.2f, 0.2f, 1f};
        float[] lightColorSpecular = {0.8f, 0.8f, 0.8f, 1f};

        // Set light parameters.
        gl.glLightfv(GL_LIGHT1, GL_POSITION, lightPos, 0);
        gl.glLightfv(GL_LIGHT1, GL_AMBIENT, lightColorAmbient, 0);
        gl.glLightfv(GL_LIGHT1, GL_SPECULAR, lightColorSpecular, 0);

        // Enable lighting in GL.
        gl.glEnable(GL_LIGHT1);
        gl.glEnable(GL_LIGHTING);

        gl.glPushMatrix();
        gl.glRotated(counter%360,0,1,0);
        gl.glRotated(90,1,0,0);
        counter += 0.1;

        // Set material properties.
        float[] rgba = {1f, 1f, 1f};
        gl.glMaterialfv(GL.GL_FRONT, GL2.GL_AMBIENT, rgba, 0);
        gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SPECULAR, rgba, 0);
        gl.glMaterialf(GL.GL_FRONT, GL2.GL_SHININESS, 0.5f);


        Texture texture = img.getTexture();
        texture.enable();
        texture.bind();


        GLUquadric earth = glu.gluNewQuadric();
        glu.gluQuadricTexture(earth,true);
        glu.gluQuadricDrawStyle(earth, GLU.GLU_FILL);
        glu.gluQuadricNormals(earth,GLU.GLU_FLAT);
        glu.gluQuadricOrientation(earth,GLU.GLU_OUTSIDE);
        float radius = 20.378f;
        int slices = 64;
        int stacks = 64;
        glu.gluSphere(earth,radius,slices, stacks);
        glu.gluDeleteQuadric(earth);
        texture.disable();

        //gfx.setPaint(AminoColor.RED);
        //gfx.fillRect(1, 2, 3, 4);

        gl.glPopMatrix();
    }
}
