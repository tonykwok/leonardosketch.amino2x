package com.joshondesign.amino.core;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
@class Core  The center of an Amino application.
@category misc
 */
public class Core {
    private Color backgroundColor;
    public Node root;
    private int width;
    private int height;
    private int fps = 30;
    private boolean clearBackground = true;
    private Map<String,Map<String,List<Callback>>> listeners = new HashMap<String,Map<String,List<Callback>>>();
    private JComponent comp;
    private List<Callback> callbacks = new ArrayList<Callback>();
    private boolean dirtyTrackingEnabled;
    private long lastTick;
    private long tickSum;
    private int tickIndex;
    private int tickSamples = 30;
    private long[] tickList = new long[tickSamples];
    private List<Anim> anims = new ArrayList<Anim>();
    private boolean smoothStrokes = false;
    private static CoreImpl _impl;
    private static Core _core;

    protected Core() {
    }

    public static void setImpl(CoreImpl impl) {
        _impl = impl;
    }

    /*
    //@method Set the size of the window. This will be replaced once we have a proper *Frame* class.
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }
    */

    /*
    //@property background The color of the scene's background.
    public Core setBackground(Color white) {
        this.backgroundColor = white;
        return this;
    }
    */

    //@method Start the rendering. If this isn't called then you won't see anything on the screen.
    public void start() {
        _impl.startTimerLoop(this);
        /*
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                realStart();
            }
        });*/
    }

    private void realStart() {
        /*
        JFrame frame = new JFrame();
        comp = new JComponent() {
            @Override
            protected void paintComponent(Graphics graphics) {
                Graphics2D gfx = (Graphics2D) graphics;
                _update(gfx);
            }
        };
        frame.add(comp);
        frame.pack();
        frame.setSize(this.width,this.height+20);
        frame.setVisible(true);
        MasterListener ml = new MasterListener(comp,root);
        comp.addMouseListener(ml);
        comp.addMouseMotionListener(ml);
        comp.addKeyListener(ml);
        new Timer(1000/60,new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                comp.repaint();
            }
        }).start();
        comp.requestFocus();
        */
    }

    /*
    private void drawScene(Graphics2D ctx) {
        ctx.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        if(isSmoothStrokes()) {
            ctx.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        }
        ctx.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        //fill the background
        if(this.clearBackground) {
            ctx.setPaint(backgroundColor);
            ctx.fillRect(0,0,this.width,this.height);
        }

        //draw the scene
        root.draw(ctx);
    }
    */

    private void _update(Graphics2D ctx) {
        long time = System.nanoTime();

        //process animation
        for(int i=0;i<this.anims.size(); i++) {
            Anim a = anims.get(i);
            if(!a.isStarted()) {
                try {
                    a.start(time);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                continue;
            }
            try {
                a.update(time);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //process callbacks
        for(int i=0;i<callbacks.size();i++) {
            callbacks.get(i).call(null);
        }

        //var ctx = self.canvas.getContext("2d");

        /*
        if(dirtyTrackingEnabled) {
            if(root.isDirty()) {
                //console.log("drawing");
                drawScene(ctx);
            }
        } else {
            drawScene(ctx);
        }
         */

        //ctx.save();
        ctx.translate(0,height-50);
        ctx.setPaint(Color.GRAY);
        ctx.fillRect(0,-10,200,60);
        //draw a debugging overlay
        ctx.setPaint(Color.BLACK);
        ctx.drawString("timestamp " + System.nanoTime(), 10, 0);

        //calc fps
        long delta = time-this.lastTick;
        this.lastTick = time;
        if(this.tickList.length <= this.tickIndex) {
            this.tickList[this.tickList.length] = 0;
        }
        this.tickSum -= this.tickList[this.tickIndex];
        this.tickSum += delta;
        this.tickList[this.tickIndex]=delta;
        ++this.tickIndex;
        if(this.tickIndex>=this.tickSamples) {
            this.tickIndex = 0;
        }
        double fpsAverage = this.tickSum/this.tickSamples;
        ctx.drawString("last msec/frame " + delta/(1000*1000), 10, 10);
        ctx.drawString("last frame msec " + ((System.nanoTime() - time)/(1000*1000)), 10, 20);
        ctx.drawString("avg msec/frame  " + (fpsAverage), 10, 30);
        ctx.drawString("avg fps = " + ((1.0 / fpsAverage) * 1000*1000*1000), 10, 40);
        //ctx.restore();
    }

    public void fireEvent(String type, Object key, Object e) {
        //u.p("firing event for key: " + key + " type = " + type);
        String k = "";
        if(key != null) {
            k = key.hashCode()+"";
        } else {
            k = "*";
        }
        //u.p("Using real key: " + k);
        //u.p("firing event for key: " + k + " type = " + type);
        if(listeners.containsKey(k)) {
            if(listeners.get(k).containsKey(type)) {
                for(Callback c : listeners.get(k).get(type)) {
                    c.call(e);
                }
            }
        }
    }

    public Core listen(Events type, Object eventTarget, Callback callback) {
        return listen(type.toString(),eventTarget, callback);
    }

    //@method Register an event listener. It will listen to events based on the eventType and eventTarget.  When events happen they will be sent to the *callback*.
    public Core listen(String eventType, Object eventTarget, Callback callback) {
        String key = "";
        if(eventTarget != null) {
            key = eventTarget.hashCode()+"";
        } else {
            key = "*";
        }
        if(!this.listeners.containsKey(key)) {
            this.listeners.put(key, new HashMap<String,List<Callback>>());
        }
        if(!this.listeners.get(key).containsKey(eventType)) {
            this.listeners.get(key).put(eventType, new ArrayList<Callback>());
        }
        this.listeners.get(key).get(eventType).add(callback);
        //u.p("added listener. key = "+ key + " type = " + eventType + " = " + callback);
        return this;
    }

    //@method Add a frame callback. The callback will be called on every frame.
    public Core addCallback(Callback callback) {
        this.callbacks.add(callback);
        return this;
    }

    //@method Add an animation. The animation will begin immediately.
    public Core addAnim(Anim anim) {
        this.anims.add(anim);
        return this;
    }
    /*
    //@property root  set the root node
    public Core setRoot(Node root) {
        this.root = root;
        return this;
    }
    */

    //@property fps set the desired FPS. Remember that this is simply a *target*
    public Core setFPS(int fps) {
        this.fps = fps;
        return this;
    }

    public boolean isSmoothStrokes() {
        return smoothStrokes;
    }

    public Core setSmoothStrokes(boolean s) {
        this.smoothStrokes = s;
        return this;
    }

    public static void init(InitCallback initCallback) {
        _core = new Core();
        try {
            if(_impl == null) {
                autodetectImpl();
            }

            _impl.init(_core,initCallback);
        } catch (AminoException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InstantiationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IllegalAccessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private static void autodetectImpl() throws ClassNotFoundException, IllegalAccessException, InstantiationException {

        if("sdl".equals(System.getProperty("com.joshondesign.amino.impl"))) {
            u.p("need to do SDL");
            Class clazz = Class.forName("com.joshondesign.amino.sdl.SDLCore");
            CoreImplProvider provider = (CoreImplProvider) clazz.newInstance();
            setImpl(provider.createImpl());
            return;
        }

        if("java2d".equals(System.getProperty("com.joshondesign.amino.impl"))) {
            u.p("need to do java2d");
            Class clazz = Class.forName("com.joshondesign.amino.java2d.Java2DCore");
            CoreImplProvider provider = (CoreImplProvider) clazz.newInstance();
            setImpl(provider.createImpl());
            return;
        }

        u.p("no Core impl specified. Will try falling back to Java2D");
        Class clazz = Class.forName("com.joshondesign.amino.java2d.Java2DCore");
        CoreImplProvider provider = (CoreImplProvider) clazz.newInstance();
        setImpl(provider.createImpl());


    }

    public Window createResizableWindow(int width, int height) throws AminoException {
        return _impl.createResizableWindow(_core, width,height);
    }

    public AminoFont loadFont(URL resource) throws Exception {
        return _impl.loadFont(resource);
    }

    public AminoFont loadFont(File resource) throws Exception {
        return _impl.loadFont(resource);
    }

    public AminoFont loadFont(String fontName) throws IOException {
        return _impl.loadFont(fontName);
    }

    public AminoImage loadImage(URL resource) {
        return _impl.loadImage(resource);
    }

    public AminoImage loadImage(File file) throws IOException {
        return _impl.loadImage(file);
    }

    public LinearGradient createVerticalLinearGradient(int start, int end) {
        return _impl.createVerticalLinearGradient(start, end);
    }

    public LinearGradient createHorizontalLinearGradient(int start, int end) {
        return _impl.createHorizontalLinearGradient(start,end);
    }

    public PatternPaint loadPattern(File file) throws IOException {
        return _impl.loadPattern(file);
    }

    public AminoPaint loadPattern(URL url) throws IOException {
        return _impl.loadPattern(url);
    }



    public interface InitCallback {
        public void call(Core core) throws Exception;
    }


    public static enum Events {
        MOUSE_PRESS,
        MOUSE_RELEASE,
        MOUSE_DRAG,
        KEY_PRESSED,
        KEY_RELEASED,
    }
}
