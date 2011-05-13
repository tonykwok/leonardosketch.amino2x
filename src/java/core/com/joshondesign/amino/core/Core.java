package com.joshondesign.amino.core;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
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

    //@method Set the size of the window. This will be replaced once we have a proper *Frame* class.
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    //@property background The color of the scene's background.
    public Core setBackground(Color white) {
        this.backgroundColor = white;
        return this;
    }

    //@method Start the rendering. If this isn't called then you won't see anything on the screen.
    public void start() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                realStart();
            }
        });
    }

    private void realStart() {
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
    }

    private void drawScene(Graphics2D ctx) {
        ctx.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        ctx.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        //fill the background
        if(this.clearBackground) {
            ctx.setPaint(backgroundColor);
            ctx.fillRect(0,0,this.width,this.height);
        }

        //draw the scene
        root.draw(ctx);
    }

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

        if(dirtyTrackingEnabled) {
            if(root.isDirty()) {
                //console.log("drawing");
                drawScene(ctx);
            }
        } else {
            drawScene(ctx);
        }


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

    //@property root  set the root node
    public Core setRoot(Node root) {
        this.root = root;
        return this;
    }

    //@property fps set the desired FPS. Remember that this is simply a *target*
    public Core setFPS(int fps) {
        this.fps = fps;
        return this;
    }

    private class MasterListener implements MouseListener, MouseMotionListener, KeyListener {
        private JComponent canvas;
        private boolean _mouse_pressed = false;
        private Node root;
        private Node _drag_target = null;

        private MasterListener(JComponent comp, Node root) {
            this.canvas = comp;
            this.root = root;
        }

        public void mouseClicked(MouseEvent mouseEvent) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void mousePressed(MouseEvent e) {
            _mouse_pressed = true;
            //send target node event first
            Node node = findNode(root, e.getPoint());
            //p("---------- found node --------");
            //console.log(node);
            MEvent evt = new MEvent();
            evt.node = node;
            evt.x = e.getX();
            evt.y = e.getY();
            if(node != null) {
                Node start = node;
                _drag_target = node;
                while(start != null) {
                    fireEvent("MOUSE_PRESS", start, evt);
                    //p("blocked = " + start.isMouseBlocked());
                    if(start.isMouseBlocked()) return;
                    start = (Node) start.getParent();
                }
            }
            //send general events next
            fireEvent("MOUSE_PRESS", null, evt);
            //p("---------------");
        }

        public void mouseReleased(MouseEvent e) {
            _mouse_pressed = false;
            _drag_target = null;
            //send target node event first
            Node node = findNode(root,e.getPoint());
            //console.log(node);
            MEvent evt = new MEvent();
            evt.node = node;
            evt.x = e.getX();
            evt.y = e.getY();
            if(node != null) {
                Node start = node;
                while(start != null) {
                    fireEvent("MOUSE_RELEASE", start, evt);
                    if(start.isMouseBlocked()) return;
                    start = (Node) start.getParent();
                }
            }
            //send general events next
            fireEvent("MOUSE_RELEASE",null,evt);
        }

        public void mouseEntered(MouseEvent mouseEvent) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void mouseExited(MouseEvent mouseEvent) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void mouseDragged(MouseEvent e) {
            if(_mouse_pressed) {
                Node node = findNode(root,e.getPoint());
                MEvent evt = new MEvent();

                //redirect events to current drag target, if applicable
                if(_drag_target != null) {
                    node = _drag_target;
                }
                evt.node = node;
                evt.x = e.getX();
                evt.y = e.getY();
                if(node != null) {
                    Node start = node;
                    while(start != null) {
                        fireEvent("MOUSE_DRAG", start, evt);
                        if(start.isMouseBlocked()) return;
                        start = (Node) start.getParent();
                    }
                }
                //send general events next
                fireEvent("MOUSE_DRAG", null, evt);
            }
        }

        public void mouseMoved(MouseEvent e) {
        }

        private Node findNode(Node node, Point2D pt) {
            //u.p("find node: " + node + " " + pt);
            if(!node.isVisible()) return null;
            if(node.contains(pt)) return node;
            if(node instanceof Parent) {
                Parent parent = (Parent) node;
                if(parent.hasChildren()) {
                    Point2D nc = parent.convertToChildCoords(pt);
                    for(int i=parent.childCount()-1;i>=0;i--) {
                        Node n2 = findNode(parent.getChild(i),nc);
                        //u.p("Found " + n2);
                        if(n2 != null) {
                            return n2;
                        }
                    }
                }
            }
            return null;
        }

        private void fireEvent(String type, Object key, Object e) {
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
            comp.repaint();
        }

        public void keyTyped(KeyEvent keyEvent) {
        }

        public void keyPressed(KeyEvent keyEvent) {
            KEvent evt = new KEvent();
            evt.key = keyEvent.getKeyCode();
            fireEvent("KEY_PRESSED", null, evt);
        }

        public void keyReleased(KeyEvent keyEvent) {
            KEvent evt = new KEvent();
            evt.key = keyEvent.getKeyCode();
            fireEvent("KEY_RELEASED", null, evt);
        }
    }
}
