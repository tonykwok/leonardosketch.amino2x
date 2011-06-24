package com.joshondesign.amino.core;


import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
@class Circle A basic circle shape, *centered* around it's X and Y coordinates.
@category shape
 */
public class Circle extends Shape {
    private double radius;
    private boolean intAlign = false;

    @Override
    public void draw(Graphics2D gfx) {

        if(intAlign) {
            gfx.setPaint(getFill());
            gfx.fillOval((int)(getX()-getRadius())
                    ,(int)(getY()-getRadius())
                    ,(int)(getRadius()*2)
                    ,(int)(getRadius()*2));
            if(getStrokeWidth() > 0) {
                gfx.setPaint(getStroke());
                Stroke s = gfx.getStroke();
                gfx.setStroke(new BasicStroke((float) getStrokeWidth()));
                gfx.drawOval((int) (getX() - getRadius())
                        , (int) (getY() - getRadius())
                        , (int) (getRadius() * 2)
                        , (int) (getRadius() * 2));
                gfx.setStroke(s);
            }
        } else {
            gfx.setPaint(getFill());
            Ellipse2D.Double shape = new Ellipse2D.Double(getX() - getRadius()
                    , (getY() - getRadius())
                    , (getRadius() * 2)
                    , (getRadius() * 2));
            gfx.fill(shape);
            if(getStrokeWidth() > 0) {
                gfx.setPaint(getStroke());
                Stroke s = gfx.getStroke();
                gfx.setStroke(new BasicStroke((float) getStrokeWidth()));
                gfx.draw(shape);
                gfx.setStroke(s);
            }
        }
    }

    //@method Set the center x, center y, and radius of the circle
    public Circle set(double x, double y, double radius) {
        this.radius = radius;
        this.setX(x);
        this.setY(y);
        return this;
    }

    //@property radius The radius of the circle.
    public double getRadius() {
        return radius;
    }

    @Override
    public Bounds getVisualBounds() {
        return new Bounds(
                (int)(getX()-radius),
                (int)(getY()-radius),
                (int)radius*2,
                (int)radius*2);
    }
}
