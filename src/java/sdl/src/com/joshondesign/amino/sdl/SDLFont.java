package com.joshondesign.amino.sdl;

import com.joshondesign.amino.core.AminoFont;
import com.joshondesign.amino.core.u;
import com.joshondesign.sdljava.SDL;
import com.joshondesign.sdljava.SWIGTYPE_p__TTF_Font;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

import static com.joshondesign.sdljava.SDLUtil.p;

public class SDLFont extends AminoFont {
    SWIGTYPE_p__TTF_Font _font;
    private File file;

    public SDLFont(URL resource) {
        this(resource, 12);
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

    protected SDLFont(URL url, int size) {
        p("trying to load _sdlfont: " + url.toString());
        try {
            File file = File.createTempFile("sdltest","foo");
            FileOutputStream fout = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            InputStream in = url.openStream();
            while(true) {
                int n = in.read(buf);
                if(n <0)break;
                fout.write(buf,0,n);
            }
            fout.close();
            if(SDL.TTF_Init() == -1) {
                p("TTF_Init failed: " + SDL.SDL_GetError());
            }
            u.p("reading from tempfile: " + file.getAbsolutePath());
            _font = SDL.TTF_OpenFont(file.getAbsolutePath(), size);
            this.file = file;
            file.deleteOnExit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public SDLFont(String fontName) {
        u.p("WARNING: SDLFont does not support loading by system font name");
    }

    @Override
    public AminoFont withSize(double size) {
        return new SDLFont(this.file,(int)size);
    }
}
