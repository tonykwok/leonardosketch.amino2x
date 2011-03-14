package com.joshondesign.amino.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 3/14/11
 * Time: 12:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class PropAnim {
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

    PropAnim setLoop(boolean loop) {
        this.loop = loop;
        return this;
    }

    PropAnim setAutoReverse(boolean r) {
        this.autoReverse = r;
        return this;
    }

    PropAnim start(long time) throws InvocationTargetException, IllegalAccessException {
    //this.start = function(time) {
        this.startTime = time;
        this.started = true;
        //this.node[self.prop] = self.startValue;
        setPropertyValue(startValue);
        return this;
    }

    void update(long time) throws InvocationTargetException, IllegalAccessException {
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
