package com.joshondesign.amino.sdl;

import com.joshondesign.amino.core.*;
import com.joshondesign.amino.sdl.gfx.Util;
import com.joshondesign.sdljava.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: josh
 * Date: 9/19/11
 * Time: 12:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class SDLCoreImpl extends CoreImpl {
    private int frameCount;
    private boolean shouldStop;
    private int targetFPS = 60;
    private List<SDLWindow> windows = new ArrayList<SDLWindow>();

    @Override
    public void init(final Core core, final Core.InitCallback initCallback) {
        System.out.println("doing init");
        SDLUtil.init(new SDLUtil.InitCallback() {
            public void callback() throws Exception {
                System.out.println("should be on correct thread now");
                try {
                    Util.standard_init();
                    initCallback.call(core);
                    core.start();
                } catch (Throwable thr) {
                    thr.printStackTrace();
                }
            }
        });
    }

    @Override
    public Window createResizableWindow(Core core, int width, int height) throws AminoException {
        SDL_Surface surface = Util.createResizableWindow(width, height);
        SDLWindow window = new SDLWindow(core, surface);
        this.windows.add(window);
        return window;
    }

    @Override
    public void startTimerLoop(Core core) {
        try {
            SDL_Event event = new SDL_Event();
            long start = SDL.SDL_GetTicks();
            while(true) {
                if(frameCount % 30 == 0) {
                    p("here: " + frameCount);
                }
                int ret = SDL.SDL_PollEvent(event);
                if(ret == 1) {
                    processEvents(event);
                    //Util.dump(event);
                    if(event.getType() == SDL_EventType.SDL_ACTIVEEVENT) {

                    }
                    if(event.getType() == SDL_EventType.SDL_QUIT) {
                        break;
                    }
                    if(event.getType() == SDL_EventType.SDL_MOUSEMOTION) {
                        SDL.SDL_PeepEvents(event,1,SDL_eventaction.SDL_GETEVENT,SDL_EventMask.SDL_MOUSEMOTIONMASK);
                        SDL.SDL_PeepEvents(event,1,SDL_eventaction.SDL_GETEVENT,SDL_EventMask.SDL_MOUSEMOTIONMASK);
                        SDL.SDL_PeepEvents(event,1,SDL_eventaction.SDL_GETEVENT,SDL_EventMask.SDL_MOUSEMOTIONMASK);
                        SDL.SDL_PeepEvents(event,1,SDL_eventaction.SDL_GETEVENT,SDL_EventMask.SDL_MOUSEMOTIONMASK);
                    }
                }
                if(shouldStop) {
                    break;
                }
                updateAnims();
                draw(core);
                frameCount++;
                int delay = 1000/targetFPS;
                SDL.SDL_Delay(delay);
            }
            long end = SDL.SDL_GetTicks();
            p("total frames = " + frameCount);
            p("total time = " + (end-start));
            double f = frameCount;
            double d = end-start;
            d = d / 1000;
            double fps = f/d;
            p("fps = " + fps);

        } catch (Throwable thr) {
            thr.printStackTrace();
        }
        SDL.SDL_Quit();
        System.exit(0);
    }

    @Override
    public AminoFont loadFont(URL resource) {
        return new SDLFont(resource);
    }

    public AminoFont loadFont(File file) {
        return new SDLFont(file);
    }

    @Override
    public AminoFont loadFont(String fontName) throws IOException {
        return new SDLFont(fontName);
    }

    @Override
    public AminoImage loadImage(URL resource) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public AminoImage loadImage(File file) throws IOException {
        return new SDLImage(file);
    }

    @Override
    public LinearGradient createVerticalLinearGradient(int start, int end) {
        return new SDLVerticalLinearGradient(start,end);
    }

    @Override
    public LinearGradient createHorizontalLinearGradient(int start, int end) {
        return new SDLHorizontalLinearGradient(start,end);
    }

    @Override
    public PatternPaint loadPattern(File file) {
        return new SDLPattern(file);
    }

    @Override
    public AminoPaint loadPattern(URL url) throws IOException {
        return new SDLPattern(url);
    }

    private void draw(Core core) {
        for(SDLWindow w : windows) {
            GFX gfx = new SDLGFX(w.surface);

            gfx.setPaint(w.getBackgroundFill());
            //gfx.setPaint(SColor.GREEN);
            gfx.fillRect(0,0,w.surface.getW(),w.surface.getH());

            Node root = w.getRoot();
            root.draw(gfx);
            /*
            for(Layer layer : layers) {
                    layer.draw(gfx);
            }
            */
            SDL.SDL_Flip(w.surface);
            gfx.dispose();
        }
    }

    private void updateAnims() {
        //To change body of created methods use File | Settings | File Templates.
    }

    private void processEvents(SDL_Event event) {
        //To change body of created methods use File | Settings | File Templates.
    }

    private void p(String s) {
        System.out.println(s);
    }
}