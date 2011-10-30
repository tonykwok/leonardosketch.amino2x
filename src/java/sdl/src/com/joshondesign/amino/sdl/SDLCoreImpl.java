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
    private boolean mouseDown;
    private Core core;
    private Node _drag_target;

    @Override
    public void init(final Core core, final Core.InitCallback initCallback) {
        this.core = core;
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
    public Window createFullscreenWindow(Core core) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
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
        return new SDLImage(resource);
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
        //Util.dump(event);
        if(event.getType() == SDL_EventType.SDL_MOUSEBUTTONDOWN) {
            mouseDown = true;
            MEvent e = new MEvent();
            e.x = event.getButton().getX();
            e.y = event.getButton().getY();

            Window window = windows.get(0);
            Node node = window.findNode(new AminoPoint(e.getPoint().getX(),e.getPoint().getY()));
            e.node = node;
            if(node != null) {
                Node start = node;
                _drag_target = node;
                while(start != null) {
                    core.fireEvent(Core.Events.MOUSE_PRESS.toString(), start, e);
                    //p("blocked = " + start.isMouseBlocked());
                    if(start.isMouseBlocked()) return;
                    start = (Node) start.getParent();
                }
            }
            //send general events next
            core.fireEvent(Core.Events.MOUSE_PRESS.toString(), null, e);
        }
        if(event.getType() == SDL_EventType.SDL_MOUSEBUTTONUP) {
            mouseDown = false;
            MEvent e = new MEvent();
            e.node = null;
            e.x = event.getButton().getX();
            e.y = event.getButton().getY();
            core.fireEvent(Core.Events.MOUSE_RELEASE.toString(), null, e);

            mouseDown = false;
            _drag_target = null;
            //send target node event first
            Window window = windows.get(0);
            Node node = window.findNode(new AminoPoint(e.getPoint().getX(),e.getPoint().getY()));
            //console.log(node);
            //MEvent evt = new MEvent();
            e.node = node;
            if(node != null) {
                Node start = node;
                while(start != null) {
                    //fireEvent("MOUSE_RELEASE", start, e);
                    core.fireEvent(Core.Events.MOUSE_RELEASE.toString(),start, e);
                    if(start.isMouseBlocked()) return;
                    start = (Node) start.getParent();
                }
            }
            //send general events next
            //fireEvent("MOUSE_RELEASE",null,evt);
            core.fireEvent(Core.Events.MOUSE_RELEASE.toString(),null,e);
        }
        if(event.getType() == SDL_EventType.SDL_MOUSEMOTION && mouseDown) {
            MEvent e = new MEvent();
            e.node = null;
            e.x = event.getMotion().getX();
            e.y = event.getMotion().getY();
            core.fireEvent(Core.Events.MOUSE_DRAG.toString(), null, e);

            if(mouseDown) {
                Window window = windows.get(0);
                Node node = window.findNode(new AminoPoint(e.getPoint().getX(),e.getPoint().getY()));
                //MEvent evt = new MEvent();

                //redirect events to current drag target, if applicable
                if(_drag_target != null) {
                    node = _drag_target;
                }
                //evt.node = node;
                e.node = node;
                //evt.x = e.getX();
                //evt.y = e.getY();
                if(node != null) {
                    Node start = node;
                    while(start != null) {
                        core.fireEvent(Core.Events.MOUSE_DRAG.toString(),start, e);
                        if(start.isMouseBlocked()) return;
                        start = (Node) start.getParent();
                    }
                }
                //send general events next
                core.fireEvent(Core.Events.MOUSE_DRAG.toString(),null,e);
            }
        }
        if(event.getType() == SDL_EventType.SDL_KEYDOWN) {
            KEvent e = new KEvent();
            //e.key = event.getKey().getKeysym().getScancode();
            e.key = sdlToAminoKeycode(event.getKey());
            core.fireEvent(Core.Events.KEY_PRESSED.toString(),null,e);
        }
        if(event.getType() == SDL_EventType.SDL_KEYUP) {
            KEvent e = new KEvent();
            e.key = sdlToAminoKeycode(event.getKey());
            core.fireEvent(Core.Events.KEY_RELEASED.toString(),null,e);
        }

        /*
        if(event.getType() == SDL_EventType.SDL_MOUSEBUTTONDOWN) {
            mouseDown = true;
            for(Trigger.TriggerRule rule : triggers) {
                if(rule.matches(event)) {
                    rule.call(event);
                }
            }
        }
        if(event.getType() == SDL_EventType.SDL_MOUSEBUTTONUP) {
            mouseDown = false;
            for(Trigger.TriggerRule rule : triggers) {
                if(rule.matches(event)) {
                    rule.call(event);
                }
            }
        }
        if(event.getType() == SDL_EventType.SDL_MOUSEMOTION && mouseDown) {
            MouseEvent evt = new MouseEvent(event.getMotion().getX(),event.getMotion().getY());
            for(Callback<MouseEvent> callback : mouseDragListeners) {
                callback.call(evt);
            }
            for(Trigger.TriggerRule rule : triggers) {
                if(rule.matches(event)) {
                    rule.call(event);
                }
            }
        }
        if(event.getType() == SDL_EventType.SDL_KEYDOWN && !keydown) {
            keydown = true;
            for(Trigger.TriggerRule rule : triggers) {
                if(rule.matches(event)) {
                    rule.call(event);
                }
            }
        }
        if(event.getType() == SDL_EventType.SDL_KEYUP) {
            keydown = false;
        }

        //gesture stuff

        if(event.getType() == SDL_EventType.SDL_MOUSEBUTTONDOWN) {
            RawEvents.RawMouseEvent e = new RawEvents.RawMouseEvent();
            e.x = event.getButton().getX();
            e.y = event.getButton().getY();
            e.pressed = true;
            Gestures.publish(e);
        }

        if(event.getType() == SDL_EventType.SDL_MOUSEBUTTONUP) {
            RawEvents.RawMouseEvent e = new RawEvents.RawMouseEvent();
            e.x = event.getButton().getX();
            e.y = event.getButton().getY();
            e.released = true;
            Gestures.publish(e);
        }

        if(event.getType() == SDL_EventType.SDL_KEYDOWN) {
            RawEvents.RawKeyEvent e = new RawEvents.RawKeyEvent();
            int sym = event.getKey().getKeysym().getSym();
            int mod = event.getKey().getKeysym().getMod();
            if(!keymap.containsKey(sym)) {
                Util.warn("keymap doesn't contain the symbol: " + sym + " : " +
                        SDL.SDL_GetKeyName(sym)
                );
                return;
            }
            e.keyCode = keymap.get(sym);
            e.pressed = true;
            if((mod & SDLMod.KMOD_LSHIFT) > 0) e.shiftPressed = true;
            if((mod & SDLMod.KMOD_RSHIFT) > 0) e.shiftPressed = true;
            if((mod & SDLMod.KMOD_LMETA)  > 0) e.commandPressed = true;
            Gestures.publish(e);
        }

        if(event.getType() == SDL_EventType.SDL_KEYUP) {
            RawEvents.RawKeyEvent e = new RawEvents.RawKeyEvent();
            int sym = event.getKey().getKeysym().getSym();
            int mod = event.getKey().getKeysym().getMod();
            if(!keymap.containsKey(sym)) {
                Util.warn("keymap doesn't contain the symbol: " + sym);
                return;
            }
            e.keyCode = keymap.get(sym);
            e.released = true;
            if((mod & SDLMod.KMOD_LSHIFT) > 0) e.shiftPressed = true;
            if((mod & SDLMod.KMOD_RSHIFT) > 0) e.shiftPressed = true;
            if((mod & SDLMod.KMOD_LMETA)  > 0) e.commandPressed = true;
            Gestures.publish(e);
        }

        */
    }

    private KEvent.KeyCode sdlToAminoKeycode(SDL_KeyboardEvent key) {
        switch(key.getKeysym().getScancode()) {
            case 123: return KEvent.KeyCode.LEFT;
            case 124: return KEvent.KeyCode.RIGHT;
        }
        return KEvent.KeyCode.UNKNOWN;
    }

    private void p(String s) {
        System.out.println(s);
    }
}
