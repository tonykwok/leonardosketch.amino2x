package com.joshondesign.amino.sdl;

import com.joshondesign.amino.core.AminoFont;
import com.joshondesign.amino.core.u;
import com.joshondesign.sdljava.SDL;
import com.joshondesign.sdljava.SWIGTYPE_p__TTF_Font;

import java.io.File;
import java.net.URL;

import static com.joshondesign.sdljava.SDLUtil.p;

public class SDLFont extends AminoFont {
    SWIGTYPE_p__TTF_Font _font;
    private File file;

    public SDLFont(URL resource) {
        u.p("resource = " + resource);
    }

    public SDLFont(File file) {
        this(file,12);
    }

    protected SDLFont(File file, int size) {
        p("trying to load _sdlfont: " + file.getAbsolutePath());
        if(SDL.TTF_Init() == -1) {
            p("TTF_Init failed: " + SDL.SDL_GetError());
        }
        _font = SDL.TTF_OpenFont(file.getAbsolutePath(), size);
        this.file = file;
    }

    @Override
    public AminoFont withSize(double size) {
        return new SDLFont(this.file,(int)size);
    }
}
