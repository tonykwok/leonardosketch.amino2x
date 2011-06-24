package com.joshondesign.amino.core;

import java.awt.geom.Point2D;

/**
@class PathAnim animates a node along a Path.
@category animation
 */
public class PathAnim extends Anim {
    private Node node;
    private double duration;
    private boolean started = false;
    private long startTime;
    private Path path;
    private boolean loop = false;
    private boolean autoReverse = false;
    private boolean forward = true;

    //@constructor create a PathAnimation for the  *node* along a *path* lasting *duration* seconds.
    public PathAnim(Node node, Path path, double duration) {
        this.node = node;
        this.path = path;
        this.duration = duration;
    }

    @Override
    boolean isStarted() {
        return started;
    }

    @Override
    Anim start(long time) throws Exception {
        this.started = true;
        this.startTime = time;
        return this;
    }

    //@property loop indicates if the animation should loop once it's finished
    public PathAnim setLoop(boolean loop) {
        this.loop = loop;
        return this;
    }

    //@property autoReverse indicates if the animation should switch direction when it loops
    public PathAnim setAutoReverse(boolean r) {
        this.autoReverse = r;
        return this;
    }

    @Override
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

        Point2D pt = path.pointAtT(fract);
        node.setX(pt.getX());
        node.setY(pt.getY());
    }
}
