package com.joshondesign.amino.core.input;

import com.joshondesign.amino.core.*;

/**
 * Created by IntelliJ IDEA.
 * User: josh
 * Date: 9/20/11
 * Time: 11:28 AM
 * To change this template use File | Settings | File Templates.
 */
public class KeyboardTests implements Core.InitCallback {
    public static void main(String ... args) {
        Core.init(new KeyboardTests());
    }

    public void call(Core core) throws Exception {
        Window window = core.createResizableWindow(800, 600);
        window.setRoot(new Rect().set(20,20,200,50).setFill(AminoColor.MAGENTA));

        core.listen(Core.Events.KEY_PRESSED, null, new Callback<KEvent>() {
            public void call(KEvent e) {
                u.p("key pressed: " + e);
            }
        });

        core.listen(Core.Events.KEY_RELEASED, null, new Callback<KEvent>() {
            public void call(KEvent e) {
                u.p("key released: " + e);
            }
        });

        core.listen(Core.Events.MOUSE_PRESS, null, new Callback<MEvent>() {
            public void call(MEvent mEvent) {
                u.p("mouse press: " + mEvent);
            }
        });

        core.listen(Core.Events.MOUSE_DRAG, null, new Callback<MEvent>() {
            public void call(MEvent mEvent) {
                u.p("mouse drag: " + mEvent);
            }
        });

        core.listen(Core.Events.MOUSE_RELEASE, null, new Callback<MEvent>() {
            public void call(MEvent mEvent) {
                u.p("mouse release: " + mEvent);
            }
        });
    }
}
