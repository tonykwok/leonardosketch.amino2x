package com.joshondesign.amino.examples;

import com.joshondesign.amino.core.*;

import java.awt.geom.Point2D;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Particles2 implements Core.InitCallback {

    private Window window;
    private Slider spreadSlider;
    private Slider gravitySlider;

    private class Particle {

        private double x;
        private double y;
        private double a;
        private double v;
        public double dy;
        public double dx;

        public Particle() {
        }
    }

    private List<Particle> particles = new ArrayList<Particle>();

    public static void main(String ... args) {
        Core.init(new Particles2());
    }

    public void call(Core core) throws Exception {
        window = core.createResizableWindow(1024,700);
        window.setBackgroundFill(AminoColor.BLACK);
        final AminoFont font = core.loadFont(new File("tests/java/com/joshondesign/amino/core/resources/Junction.ttf")).withSize(30);
        spreadSlider = new Slider(core);
        gravitySlider = new Slider(core);

        Node particleLayer = new Node() {
            @Override
            public void draw(GFX gfx) {
                if(particles.size() < 1000) {
                    Particle p = new Particle();
                    resetParticle(window, p);
                    particles.add(p);
                }

                gfx.setPaint(AminoColor.BLACK);

                //clear old particles
                for(Particle part : particles) {
                    gfx.fillRect((int)part.x,(int)part.y,20,20);
                }

                for(Particle part : particles) {
                    //part.v -= gravitySlider.getValue();
                    //part.y += Math.sin(part.a)*part.v;
                    //part.x += Math.cos(part.a)*part.v;
                    part.dy += gravitySlider.getValue()/3f;
                    part.x += part.dx;
                    part.y += part.dy;
                    if(part.y < -40 || part.y > 800) {
                        resetParticle(window, part);
                    }

                }

                gfx.setPaint(AminoColor.WHITE.withAlpha(0.5));
                for(Particle part : particles) {
                    gfx.fillRect((float)part.x,(float)part.y,20f,20f);
                }
            }
        };


        window.setRoot(new Group()
            .add(particleLayer)
            .add(new Text()
                    .setText("Spread:")
                    .setFont(font)
                    .setX(800)
                    .setY(100))
            .add(spreadSlider.setX(800).setY(150))
            .add(new Text()
                    .setText("Gravity:")
                    .setFont(font)
                    .setX(800)
                    .setY(300))
            .add(gravitySlider.setX(800).setY(350))
        );


    }

    private void resetParticle(Window window, Particle p) {
        p.x = window.getWidth()/2;
        p.y = window.getHeight()-200;
        p.v = 6;
        p.a = Math.toRadians(270) + (Math.random()-0.5)*4.0*spreadSlider.getValue();
        p.dy = Math.sin(p.a)*p.v;
        p.dx = Math.cos(p.a)*p.v;
    }

    private static class Slider extends Node {
        private double value = 0.3;

        private Slider(Core core) {
            core.listen(Core.Events.MOUSE_DRAG, this, new Callback<MEvent>() {
                public void call(MEvent mEvent) {
                    double x = mEvent.getX()-getX();
                    double v = x/200.0;
                    setValue(v);
                }
            });
        }

        @Override
        public void draw(GFX gfx) {
            gfx.setPaint(AminoColor.GRAY);
            gfx.translate(this.getX(), this.getY());
            gfx.fillRect(0, 0, 200, 50);
            gfx.setPaint(AminoColor.ORANGE);
            gfx.fillRect(0,0,(int)(200*value),50);
            gfx.translate(-this.getX(), -this.getY());
        }

        @Override
        public boolean contains(Point2D pt) {
            return new Bounds((int)getX(),(int)getY(),200,50).inside(pt);
        }

        public void setValue(double value) {
            if(value >=0 && value <= 1.0) {
                this.value = value;
            }
        }

        public double getValue() {
            return value;
        }
    }
}
