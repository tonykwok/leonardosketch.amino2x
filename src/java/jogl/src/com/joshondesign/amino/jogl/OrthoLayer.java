package com.joshondesign.amino.jogl;

import com.joshondesign.amino.core.GFX;
import com.joshondesign.amino.core.Node;
import com.joshondesign.amino.jogl.JoglGFX;

import javax.media.opengl.GL2;

/**
* Created by IntelliJ IDEA.
* User: josh
* Date: 10/27/11
* Time: 9:24 PM
* To change this template use File | Settings | File Templates.
*/
public class OrthoLayer extends Node {
    private Node child;

    @Override
    public void draw(GFX gfx) {
        JoglGFX g = (JoglGFX) gfx;
        GL2 gl = g.getGL();
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glPushMatrix();
        gl.glLoadIdentity();
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glPushMatrix();
        gl.glLoadIdentity();
        gl.glOrtho(0, 1024, 700, 0, -1, 1);
        gl.glTranslated(2,0,0);
        child.draw(gfx);
    }

    public Node setChild(Node shape) {
        this.child = shape;
        return this;
    }
}
