package com.joshondesign.amino.sdl;

import com.joshondesign.amino.core.Bounds;
import com.joshondesign.amino.core.GFX;
import com.joshondesign.sdljava.SDL;
import com.joshondesign.sdljava.SDL_PixelFormat;
import com.joshondesign.sdljava.SDL_Rect;
import com.joshondesign.sdljava.SDL_Surface;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: josh
 * Date: 9/19/11
 * Time: 12:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class SDLGFX extends GFX {
    private SDL_Surface surface;
    private SDL_PixelFormat format;
    private SDL_Rect fillRect_rect;
    private SDL_Rect imgsrc_rect;
    private SDL_Rect imgdst_rect;
    private SDL_Rect fontsrc_rect;
    private SDL_Rect fontdst_rect;

    protected double translateX;
    protected double translateY;
    private Color currentPaint;
    private long current_sdlcolor;

    public SDLGFX(SDL_Surface surface) {
        super();
        this.surface = surface;
        this.format = surface.getFormat();
        this.fillRect_rect = new SDL_Rect();
        this.imgsrc_rect = new SDL_Rect();
        this.imgdst_rect = new SDL_Rect();
        this.fontsrc_rect = new SDL_Rect();
        this.fontdst_rect = new SDL_Rect();

    }

    public void fillRect(int x, int y, int w, int h) {
        fillRect_rect.setX((short) (x + this.translateX));
        fillRect_rect.setY((short) (y + this.translateY));
        fillRect_rect.setW(w);
        fillRect_rect.setH(h);

        //long c = SDL.SDL_MapRGB(format, (short) currentColor.r, (short) currentColor.g, (short) currentColor.b);
        SDL.SDL_FillRect(surface, fillRect_rect, current_sdlcolor);
    }

    @Override
    public void drawRect(int x, int y, int w, int h) {
        //h lines
        for (int i = 0; i < w + 1; i++) {
            setPixel(x + i, y);
            setPixel(x + i, y+h);
        }
        for (int j=0; j<h;j++) {
            setPixel(x,y+j);
            setPixel(x+w,y+j);
        }
    }

    private void setPixel(int x0, int y0) {
//        if (currentPaint != currentColor) {
//            SDL.setPixel(this.surface, x0+translateX, y0+translateY, getPixelValue(x0, y0, currentPaint.getImage(surface)));
//        } else {
            SDL.setPixel(surface, (int)(x0+translateX), (int)(y0+translateY), current_sdlcolor);
//        }
    }

    public void translate(double x, double y) {
        this.translateX += x;
        this.translateY += y;
    }

    public void dispose() {
        fillRect_rect.delete();
        imgsrc_rect.delete();
        imgdst_rect.delete();
        fontsrc_rect.delete();
        fontdst_rect.delete();
        format.delete();
    }

    public void setPaint(Color color) {
        this.currentPaint = color;
        this.current_sdlcolor = SDL.SDL_MapRGB(format,
                (short) currentPaint.getRed(),
                (short) currentPaint.getGreen(),
                (short) currentPaint.getBlue());
    }

    @Override
    public void drawLine(int x0, int y0, int x1, int y1) {
        int dx = abs(x1 - x0), sx = x0 < x1 ? 1 : -1;
        int dy = -abs(y1 - y0), sy = y0 < y1 ? 1 : -1;
        int err = dx + dy, e2; /* error value e_xy */

        for (; ; ) {  /* loop */
            setPixel(x0, y0);
            if (x0 == x1 && y0 == y1) break;
            e2 = 2 * err;
            if (e2 >= dy) {
                err += dy;
                x0 += sx;
            } /* e_xy+e_x > 0 */
            if (e2 <= dx) {
                err += dx;
                y0 += sy;
            } /* e_xy+e_y < 0 */
        }
    }


    private int abs(int i) {
        if (i < 0) {
            return -i;
        }
        return i;
    }
}
