package com.joshondesign.amino.core;

import java.awt.geom.Point2D;

/**
 * Created by IntelliJ IDEA.
 * User: josh
 * Date: 9/19/11
 * Time: 12:12 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Window {
    protected Node root;
    protected AminoColor backgroundFill = AminoColor.WHITE;

    public void setRoot(Node root) {
        this.root = root;
    }

    public Node getRoot() {
        return this.root;
    }


    public void setBackgroundFill(AminoColor backgroundFill) {
        this.backgroundFill = backgroundFill;
    }


    public abstract int getWidth();
    public abstract int getHeight();

    public abstract Core getCore();

    public Node findNode(Point2D pt) {
        return findNode(getRoot(),pt);
    }

    private Node findNode(Node node, Point2D pt) {
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


}
