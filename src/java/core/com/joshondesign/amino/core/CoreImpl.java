package com.joshondesign.amino.core;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: josh
 * Date: 9/19/11
 * Time: 12:09 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class CoreImpl {
    public abstract void init(Core core, Core.InitCallback initCallback) throws AminoException;
    public abstract Window createResizableWindow(Core core, int width, int height) throws AminoException;
    public abstract void startTimerLoop(Core core);

    public abstract AminoFont loadFont(URL resource) throws IOException, FontFormatException;
    public abstract AminoFont loadFont(File resource) throws IOException, FontFormatException;

    public abstract AminoImage loadImage(URL resource);
}
