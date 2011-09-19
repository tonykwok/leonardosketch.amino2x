package com.joshondesign.amino.java2d;

import com.joshondesign.amino.core.CoreImpl;
import com.joshondesign.amino.core.CoreImplProvider;

/**
 * Created by IntelliJ IDEA.
 * User: josh
 * Date: 9/19/11
 * Time: 1:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class Java2DCore implements CoreImplProvider{
    public static CoreImpl getImpl() {
        return new Java2DCoreImpl();
    }

    public CoreImpl createImpl() {
        return Java2DCore.getImpl();
    }
}
