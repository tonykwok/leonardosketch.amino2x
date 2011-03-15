package com.joshondesign.amino.core;

import java.awt.*;
import java.awt.geom.Path2D;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 3/14/11
 * Time: 5:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class PathNode extends Shape {
    private Path path;

    @Override
    public void draw(Graphics2D gfx) {
        gfx.setPaint(getFill());
        Path2D path2d = path.toPath2D();
        gfx.fill(path2d);
        if(getStrokeWidth() > 0) {
            gfx.setPaint(getStroke());
            gfx.draw(path2d);
        }
    }

    public PathNode setPath(Path path) {
        this.path = path;
        return this;
    }

}
