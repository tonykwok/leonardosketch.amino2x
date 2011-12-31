package com.joshondesign.amino.core;

import java.awt.*;
import java.awt.geom.Path2D;

/**
@class PathNode A *Shape* that draws or fills in a path.
@category shape
 */
public class PathNode extends Shape {
    private Path path;

    @Override
    public void draw(GFX gfx) {
        gfx.setPaint(getFill());
        Path2D path2d = path.toPath2D();
        //gfx.fill(path2d);
        if(getStrokeWidth() > 0) {
            gfx.setPaint(getStroke());
            //gfx.draw(path2d);
        }
    }

    //@property path the path to draw/fill.
    public PathNode setPath(Path path) {
        this.path = path;
        return this;
    }

}
