package com.joshondesign.amino.sdl;

import com.joshondesign.amino.core.CoreImpl;
import com.joshondesign.amino.core.CoreImplProvider;

/**
 * Created by IntelliJ IDEA.
 * User: josh
 * Date: 9/19/11
 * Time: 12:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class SDLCore implements CoreImplProvider {
    public CoreImpl createImpl() {
        return SDLCore.getImpl();
    }

    public static CoreImpl getImpl() {
        return new SDLCoreImpl();
    }
}
