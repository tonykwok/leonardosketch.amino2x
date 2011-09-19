package com.joshondesign.amino.java2d;

import com.joshondesign.amino.core.*;
import com.joshondesign.amino.core.Window;
import com.joshondesign.amino.java2d.Java2DWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
                } catch (AminoException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        });
    }

    @Override
    public Window createResizableWindow(Core core, int width, int height) throws AminoException {
        Java2DWindow w = new Java2DWindow(width, height);
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

}
