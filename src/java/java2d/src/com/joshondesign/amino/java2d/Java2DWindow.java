package com.joshondesign.amino.java2d;

import com.joshondesign.amino.core.GFX;
import com.joshondesign.amino.core.Window;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: josh
 * Date: 9/19/11
 * Time: 1:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class Java2DWindow extends Window {
    JFrame _frame;
    JComponent _rootcomp;

    public Java2DWindow(int width, int height) {
        this._frame = new JFrame();
        this._frame.setSize(width, height);
        this._frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this._rootcomp = new JComponent() {
            @Override
            protected void paintComponent(Graphics graphics) {
                Graphics2D gfx = (Graphics2D) graphics;
                _update(gfx, this);
            }
        };
        this._frame.add(_rootcomp);

    }

    private void _update(Graphics2D graphics2D, JComponent jComponent) {
        GFX ctx = new Java2DGFX(graphics2D);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        ctx.setPaint(backgroundFill);
        ctx.fillRect(0,0,jComponent.getWidth(),jComponent.getHeight());
        //draw the scene
        root.draw(ctx);
    }


}
