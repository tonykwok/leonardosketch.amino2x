package com.joshondesign.amino.java2d;

import com.joshondesign.amino.core.*;
import com.joshondesign.amino.core.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by IntelliJ IDEA.
 * User: josh
 * Date: 9/19/11
 * Time: 1:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class Java2DWindow extends Window {
    JFrame _frame;
    JComponent _rootcomp;
    private Core core;

    public Java2DWindow(Core core, int width, int height) {
        this.core = core;
        this._frame = new JFrame();
        this._frame.setSize(width, height);
        this._frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this._rootcomp = new JComponent() {
            @Override
            protected void paintComponent(Graphics graphics) {
                Graphics2D gfx = (Graphics2D) graphics;
                _update(gfx, this);
            }
        };
        this._frame.add(_rootcomp);

        MasterListener ml = new MasterListener(_rootcomp, this);
        _rootcomp.addMouseListener(ml);
        _rootcomp.addMouseMotionListener(ml);
        _rootcomp.addKeyListener(ml);
    }


    private void _update(Graphics2D graphics2D, JComponent jComponent) {
        GFX ctx = new Java2DGFX(graphics2D);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        //graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        ctx.setPaint(backgroundFill);
        ctx.fillRect(0,0,jComponent.getWidth(),jComponent.getHeight());
        //draw the scene
        root.draw(ctx);
    }

    @Override
    public int getWidth() {
        return _frame.getWidth();
    }

    @Override
    public int getHeight() {
        return _frame.getHeight();
    }

    @Override
    public Core getCore() {
        return this.core;
    }

    @Override
    public void close() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    private static class MasterListener implements MouseListener, MouseMotionListener, KeyListener {
        private JComponent canvas;
        private boolean _mouse_pressed = false;
        private Node _drag_target = null;
        private Java2DWindow window;

        private MasterListener(JComponent comp, Java2DWindow java2DWindow) {
            this.canvas = comp;
            this.window = java2DWindow;
        }

        public void mouseClicked(MouseEvent mouseEvent) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void mousePressed(MouseEvent e) {
            _mouse_pressed = true;
            //send target node event first
            Node node = window.findNode(new AminoPoint(e.getPoint().getX(),e.getPoint().getY()));
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
            Node node = window.findNode(new AminoPoint(e.getPoint().getX(),e.getPoint().getY()));
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
                Node node = window.findNode(new AminoPoint(e.getPoint().getX(),e.getPoint().getY()));
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

        private Node findNode(Node node, AminoPoint pt) {
            if(!node.isVisible()) return null;
            if(node.contains(pt)) return node;
            if(node instanceof Parent) {
                Parent parent = (Parent) node;
                if(parent.hasChildren()) {
                    AminoPoint nc = parent.convertToChildCoords(pt);
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
            window.core.fireEvent(type, key, e);
            canvas.repaint();
        }

        public void keyTyped(KeyEvent keyEvent) {
        }

        public void keyPressed(KeyEvent keyEvent) {
            KEvent evt = new KEvent();
            //evt.key = keyEvent.getKeyCode();
            evt.key = awtToAminoKeycode(keyEvent.getKeyCode());
            fireEvent("KEY_PRESSED", null, evt);
        }

        public void keyReleased(KeyEvent keyEvent) {
            KEvent evt = new KEvent();
            evt.key = awtToAminoKeycode(keyEvent.getKeyCode());
            fireEvent("KEY_RELEASED", null, evt);
        }

        private static KEvent.KeyCode awtToAminoKeycode(int keyCode) {
            switch(keyCode) {
                case 37: return KEvent.KeyCode.LEFT;
                case 39: return KEvent.KeyCode.RIGHT;
            }
            return KEvent.KeyCode.UNKNOWN;
        }

    }

}
