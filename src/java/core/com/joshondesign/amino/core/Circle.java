package com.joshondesign.amino.core;


import java.awt.Graphics2D;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 3/14/11
 * Time: 6:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class Circle extends Shape {
    private double radius;

    @Override
    public void draw(Graphics2D gfx) {
        gfx.setPaint(getFill());
        gfx.fillOval((int)(getX()-getRadius())
                ,(int)(getY()-getRadius())
                ,(int)(getRadius()*2)
                ,(int)(getRadius()*2));
    }

    public Circle set(double x, double y, double radius) {
        this.radius = radius;
        this.setX(x);
        this.setY(y);
        return this;
    }

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
