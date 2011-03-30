package com.joshondesign.amino.core;

/**
@class Callback  a generified callback interface, used throughout the system. This will go away once Java gets closures
@category misc
 */
public interface Callback<E> {
    //@method call back with an object
    public void call(E e);
}
