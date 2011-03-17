package com.joshondesign.amino.core;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 3/14/11
 * Time: 6:53 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Anim {
    abstract boolean isStarted();
    abstract Anim start(long time) throws Exception;
    abstract void update(long time) throws Exception;
}
