package com.joshondesign.amino.jogl;

import com.joshondesign.amino.core.AminoFont;
import com.sun.opengl.util.awt.TextRenderer;

import java.awt.*;
import java.io.File;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: josh
 * Date: 9/25/11
 * Time: 8:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class JoglFont extends AminoFont {
    private Object resource;
    private double size = 12;
    private TextRenderer _renderer;

    public JoglFont(Object resource, double size) {
        this.resource = resource;
        this.size = size;
    }

    @Override
    public AminoFont withSize(double size) {
        return new JoglFont(this.resource,size);
    }

    public TextRenderer getText() {
        if(_renderer == null) {
            Font _font = null;
            if(resource instanceof File) {
                try {
                    _font = Font.createFont(Font.TRUETYPE_FONT, (File)resource).deriveFont((float) size);
                } catch (Exception e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
            if(resource instanceof URL) {
                try {
                    _font = Font.createFont(Font.TRUETYPE_FONT, ((URL)resource).openStream()).deriveFont((float) size);
                } catch (Exception e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
            _renderer = new TextRenderer(_font,true,true);
        }
        return _renderer;
    }
}
