package com.joshondesign.amino.java2d;

import com.joshondesign.amino.core.*;
import com.joshondesign.amino.core.Window;
import com.joshondesign.amino.java2d.Java2DWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: josh
 * Date: 9/19/11
 * Time: 1:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class Java2DCoreImpl extends CoreImpl {
    private List<Java2DWindow> windows = new ArrayList<Java2DWindow>();

    @Override
    public void init(final Core core, final Core.InitCallback initCallback) throws AminoException {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    initCallback.call(core);
                    core.start();
                } catch (Exception e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        });
    }

    @Override
    public Window createResizableWindow(Core core, int width, int height) throws AminoException {
        Java2DWindow w = new Java2DWindow(core,width, height);
        this.windows.add(w);
        return w;
    }

    @Override
    public void startTimerLoop(Core core) {
        for(Java2DWindow w : windows) {
            if(!w._frame.isVisible()) {
                //w._frame.pack();
                w._frame.show();
            }
        }

        new Timer(1000/60,new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                for(Java2DWindow w : windows) {
                    w._rootcomp.repaint();
                }
            }
        }).start();


    }

    @Override
    public AminoFont loadFont(URL url) throws IOException, FontFormatException {
        return new Java2DFont(url);
    }

    @Override
    public AminoFont loadFont(File file) throws IOException, FontFormatException {
        return new Java2DFont(file);
    }

    @Override
    public AminoFont loadFont(String fontName) throws IOException {
        return new Java2DFont(fontName);
    }

    @Override
    public AminoImage loadImage(URL resource) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public AminoImage loadImage(File file) throws IOException {
        return new Java2DImage(file);
    }

    @Override
    public LinearGradient createVerticalLinearGradient(int start, int end) {
        return new Java2DVerticalLinearGradient(start,end);
    }

    @Override
    public LinearGradient createHorizontalLinearGradient(int start, int end) {
        return new Java2DHorizontalLinearGradient(start,end);
    }

    @Override
    public PatternPaint loadPattern(File file) throws IOException {
        return new Java2DPatternPaint(file);
    }

    @Override
    public AminoPaint loadPattern(URL url) throws IOException {
        return new Java2DPatternPaint(url);
    }

}
