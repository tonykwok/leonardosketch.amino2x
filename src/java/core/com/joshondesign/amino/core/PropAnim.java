package com.joshondesign.amino.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
@class PropAnim animates a property on a node. You can use a *PropAnim* to animate a coordinate, a dimension of a shape, opacity, or pretty much any other property on a Node.
@category animation
 */
public class PropAnim extends Anim {
    private Node node;
    private String prop;
    private boolean started;
    private double startValue;
    private double endValue;
    private double duration;
    private boolean loop;
    private boolean autoReverse;
    private boolean forward;
    private long startTime;
    private Method getMethod;
    private Method setMethod;

    //@constructor create a new PropAnim from a node, the string name of a property, the start value, the end value, and a duration in seconds
    public PropAnim(Node n, String prop, double start, double end, double duration) throws NoSuchMethodException {
        this.node = n;
        this.prop = prop;
        this.started = false;
        this.startValue = start;
        this.endValue = end;
        this.duration = duration;
        this.loop = false;
        this.autoReverse = false;
        this.forward = true;
        String property = prop;
        String getter = "get" + property.substring(0,1).toUpperCase() + property.substring(1);
        String setter = "set" + property.substring(0,1).toUpperCase() + property.substring(1);
        getMethod = this.node.getClass().getMethod(getter);
        setMethod = this.node.getClass().getMethod(setter,double.class);
    }

    boolean isStarted() {
        return this.started;
    }

    //@property loop determines if the animation should loop when it is finished.
    public PropAnim setLoop(boolean loop) {
        this.loop = loop;
        return this;
    }

    //@property autoReverse determines if the animation should reverse direction when it loops
    public PropAnim setAutoReverse(boolean r) {
        this.autoReverse = r;
        return this;
    }

    Anim start(long time) throws Exception {
    //this.start = function(time) {
        this.startTime = time;
        this.started = true;
        //this.node[self.prop] = self.startValue;
        setPropertyValue(startValue);
        return this;
    }

    void update(long time) throws Exception {
        long elapsed = time-this.startTime;
        double fract = 0.0;
        fract = elapsed/(duration*1000*1000*1000);
        if(fract > 1.0) {
            if(this.loop) {
                this.startTime = time;
                if(this.autoReverse) {
                    this.forward = !this.forward;
                }
                fract = 0.0;
            } else {
                return;
            }
        }

        if(!this.forward) {
            fract = 1.0-fract;
        }
        double value = (endValue-startValue)*fract + startValue;
        setPropertyValue(value);
        //self.node[self.prop] = value;
        node.markDirty();
    }

    private void setPropertyValue(double value) throws InvocationTargetException, IllegalAccessException {
        setMethod.invoke(node,new Object[]{value});
    }
}
