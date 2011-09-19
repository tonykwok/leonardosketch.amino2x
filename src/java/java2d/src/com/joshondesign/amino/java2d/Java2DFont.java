package com.joshondesign.amino.java2d;

import com.joshondesign.amino.core.AminoFont;
import com.joshondesign.amino.core.u;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: josh
 * Date: 9/19/11
 * Time: 2:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class Java2DFont extends AminoFont {
    Font _font;

    public Java2DFont(URL resource) throws IOException, FontFormatException {
        _font = Font.createFont(Font.TRUETYPE_FONT, resource.openStream()).deriveFont(14f);
    }

    public Java2DFont(Font font) {
        _font = font;
    }

    public Java2DFont(File file) throws IOException, FontFormatException {
        _font = Font.createFont(Font.TRUETYPE_FONT, file).deriveFont(14f);
    }

    @Override
    public AminoFont withSize(double size) {
        return new Java2DFont(_font.deriveFont((float)size));
    }
}
