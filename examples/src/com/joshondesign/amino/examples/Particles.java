package com.joshondesign.amino.examples;

/**
 * Created by IntelliJ IDEA.
 * User: josh
 * Date: 9/20/11
 * Time: 8:28 PM
 * To change this template use File | Settings | File Templates.
 */

import com.joshondesign.amino.core.*;
import com.joshondesign.amino.core.Window;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: josh
 * Date: 9/19/11
 * Time: 1:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class Particles implements Core.InitCallback {

    private List<Particle> particles = new ArrayList<Particle>();

    public static void main(String ... args) {
        Core.init(new Particles());
    }

    public void call(Core core) throws AminoException {

        final Window window = core.createResizableWindow(800,600);
        window.setBackgroundFill(AminoColor.BLACK);
        window.setRoot(new Node() {
            @Override
            public void draw(GFX gfx) {
                if(particles.size() < 100) {
                    particles.add(new Particle(
                            (int)(Math.random()*window.getWidth())
                            ,650
                            ,2+Math.random()*2
                    ));
                }

                gfx.setPaint(AminoColor.BLACK);
                //clear old particles
                for(Particle part : particles) {
                    gfx.fillRect(part.x,(int)part.y,20,20);
                }

                for(Particle part : particles) {
                    part.y -= part.vy;
                    if(part.y < -40) part.y = 700;
                }

                gfx.setPaint(AminoColor.WHITE);
                for(Particle part : particles) {
                    gfx.fillRect(part.x,(int)part.y,20,20);
                }
            }
        });

        core.start();
    }


    private class Particle {

        private int x;
        private double y;
        private double vy;

        public Particle(int x, int y, double v) {
            this.x = x;
            this.y = y;
            this.vy = v;
        }
    }
}
