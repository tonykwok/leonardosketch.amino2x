package com.joshondesign.amino.sdl;

import com.joshondesign.amino.core.PatternPaint;
import com.joshondesign.sdljava.SDL;
import com.joshondesign.sdljava.SDL_Surface;

import java.io.File;
import java.net.URL;

import static com.joshondesign.sdljava.SDLUtil.p;

/**
 * Created by IntelliJ IDEA.
 * User: josh
 * Date: 9/19/11
 * Time: 6:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class SDLPattern extends PatternPaint implements SDLSurfacePaint{
    private SDL_Surface image;

    public SDLPattern(File file) {
        p("trying to load image file: " + file.getAbsolutePath());
        p("exists = " + file.exists());
        this.image = SDL.IMG_Load(file.getAbsolutePath());
    }

    public SDLPattern(URL url) {
        p("WARNING: =========== SDLPattern from URL not implemented!!!! ==========");
    }

    public SDL_Surface getImage(SDL_Surface surface) {
        return this.image;
    }
}
