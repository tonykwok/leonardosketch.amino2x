package com.joshondesign.amino.core;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 3/14/11
 * Time: 11:52 AM
 * To change this template use File | Settings | File Templates.
 */
public class Text extends Shape {
    String text = "-no text-";
    Font font = new Font("Verdana",20,Font.PLAIN);

    @Override
    public void draw(Graphics2D ctx) {
        Font f = ctx.getFont();
        u.p("drawing " + this.text + " " + this.font + " " + this.fill);
        ctx.setFont(this.font);
        ctx.setPaint(this.fill);
        ctx.drawString(this.text, (int) this.x, (int) this.y);
        ctx.setFont(f);
        this.clearDirty();
    }

    public Text setText(String text) {
        this.text = text;
        markDirty();
        return this;
    }

    public Text setFont(Font font) {
        this.font = font;
        markDirty();
        return this;
    }
}
