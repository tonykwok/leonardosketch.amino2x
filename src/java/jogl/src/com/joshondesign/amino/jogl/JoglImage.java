package com.joshondesign.amino.jogl;

import com.joshondesign.amino.core.AminoImage;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.awt.AWTTextureIO;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: josh
 * Date: 9/25/11
 * Time: 10:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class JoglImage extends AminoImage {
    //private File file;
    private BufferedImage image;
    private Texture _texture;

    public JoglImage(File file) throws IOException {
        //this.file = file;
        this.image = ImageIO.read(file);
    }

    public JoglImage(URL resource) throws IOException {
        this.image = ImageIO.read(resource);
    }

    @Override
    public int getWidth() {
        return this.image.getWidth();
    }

    @Override
    public int getHeight() {
        return this.image.getHeight();
    }

    public Texture getTexture() {
        if(_texture == null) {
            _texture = AWTTextureIO.newTexture(this.image,false);
        }
        return _texture;
    }
}
