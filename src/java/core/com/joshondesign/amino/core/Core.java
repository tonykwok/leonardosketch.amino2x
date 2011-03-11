package com.joshondesign.amino.core;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 3/10/11
 * Time: 2:33 PM
 * To change this template use File | Settings | File Templates.
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

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public Core setBackground(Color white) {
        this.backgroundColor = white;
        return this;
    }

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
                drawScene(gfx);
            }
        };
        frame.add(comp);
        frame.pack();
        frame.setSize(this.width,this.height);
        frame.setVisible(true);
        MasterListener ml = new MasterListener(comp,root);
        comp.addMouseListener(ml);
        comp.addMouseMotionListener(ml);
    }

    private void drawScene(Graphics2D ctx) {
        ctx.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

        //fill the background
        if(this.clearBackground) {
            ctx.setPaint(backgroundColor);
            ctx.fillRect(0,0,this.width,this.height);
        }

        //draw the scene
        root.draw(ctx);
    }

    public void listen(String eventType, Object eventTarget, Callback callback) {
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
    }

    private class MasterListener implements MouseListener, MouseMotionListener {
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

        private void fireEvent(String type, Object key, MEvent e) {
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

    }
}
