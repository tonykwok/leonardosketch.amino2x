package com.joshondesign.amino.jogl;

import com.sun.opengl.util.texture.Texture;

/**
 * Created by IntelliJ IDEA.
 * User: josh
 * Date: 9/24/11
 * Time: 12:18 PM
 * To change this template use File | Settings | File Templates.
 */
public interface TextureProviderPaint {
    public Texture getTexture();

    float getWrapMultiplierH(int w, int h);
    float getWrapMultiplierV(int w, int h);
}
