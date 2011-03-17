package com.joshondesign.amino.core;

import java.awt.*;

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

    public Circle set(double radius, double x, double y) {
        this.radius = radius;
        this.setX(x);
        this.setY(y);
        return this;
    }

    public double getRadius() {
        return radius;
    }
}
