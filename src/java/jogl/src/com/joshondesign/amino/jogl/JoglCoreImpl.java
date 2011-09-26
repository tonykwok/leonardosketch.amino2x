package com.joshondesign.amino.jogl;

import com.joshondesign.amino.core.*;
import com.joshondesign.amino.core.Window;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: josh
 * Date: 9/20/11
 * Time: 12:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class JoglCoreImpl extends CoreImpl {
    private List<JOGLWindow> windows = new ArrayList<JOGLWindow>();

    @Override
    public void init(Core core, Core.InitCallback initCallback) throws AminoException {
        try {
            initCallback.call(core);
            core.start();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Override
    public Window createResizableWindow(Core core, int width, int height) throws AminoException {
        JOGLWindow window =  new JOGLWindow(core,width,height);
        windows.add(window);
        return window;
    }

    @Override
    public void startTimerLoop(Core core) {
        for(JOGLWindow win : windows) {
            win.start();
        }
    }

    @Override
    public AminoFont loadFont(URL resource) throws IOException, FontFormatException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public AminoFont loadFont(File resource) throws IOException, FontFormatException {
        return new JoglFont(resource);
    }

    @Override
    public AminoFont loadFont(String fontName) throws IOException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public AminoImage loadImage(URL resource) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public AminoImage loadImage(File file) throws IOException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public LinearGradient createVerticalLinearGradient(int start, int end) {
        return new JoglVerticalLinearGradient(start,end);
    }

    @Override
    public LinearGradient createHorizontalLinearGradient(int start, int end) {
        return new JoglHorizontalLinearGradient(start, end);
    }

    @Override
    public PatternPaint loadPattern(File file) throws IOException {
        return new JoglPatternPaint(file);
    }

    @Override
    public AminoPaint loadPattern(URL url) throws IOException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
