package com.joshondesign.amino.sdl;

import com.joshondesign.amino.core.Core;
import com.joshondesign.amino.core.Window;
import com.joshondesign.sdljava.SDL_Surface;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: josh
 * Date: 9/19/11
 * Time: 12:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class SDLWindow extends Window {
    private Core core;
    SDL_Surface surface;

    public SDLWindow(Core core, SDL_Surface surface) {
        super();
        this.core = core;
        this.surface = surface;
    }

    public Color getBackgroundFill() {
        return backgroundFill;
    }

}
