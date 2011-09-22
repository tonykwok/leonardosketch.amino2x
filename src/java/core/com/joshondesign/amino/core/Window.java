package com.joshondesign.amino.core;

import java.awt.*;

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
}
