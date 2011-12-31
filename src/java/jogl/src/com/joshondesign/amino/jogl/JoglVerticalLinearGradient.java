package com.joshondesign.amino.jogl;

import com.joshondesign.amino.core.AminoColor;
import com.joshondesign.amino.core.LinearGradient;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;
import com.sun.opengl.util.texture.awt.AWTTextureIO;

import javax.media.opengl.GL2;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by IntelliJ IDEA.
 * User: josh
 * Date: 9/24/11
 * Time: 12:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class JoglVerticalLinearGradient extends LinearGradient implements TextureProviderPaint {
    private int start;
    private int end;
    private Texture _texture;

    public JoglVerticalLinearGradient(int start, int end) {
        this.start = start;
        this.end = end;
    }
    public Texture getTexture() {
        if(_texture == null) {
            float[] vs = new float[this.values.size()];
            Color[] cs = new Color[this.values.size()];

            for(int i=0; i<vs.length; i++) {
                vs[i] = this.values.get(i).floatValue();
                cs[i] = toAWTColor(this.colors.get(i));
            }
            Paint paint = new java.awt.LinearGradientPaint(new Point(0,start), new Point(0,end),vs,cs, MultipleGradientPaint.CycleMethod.REPEAT);
            BufferedImage bi = new BufferedImage(1,end-start,BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = bi.createGraphics();
            g2.setPaint(paint);
            g2.fillRect(0, 0, bi.getWidth(), bi.getHeight());
            g2.dispose();
            _texture = AWTTextureIO.newTexture(bi,false);
            _texture.setTexParameteri(GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
        }
        return _texture;
    }

    public float getWrapMultiplierH(int w, int h) {
        return 1;
    }

    public float getWrapMultiplierV(int w, int h) {
        return ((float)h)/((float)(end-start));
    }

    public static Color toAWTColor(AminoColor aminoColor) {
        return new Color(aminoColor.getRed(),aminoColor.getGreen(),aminoColor.getBlue(),aminoColor.getAlpha());
    }
}
