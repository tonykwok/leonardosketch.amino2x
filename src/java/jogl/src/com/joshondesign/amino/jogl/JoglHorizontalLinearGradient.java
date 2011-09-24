package com.joshondesign.amino.jogl;

import com.joshondesign.amino.core.LinearGradient;
import com.joshondesign.amino.core.u;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.awt.AWTTextureIO;

import javax.media.opengl.GL2;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by IntelliJ IDEA.
 * User: josh
 * Date: 9/24/11
 * Time: 12:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class JoglHorizontalLinearGradient extends LinearGradient implements TextureProviderPaint {
    private int start;
    private int end;
    private Texture _texture;

    public JoglHorizontalLinearGradient(int start, int end) {
        this.start = start;
        this.end = end;
    }
    public Texture getTexture() {
        if(_texture == null) {
            float[] vs = new float[this.values.size()];
            Color[] cs = new Color[this.values.size()];

            for(int i=0; i<vs.length; i++) {
                vs[i] = this.values.get(i).floatValue();
                cs[i] = JoglVerticalLinearGradient.toAWTColor(this.colors.get(i));
            }
            Paint paint = new java.awt.LinearGradientPaint(new Point(start,0), new Point(end,0),vs,cs, MultipleGradientPaint.CycleMethod.REPEAT);
            BufferedImage bi = new BufferedImage(end-start,1,BufferedImage.TYPE_INT_ARGB);
            u.p("size = " + bi.getWidth() + " " + bi.getHeight());
            Graphics2D g2 = bi.createGraphics();
            g2.setPaint(paint);
            g2.fillRect(0, 0, bi.getWidth(), bi.getHeight());
            g2.dispose();
            _texture = AWTTextureIO.newTexture(bi, false);
            _texture.setTexParameteri(GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
        }
        return _texture;
    }

    public float getWrapMultiplierH(int w, int h) {
        return ((float)w)/((float)(end-start));
    }

    public float getWrapMultiplierV(int w, int h) {
        return 1f;
    }
}
