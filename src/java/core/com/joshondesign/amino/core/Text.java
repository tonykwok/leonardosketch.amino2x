package com.joshondesign.amino.core;

import java.awt.*;

/**
@class Text A shape which draws text in a particular font.
@category shape
 */
public class Text extends Shape {
    String text = "-no text-";
    AminoFont font;

    @Override
    public void draw(GFX ctx) {
        if(!isVisible()) return;
        //Font f = ctx.getFont();
        //u.p("drawing " + this.text + " " + this.font + " " + this.fill + " " + this.x + " " + this.y);
        //ctx.setFont(this.font);
        ctx.setPaint(this.fill);
        ctx.fillText(font, this.text, (int)this.x, (int) this.y);
        //ctx.drawString(this.text, (int) this.x, (int) this.y);
        //ctx.setFont(f);
        this.clearDirty();
    }

    //@property text the text string to be drawn
    public Text setText(String text) {
        this.text = text;
        markDirty();
        return this;
    }

    //@property font the Java2D font to draw the string with
    public Text setFont(AminoFont font) {
        this.font = font;
        markDirty();
        return this;
    }
}
