package com.joshondesign.amino.sdl;

import com.joshondesign.amino.core.*;
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
    private long current_sdlcolor;
    private AminoPaint paint;
    private AminoColor current_color = AminoColor.BLACK;

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

        if(paint != null  && paint instanceof SDLSurfacePaint) {
            SDLSurfacePaint pt = (SDLSurfacePaint) paint;
            SDL_Surface image = pt.getImage(surface);
            for (int i = x; i < x + w; i++) {
                for (int j = y; j < y + h; j++) {
                    //drawImage(image,i,y);
                    setPixel(i, j);
                }
            }
        } else {
            fillRect_rect.setX((short) (x + this.translateX));
            fillRect_rect.setY((short) (y + this.translateY));
            fillRect_rect.setW(w);
            fillRect_rect.setH(h);
            SDL.SDL_FillRect(surface, fillRect_rect, current_sdlcolor);
        }
    }

    @Override
    public void drawRoundRect(int x1, int y1, int w, int h, int r) {
        int width = w - r - r;
        int height = h - r - r;
        int xm = x1 + r;
        int ym = y1 + r;
        int x = -r, y = 0, err = 2 - 2 * r; /* II. Quadrant */
        do {
            setPixel(xm - x + width, ym + y + height); /*   I. Quadrant */
            setPixel(xm - y, ym - x + height); /*  II. Quadrant */
            setPixel(xm + x, ym - y); /* III. Quadrant */
            setPixel(xm + y + width, ym + x); /*  IV. Quadrant */
            r = err;
            if (r > x) err += ++x * 2 + 1; /* e_xy+e_x > 0 */
            if (r <= y) err += ++y * 2 + 1; /* e_xy+e_y < 0 */
        } while (x < 0);
        drawVLine(x1, y1 + r / 2 + 1, h - r);
        drawVLine(x1 + w, y1 + r / 2, h - r);

        drawHLine(x1 + r / 2, y1, w - r);
        drawHLine(x1 + r / 2 + 1, y1 + h, w - r);
    }

    @Override
    public void fillRoundRect(int x1, int y1, int w, int h, int r) {
        int width = w - r - r;
        int height = h - r - r;
        int xm = x1 + r;
        int ym = y1 + r;
        int x = -r, y = 0, err = 2 - 2 * r; /* II. Quadrant */
        do {
            setPixel(xm - x + width, ym + y + height); /*   I. Quadrant */
            setPixel(xm - y, ym - x + height); /*  II. Quadrant */
            setPixel(xm + x, ym - y); /* III. Quadrant */
            setPixel(xm + y + width, ym + x); /*  IV. Quadrant */
            int x2 = xm-x+width;
            int w2 = x2-(xm+x);
            drawHLine(xm+x,ym-y,w2);
            drawHLine(xm+x,ym+y+height,w2);
            r = err;
            if (r > x) err += ++x * 2 + 1; /* e_xy+e_x > 0 */
            if (r <= y) err += ++y * 2 + 1; /* e_xy+e_y < 0 */
        } while (x < 0);
        drawVLine(x1, y1 + r / 2 + 1, h - r);
        drawVLine(x1 + w, y1 + r / 2, h - r);

        drawHLine(x1 + r / 2, y1, w - r);
        drawHLine(x1 + r / 2 + 1, y1 + h, w - r);
        for(int i=y1+r/2; i<y1+h-r/2; i++) {
            drawHLine(x1,i,w);
        }
    }

    @Override
    public void drawEllipse(int x1, int y1, int w, int h) {
        int x2 = x1 + w;
        int y2 = y1 + h;
        int a = abs(x1 - x2), b = abs(y1 - y2), b1 = b & 1; /* values of diameter */
        long dx = 4 * (1 - a) * b * b, dy = 4 * (b1 + 1) * a * a; /* error increment */
        long err = dx + dy + b1 * a * a, e2; /* error of 1.step */

        if (x2 > x1) {
            x2 = x1;
            x1 += a;
        } /* if called with swapped points */
        if (y2 > y1) y2 = y1; /* .. exchange them */
        y2 += (b + 1) / 2;
        y1 = y2 - b1;   /* starting pixel */
        a *= 8 * a;
        b1 = 8 * b * b;

        do {
            setPixel(x1, y2); /*   I. Quadrant */
            setPixel(x2, y2); /*  II. Quadrant */
            setPixel(x2, y1); /* III. Quadrant */
            setPixel(x1, y1); /*  IV. Quadrant */
            e2 = 2 * err;
            if (e2 >= dx) {
                x2++;
                x1--;
                err += dx += b1;
            } /* x step */
            if (e2 <= dy) {
                y2++;
                y1--;
                err += dy += a;
            }  /* y step */
        } while (x2 <= x1);

        while (y2 - y1 < b) {  /* too early stop of flat ellipses a=1 */
            setPixel(x2 - 1, y2); /* -> finish tip of ellipse */
            setPixel(x1 + 1, y2++);
            setPixel(x2 - 1, y1);
            setPixel(x1 + 1, y1--);
        }
    }

    @Override
    public void fillEllipse(int x1, int y1, int w, int h) {
        int x2 = x1 + w;
        int y2 = y1 + h;
        int a = abs(x1 - x2), b = abs(y1 - y2), b1 = b & 1; /* values of diameter */
        long dx = 4 * (1 - a) * b * b, dy = 4 * (b1 + 1) * a * a; /* error increment */
        long err = dx + dy + b1 * a * a, e2; /* error of 1.step */

        if (x2 > x1) {
            x2 = x1;
            x1 += a;
        } /* if called with swapped points */
        if (y2 > y1) y2 = y1; /* .. exchange them */
        y2 += (b + 1) / 2;
        y1 = y2 - b1;   /* starting pixel */
        a *= 8 * a;
        b1 = 8 * b * b;

        do {
            setPixel(x1, y2); /*   I. Quadrant */
            setPixel(x2, y2); /*  II. Quadrant */
            setPixel(x2, y1); /* III. Quadrant */
            setPixel(x1, y1); /*  IV. Quadrant */
            drawHLine(x2, y1, x1 - x2);
            drawHLine(x2,y2,x1-x2);
            e2 = 2 * err;
            if (e2 >= dx) {
                x2++;
                x1--;
                err += dx += b1;
            } /* x step */
            if (e2 <= dy) {
                y2++;
                y1--;
                err += dy += a;
            }  /* y step */
        } while (x2 <= x1);

        while (y2 - y1 < b) {  /* too early stop of flat ellipses a=1 */
            setPixel(x2 - 1, y2); /* -> finish tip of ellipse */
            setPixel(x1 + 1, y2++);
            setPixel(x2 - 1, y1);
            setPixel(x1 + 1, y1--);
        }
    }

    @Override
    public void drawCircle(int cx, int cy, int radius) {
        int x = -radius, y = 0, err = 2 - 2 * radius; /* II. Quadrant */
        do {
            setPixel(cx-x,cy+y);
            setPixel(cx-y,cy-x);

            drawHLine(cx-y,cy-x,y);
            drawHLine(cx,cy-x,y);

            //setPixel(cx+x,cy-y);
            //setPixel(cx+y,cy+x);
            //SDL.setPixel((SDL_Surface) gfx.getNative(), cx - x, cy + y, current_sdlcolor); /*   I. Quadrant */
            //SDL.setPixel((SDL_Surface) gfx.getNative(), cx - y, cy - x, current_sdlcolor); /*  II. Quadrant */
            //SDL.setPixel((SDL_Surface) gfx.getNative(), cx + x, cy - y, current_sdlcolor); /* III. Quadrant */
            //SDL.setPixel((SDL_Surface) gfx.getNative(), cx + y, cy + x, current_sdlcolor); /*  IV. Quadrant */
            radius = err;
            if (radius > x) err += ++x * 2 + 1; /* e_xy+e_x > 0 */
            if (radius <= y) err += ++y * 2 + 1; /* e_xy+e_y < 0 */
        } while (x < 0);
    }

    @Override
    public void fillCircle(int cx, int cy, int radius) {
        drawCircle(cx,cy,radius);
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

    private void drawImage(SDL_Surface image, int x, int y) {
        imgsrc_rect.setX((short) 0);
        imgsrc_rect.setY((short) 0);
        imgsrc_rect.setW(image.getW());
        imgsrc_rect.setH(image.getH());
        imgdst_rect.setX((short) (x+this.translateX));
        imgdst_rect.setY((short) (y+this.translateY));
        imgdst_rect.setW(0);
        imgdst_rect.setH(0);
        SDL.SDL_UpperBlit(image,imgsrc_rect,surface,imgdst_rect);
    }

    @Override
    public void drawImage(AminoImage image, int x, int y) {
        drawImage(((SDLImage)image)._image,x,y);
    }

    @Override
    public void drawImage(AminoImage image, int sx, int sy, int sw, int sh, int dx, int dy, int dw, int dh) {
        imgsrc_rect.setX((short) sx);
        imgsrc_rect.setY((short) sy);
        imgsrc_rect.setW(sw);
        imgsrc_rect.setH(sh);
        imgdst_rect.setX((short) (dx+this.translateX));
        imgdst_rect.setY((short) (dy+this.translateY));
        imgdst_rect.setW(dw);
        imgdst_rect.setH(dh);

        SDL.SDL_UpperBlit(((SDLImage)image)._image, imgsrc_rect, surface, imgdst_rect);
    }


    @Override
    public void drawImage9Slice(AminoImage image
            , int left, int right
            , int top, int bottom
            , int destX, int destY
            , int destW, int destH) {


        //draw parts of image
        int yh = image.getHeight()-top-bottom;

        drawRow(image, destX, destY, destW,
                left, right, 0, top, 0);

        //middle
        //Util.p("top = " + top + " desth = " + destH + " bottom = " + bottom + " yh = " + yh);
        for (int j = top; j <= destH-bottom-yh; j += yh) {
            //Util.p("drawing once");
            drawRow(image, destX, destY, destW,
                left, right, top, yh, j);
        }

        //center end
        int ygap = (destH-top-bottom) % yh;
        drawRow(image, destX, destY, destW,
                left, right, top, ygap, destH-bottom-ygap
                );

        drawRow(image, destX, destY, destW,
                left, right, image.getHeight()-bottom, bottom, destH-bottom
        );
    }

    private void drawRow(AminoImage image, int destX, int destY, int destW,
                         int left, int right, int sy, int sh, int dy) {
        int xw = image.getWidth()-left-right;
        // left
        drawImage(image,
                0, sy, left, sh,
                destX+0, destY+dy, left, sh);
        // center

        for (int i = left; i < destW-right-xw; i += xw) {
            drawImage(image,
                    left, sy, image.getWidth()-right-left, sh,
                    destX+i, destY+dy, xw, sh);
        }
        //center end
        int xgap = (destW-left-right) % xw;
        drawImage(image,
                left, sy, xgap, sh,
                destX+destW-right-xgap, destY+dy, xgap, sh
            );
        // right
        drawImage(image,
                image.getWidth()-right, sy, right, sh,
                destX+destW-right, destY+dy, right, sh);
    }


    @Override
    public void fillText(AminoFont aminoFont, String s, int dx, int dy) {
        SDLFont font = (SDLFont) aminoFont;
        SDL_Color fg = new SDL_Color();
        fg.setR((short) current_color.getRed());
        fg.setG((short) current_color.getGreen());
        fg.setB((short) current_color.getBlue());
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

    private long getPixelValue(int x0, int y0, SDL_Surface image) {
        return SDL.getPixel(image, x0 % image.getW(), y0 % image.getH());
    }

    private void setPixel(int x0, int y0) {
        if (paint != null && paint instanceof SDLSurfacePaint) {
            SDLSurfacePaint pt = (SDLSurfacePaint) paint;
            SDL.setPixel(this.surface, (int)(x0+translateX), (int)(y0+translateY),
                    getPixelValue(x0, y0, pt.getImage(surface)));
        } else {
            SDL.setPixel(surface, (int)(x0+translateX), (int)(y0+translateY), current_sdlcolor);
        }
    }

    public void drawVLine(int x, int y, int height) {
        for (int i = 0; i < height + 1; i++) {
            setPixel(x, y + i);
        }
    }

    public void drawHLine(int x, int y, int width) {
        for (int i = 0; i < width + 1; i++) {
            setPixel(x + i, y);
        }
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

    @Override
    public void setPaint(AminoPaint backgroundFill) {
        this.paint = backgroundFill;
        if(backgroundFill instanceof AminoColor) {
            this.current_color = (AminoColor)backgroundFill;
            this.current_sdlcolor = SDL.SDL_MapRGB(format,
                    (short) current_color.getRed(),
                    (short) current_color.getGreen(),
                    (short) current_color.getBlue());

        }
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

    public static AminoColor interpolate(AminoColor A, AminoColor B, double fraction) {
        int r = (int)(A.getRed()*(1.0-fraction) + B.getRed()*fraction);
        int g = (int)(A.getGreen()*(1.0-fraction) + B.getGreen()*fraction);
        int b = (int)(A.getBlue()*(1.0-fraction) + B.getBlue()*fraction);
        return AminoColor.fromRGB(r,g,b);
    }

}
