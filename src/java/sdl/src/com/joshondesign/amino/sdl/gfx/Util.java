package com.joshondesign.amino.sdl.gfx;

import com.joshondesign.amino.core.AminoException;
import com.joshondesign.amino.sdl.SDLException;
import com.joshondesign.sdljava.*;

public class Util {
    public static final boolean AVIAN_WORKAROUND = true;

    public static void p(String s) {
        System.out.println(s);
    }

    public static SDL_Rect rect(int x, int y, int w, int h) {
        SDL_Rect rect = new SDL_Rect();
        rect.setX((short) x);
        rect.setY((short) y);
        rect.setW(w);
        rect.setH(h);
        return rect;
    }

    public static void dump(SDL_VideoInfo info) {
        p("hw avail = " + info.getHw_available());
        p("wm avail = " + info.getWm_available());
        p("screen size = " + info.getCurrent_w() + " " + info.getCurrent_h());
        p("blit fill = " + info.getBlit_fill());
        p("blit hw = " + info.getBlit_hw());
        p("blit sw =  " + info.getBlit_sw());
        p("video mem = " + info.getVideo_mem());
        p("bpp = " + info.getVfmt().getBitsPerPixel());
        p("Bpp = " + info.getVfmt().getBytesPerPixel());
        p("alpha = " + info.getVfmt().getAlpha());
    }

    public static void dump(SDL_Surface surface) {
        p("--------");
        p("_surface: " + Integer.toBinaryString((int) surface.getFlags()));

        p("sw = " +(SDLConstants.SDL_SWSURFACE & surface.getFlags()));
        p("hw = " +(SDLConstants.SDL_HWSURFACE & surface.getFlags()));
        p("anyformat = " +(SDLConstants.SDL_ANYFORMAT & surface.getFlags()));
        p("async = " +(SDLConstants.SDL_ASYNCBLIT & surface.getFlags()));
        p("hw pallete = " +(SDLConstants.SDL_HWPALETTE & surface.getFlags()));

        p("double = " +(SDLConstants.SDL_DOUBLEBUF & surface.getFlags()));
        p("full = " +(SDLConstants.SDL_FULLSCREEN & surface.getFlags()));
        p("opengl = " +(SDLConstants.SDL_OPENGL & surface.getFlags()));
        p("openglblit = " +((SDLConstants.SDL_OPENGLBLIT & surface.getFlags())!=0));

        p("hwaccel = " +((SDLConstants.SDL_HWACCEL & surface.getFlags())!=0));
        p("src _sdlcolor key = " +(SDLConstants.SDL_SRCCOLORKEY & surface.getFlags()));
        p("rle accel = " +(SDLConstants.SDL_RLEACCEL & surface.getFlags()));
        p("src alpha = " +(SDLConstants.SDL_SRCALPHA & surface.getFlags()));
        p("prealloc = " +(SDLConstants.SDL_PREALLOC & surface.getFlags()));

        p("resize = " +((SDLConstants.SDL_RESIZABLE & surface.getFlags())!=0));
        p("noframe = " +(SDLConstants.SDL_NOFRAME & surface.getFlags()));

        p("-- masks");
        p("alpha mask = " + Integer.toBinaryString((int) surface.getFormat().getAmask()));
        p("red   mask = " + Integer.toBinaryString((int) surface.getFormat().getRmask()));
        p("green mask = " + Integer.toBinaryString((int) surface.getFormat().getGmask()));
        p("blue  mask = " + Integer.toBinaryString((int) surface.getFormat().getBmask()));

    }


    public static void standard_init() throws AminoException {
        if(SDL.SDL_Init(SDL.SDL_INIT_VIDEO | SDLConstants.SDL_INIT_TIMER) < 0) {
            throw new AminoException("init failed");
        }
    }

    public static SDL_Surface createFullscreenWindow() throws AminoException {
        SDL_VideoInfo info = SDL.SDL_GetVideoInfo();
        SDL_Surface surface = SDL.SDL_SetVideoMode(
                0,0
                , info.getVfmt().getBitsPerPixel()
                , SDLConstants.SDL_FULLSCREEN);
        if (surface == null) {
            p("couldn't create a _surface");
            throw new AminoException("couldn't create a _surface");
        }
        return surface;
    }
    public static SDL_Surface createResizableWindow(int w, int h) throws AminoException {
        if("arm".equals(System.getProperty("os.arch"))) {
            return createFullscreenWindow();
        }

        SDL_Surface surface = SDL.SDL_SetVideoMode(w,h, 32,
                SDLConstants.SDL_ANYFORMAT | SDLConstants.SDL_RESIZABLE);
        if (surface == null) {
            p("couldn't create a _surface");
            throw new AminoException("couldn't create a _surface");
        }
        return surface;
    }




    public static void dump(SDL_Event event) {
        p("event type = " + event.getType());
        int type = event.getType();
        if(type == SDL_EventType.SDL_ACTIVEEVENT) p("SDL_ACTIVEEVENT");

        if(type == SDL_EventType.SDL_MOUSEMOTION) p("SDL_MOUSEMOTION");
        if(type == SDL_EventType.SDL_MOUSEBUTTONDOWN) p("SDL_MOUSEBUTTONDOWN");
        if(type == SDL_EventType.SDL_MOUSEBUTTONUP) p("SDL_MOUSEBUTTONUP");

        if(type == SDL_EventType.SDL_NOEVENT) p("SDL_NOEVENT");
        if(type == SDL_EventType.SDL_NUMEVENTS) p("SDL_NUMEVENTS");

        if(type == SDL_EventType.SDL_QUIT) p("SDL_QUIT");
        if(type == SDL_EventType.SDL_SYSWMEVENT) p("SDL_SYSWMEVENT");
        if(type == SDL_EventType.SDL_USEREVENT) p("SDL_USEREVENT");

        if(type == SDL_EventType.SDL_VIDEOEXPOSE) p("SDL_VIDEOEXPOSE");
        if(type == SDL_EventType.SDL_VIDEORESIZE) p("SDL_VIDEORESIZE");

        if(type == SDL_EventType.SDL_KEYDOWN) {
            String key = SDL.SDL_GetKeyName(event.getKey().getKeysym().getSym());
            p("SDL_KEYDOWN  key='" + key
                    + "' scancode = '"
                    + event.getKey().getKeysym().getScancode()+"'"
                    + "\n     unicode = " + event.getKey().getKeysym().getUnicode()
                    + "  mod = " + event.getKey().getKeysym().getMod()
            );
            p("state = " + event.getKey().getState());
            p("type = " + event.getKey().getType());
        }
    }
    /*
    public static void debug_WaitForQuit(Callback callback) {
        SDL_Event event = new SDL_Event();
        while(true) {
            SDL.SDL_PollEvent(event);
            //if(event.getType() == SDL_EventType.SDL_MOUSEMOTION) continue;
            if(event.getType() == SDL_EventType.SDL_QUIT) { break; }
            if(event.getType() == SDL_EventType.SDL_KEYDOWN) {
                String key = SDL.SDL_GetKeyName(event.getKey().getKeysym().getSym());
                if("q".equals(key)) break;
            }
            if(event.getType() == SDL_EventType.SDL_MOUSEBUTTONDOWN) { break; }
            if(callback != null) {
                callback.call(null);
            }
        }

        SDL.SDL_Quit();
        p("quit SDL");
    }
      */
    public static void warn(String s) {
        p("WARNING:   " + s);
    }
}
