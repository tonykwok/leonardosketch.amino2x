package com.joshondesign.amino.jogl;

import com.joshondesign.amino.core.GFX;
import com.joshondesign.amino.core.Node;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

/**
* Created by IntelliJ IDEA.
* User: josh
* Date: 10/27/11
* Time: 9:24 PM
* To change this template use File | Settings | File Templates.
*/
public class PerspectiveLayer extends Node {
    private Node child;
    private GLU glu;

    public PerspectiveLayer() {
        this.glu = new GLU();
    }

    @Override
    public void draw(GFX gfx) {
        JoglGFX g = (JoglGFX) gfx;
        GL2 gl = g.getGL();
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        float widthHeightRatio = (float) 1024 / (float) 700;
        glu.gluPerspective(45, widthHeightRatio, 1, 1000);
        float distance = 100;
        glu.gluLookAt(0, 0, distance, 0, 0, 0, 0, 1, 0);
        child.draw(gfx);
    }
    public Node setChild(Node shape) {
        this.child = shape;
        return this;
    }
}
