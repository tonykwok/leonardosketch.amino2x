package com.joshondesign.amino.sdl;

import com.joshondesign.amino.core.AminoImage;
import com.joshondesign.sdljava.SDL;
import com.joshondesign.sdljava.SDL_Surface;

import java.io.File;

import static com.joshondesign.sdljava.SDLUtil.p;

/**
 * Created by IntelliJ IDEA.
 * User: josh
 * Date: 9/19/11
 * Time: 3:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class SDLImage extends AminoImage {
    SDL_Surface _image;

    public SDLImage(File file) {
        p("trying to load image file: " + file.getAbsolutePath());
        p("exists = " + file.exists());
        SDL_Surface image = SDL.IMG_Load(file.getAbsolutePath());
        this._image = image;
    }

    @Override
    public int getWidth() {
        return this._image.getW();
    }

    @Override
    public int getHeight() {
        return this._image.getH();
    }
}
