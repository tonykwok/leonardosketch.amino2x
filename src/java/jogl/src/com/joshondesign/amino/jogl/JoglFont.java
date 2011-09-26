package com.joshondesign.amino.jogl;

import com.joshondesign.amino.core.AminoFont;
import com.sun.opengl.util.awt.TextRenderer;

import java.awt.*;
import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: josh
 * Date: 9/25/11
 * Time: 8:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class JoglFont extends AminoFont {
    private File file;
    private double size = 12;
    private TextRenderer _renderer;

    public JoglFont(File resource, double size) {
        this.file = resource;
        this.size = size;
    }

    @Override
    public AminoFont withSize(double size) {
        return new JoglFont(this.file,size);
    }

    public TextRenderer getText() {
        if(_renderer == null) {
            _renderer = new TextRenderer(new Font("SansSerif",Font.PLAIN, (int) this.size),true,true);
        }
        return _renderer;
    }
}
