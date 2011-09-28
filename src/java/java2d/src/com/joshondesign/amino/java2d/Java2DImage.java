package com.joshondesign.amino.java2d;

import com.joshondesign.amino.core.AminoImage;
import com.joshondesign.amino.core.u;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: josh
 * Date: 9/19/11
 * Time: 3:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class Java2DImage extends AminoImage {
    BufferedImage _image;

    public Java2DImage(File file) throws IOException {
        u.p("trying to read image: " + file.getAbsolutePath());
        u.p("exists = " + file.exists());
        this._image = ImageIO.read(file);
    }

    public Java2DImage(URL url) throws IOException {
        u.p("trying to read image: " + url);
        this._image = ImageIO.read(url);
    }

    @Override
    public int getWidth() {
        return this._image.getWidth();
    }

    @Override
    public int getHeight() {
        return this._image.getHeight();
    }
}
