package com.joshondesign.amino.jogl;

import com.joshondesign.amino.core.PatternPaint;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.awt.AWTTextureIO;

import javax.media.opengl.GL2;
import java.io.File;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: josh
 * Date: 9/24/11
 * Time: 11:52 AM
 * To change this template use File | Settings | File Templates.
 */
public class JoglPatternPaint extends PatternPaint implements TextureProviderPaint {
    private Texture _texture;
    private File file;

    public JoglPatternPaint(File file) throws IOException {
        this.file = file;
    }
    //        texture = AWTTextureIO.newTexture(file, false);

    public Texture getTexture() {
        if(_texture == null) {
            try {
                _texture = AWTTextureIO.newTexture(file,false);
                _texture.setTexParameteri(GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return _texture;
    }

    public float getWrapMultiplierH(int w, int h) {
        return ((float)w)/((float)(_texture.getWidth()));
    }

    public float getWrapMultiplierV(int w, int h) {
        return ((float)h)/((float)(_texture.getHeight()));
    }
}
