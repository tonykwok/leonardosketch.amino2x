package com.joshondesign.amino.core;

/**
 @class Anim The base class of all animations. Use one of the subclasses.
 */
public abstract class Anim {
    abstract boolean isStarted();
    abstract Anim start(long time) throws Exception;
    abstract void update(long time) throws Exception;
}
