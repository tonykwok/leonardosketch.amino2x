package com.joshondesign.amino.jogl;

import com.joshondesign.amino.core.AminoImage;
import com.joshondesign.amino.core.u;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.awt.AWTTextureIO;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: josh
 * Date: 9/25/11
 * Time: 10:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class JoglImage extends AminoImage {
    private File file;
    private BufferedImage image;
    private Texture _texture;

    public JoglImage(File file) throws IOException {
        this.file = file;
        this.image = ImageIO.read(file);
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
            Graphics2D g = this.image.createGraphics();
            g.setPaint(Color.RED);
            g.fillRect(0,0,50,50);
            g.dispose();
            try {
                _texture = AWTTextureIO.newTexture(this.file,false);
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            u.p("created texture for image: " + this.image.getWidth() + " " + this.getHeight());
        }
        return _texture;
    }
}
