package com.joshondesign.amino.core;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 5/12/11
 * Time: 7:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class KEvent {
    public static enum KeyCode {
        LEFT,
        RIGHT,
        UNKNOWN,
    }

    public KeyCode key;

    @Override
    public String toString() {
        return "KEvent{" +
                "key=" + key +
                '}';
    }

}
