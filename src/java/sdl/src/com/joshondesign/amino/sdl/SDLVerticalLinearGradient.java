package com.joshondesign.amino.sdl;

import com.joshondesign.amino.core.AminoColor;
import com.joshondesign.amino.core.LinearGradient;
import com.joshondesign.sdljava.SDL;
import com.joshondesign.sdljava.SDLConstants;
import com.joshondesign.sdljava.SDL_PixelFormat;
import com.joshondesign.sdljava.SDL_Surface;

import java.awt.*;

import static com.joshondesign.amino.sdl.SDLGFX.interpolate;

/**
 * Created by IntelliJ IDEA.
 * User: josh
 * Date: 9/19/11
 * Time: 3:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class SDLVerticalLinearGradient extends LinearGradient implements SDLSurfacePaint {
    private int start;
    private int end;
    private SDL_Surface _texture;

    public SDLVerticalLinearGradient(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public SDL_Surface getImage(SDL_Surface surface) {
        if(_texture == null) {
            double length = this.end-this.start;
            SDL_PixelFormat format = surface.getFormat();
            SDL_Surface surface2 = SDL.SDL_CreateRGBSurface(SDLConstants.SDL_SWSURFACE,
                    1, (int) length,
                    format.getBitsPerPixel(), format.getRmask(), format.getGmask(), format.getBmask(), format.getAmask());
            format.delete();

            double v = values.get(0);
            AminoColor c = colors.get(0);
            int currentIndex = 0;
            //for each span
            for(int i=1; i<values.size(); i++) {
                double nv = values.get(i);
                AminoColor nc = colors.get(i);
                double spanFract = nv-v;
                int spanLength = (int) (length*spanFract);

                for(int j=0; j<spanLength; j++) {
                    double colorFraction = ((double)j)/spanLength;
                    AminoColor finalColor = interpolate(c,nc,colorFraction);
                    int sdlcolor = (finalColor.getBlue()<<24) | (finalColor.getGreen() << 16) | (finalColor.getRed()<<8) | 0xFF;
                    SDL.setPixel(surface2,0,currentIndex+j,sdlcolor);
                }
                currentIndex += spanLength;

                v = nv;
                c = nc;
            }
            _texture = surface2;
        }
        return _texture;
    }

}
