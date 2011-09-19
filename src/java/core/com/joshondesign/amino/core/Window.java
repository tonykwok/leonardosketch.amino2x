package com.joshondesign.amino.core;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: josh
 * Date: 9/19/11
 * Time: 12:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class Window {
    protected Node root;
    protected Color backgroundFill;

    public void setRoot(Node root) {
        this.root = root;
    }

    public Node getRoot() {
        return this.root;
    }


    public void setBackgroundFill(Color backgroundFill) {
        this.backgroundFill = backgroundFill;
    }
}
