package com.joshondesign.amino.core;

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
}
