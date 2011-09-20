package com.joshondesign.amino.jogl;

import com.joshondesign.amino.core.CoreImpl;
import com.joshondesign.amino.core.CoreImplProvider;

/**
 * Created by IntelliJ IDEA.
 * User: josh
 * Date: 9/20/11
 * Time: 12:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class JoglCore implements CoreImplProvider {
    public static CoreImpl getImpl() {
        return new JoglCoreImpl();
    }

    public CoreImpl createImpl() {
        return JoglCore.getImpl();
    }

}
