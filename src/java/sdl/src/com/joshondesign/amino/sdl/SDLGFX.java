package com.joshondesign.amino.sdl;

import com.joshondesign.amino.core.AminoFont;
import com.joshondesign.amino.core.AminoImage;
import com.joshondesign.amino.core.Bounds;
import com.joshondesign.amino.core.GFX;
import com.joshondesign.sdljava.*;

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

    @Override
    public void drawImage(AminoImage image, int x, int y) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void drawImage(AminoImage image, int sx, int sy, int sw, int sh, int dx, int dy, int dw, int dh) {
    }

    @Override
    public void drawImage9Slice(AminoImage image, int left, int right, int top, int bottom, int x, int y, int w, int h) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void fillText(AminoFont aminoFont, String s, int dx, int dy) {
        SDLFont font = (SDLFont) aminoFont;
        SDL_Color fg = new SDL_Color();
        fg.setR((short) currentPaint.getRed());
        fg.setG((short) currentPaint.getGreen());
        fg.setB((short) currentPaint.getBlue());
        fg.setUnused((short) 0);

        SDL_Color bg = new SDL_Color();
        bg.setR((short) 0xFF);
        bg.setG((short) 0xFF);
        bg.setB((short) 0xFF);
        bg.setUnused((short) 0xFF);

        //SDL.TTF_SetFontHinting(font._sdlfont, SDL.TTF_HINTING_NORMAL);

        int x = 0;
        int[] minx = {0};
        int[] maxx = {0};
        int[] miny = {0};
        int[] maxy = {0};
        int[] advance = {0};
        for (int i = 0; i < s.length(); i++) {
            //SDL_Surface glyph_surface = SDL.TTF_RenderGlyph_Shaded(font._sdlfont, s.charAt(i), fg, bg);
            SDL_Surface glyph_surface = SDL.TTF_RenderGlyph_Blended(font._font, s.charAt(i), fg);
            int ret = SDL.TTF_GlyphMetrics(font._font, s.charAt(i)
                    , minx
                    , maxx
                    , miny
                    , maxy
                    , advance
            );
            fontsrc_rect.setX((short) 0);
            fontsrc_rect.setY((short) 0);
            fontsrc_rect.setW(glyph_surface.getW());
            fontsrc_rect.setH(glyph_surface.getH());

            fontdst_rect.setX((short) (dx + x + translateX));
            fontdst_rect.setY((short) (dy - maxy[0] + translateY));
            fontdst_rect.setW(0);
            fontdst_rect.setH(0);
            SDL.SDL_UpperBlit(glyph_surface, fontsrc_rect, surface, fontdst_rect);
            x += advance[0];
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
