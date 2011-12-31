package com.joshondesign.amino.core;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 3/14/11
 * Time: 11:27 AM
 * To change this template use File | Settings | File Templates.
 */
public class DragBlocks implements Core.InitCallback {

    static class BlockImageView extends ImageView {
        BlockImageView(URL src) throws IOException {
            super(src);
        }

        public boolean solved = false;
    }
    public static void main(String ... args) throws IOException {
        Core.init(new DragBlocks());
    }

    public void call(Core core) throws Exception {
        Window window = core.createResizableWindow(1024, 740);
        window.setBackgroundFill(AminoColor.BLACK);

        BlockImageView bg = new BlockImageView(DragBlocks.class.getResource("images/background.png"));
        final BlockImageView block1 = new BlockImageView(DragBlocks.class.getResource("images/block1.png"));
        block1.setX(730).setY(450);
        block1.solved = false;
        final BlockImageView block2 = new BlockImageView(DragBlocks.class.getResource("images/block2.png"));
        block2.setX(550).setY(200);
        block2.solved = false;
        final BlockImageView block3 = new BlockImageView(DragBlocks.class.getResource("images/block3.png"));
        block3.setX(300).setY(500);
        block3.solved = false;


        final Shape block1spot = new Shape() {
            @Override
            public void draw(GFX g) {
                //Graphics2D ctx = (Graphics2D) g.create();
                g.setPaint(AminoColor.BLACK);
                /*
                ctx.setComposite(AlphaComposite.SrcOver.derive(0.5f));
                ctx.translate(105 + 5, 60 + 10);
                Path2D pth = new Path2D.Double();
                pth.moveTo(0,109);
                pth.lineTo(192,0);
                pth.lineTo(192,219);
                pth.closePath();
                ctx.fill(pth);
                ctx.dispose();
                */
            }
        };
        block1spot.setX(105);
        block1spot.setY(60);


        final Shape block2spot = new Shape() {
            @Override
            public void draw(GFX g) {
                /*
                Graphics2D ctx = (Graphics2D) g.create();
                ctx.setPaint(Color.BLACK);
                ctx.setComposite(AlphaComposite.SrcOver.derive(0.5f));
                ctx.translate(105+5,360+10);
                Path2D pth = new Path2D.Double();
                pth.moveTo(0, 2);
                pth.lineTo(112, 2);
                pth.lineTo(112,219);
                pth.lineTo(0,219);
                pth.closePath();
                ctx.fill(pth);
                ctx.dispose();
                */
            }
        };
        block2spot.setX(105);
        block2spot.setY(360);

        final Shape block3spot = new Shape() {
            @Override
            public void draw(GFX g) {
                /*
                Graphics2D ctx = (Graphics2D) g.create();
                ctx.setPaint(Color.BLACK);
                ctx.setComposite(AlphaComposite.SrcOver.derive(0.5f));
                ctx.translate(705 + 8, 60 + 10);
                double r = 83;
                ctx.fillOval(0, 0, (int) r * 2, (int) r * 2);
                ctx.dispose();
                */
            }
        };
        block3spot.setX(705);
        block3spot.setY(60);


        final java.util.List<BlockImageView> blocks = new ArrayList();
        blocks.add(block1);
        blocks.add(block2);
        blocks.add(block3);

        final Group solvedOverlay = new Group();
        solvedOverlay.add(new Rect()
                .set(100, 100, 1024 - 200, 740 - 200)
                .setFill(new AminoColor(100, 100, 100, 128)));//"rgba(100,100,100,0.5)"));
        AminoFont font = core.loadFont("Georgia").withSize(100);
        solvedOverlay.add(new Text()
                .setText("Solved")
                .setFont(font)
                .setFill(AminoColor.WHITE)
                .setX(320)
                .setY(390));
        solvedOverlay.setVisible(false);

        Group blocksGroup = new Group().add(block1).add(block2).add(block3);
        core.root = new Group()
                .add(bg)
                .add(block1spot).add(block2spot).add(block3spot)
                .add(blocksGroup)
                .add(solvedOverlay);

        core.addCallback(new Callback() {
            public void call(Object o) {
                boolean s = true;
                for (int i = 0; i < blocks.size(); i++) {
                    if (!blocks.get(i).solved) {
                        s = false;
                    }
                }
                if (s) {
                    if (!solvedOverlay.isVisible()) {
                        solvedOverlay.setVisible(true);
                    }
                }
            }
        });


        new Dragger(core,block1,block1spot);
        new Dragger(core,block2,block2spot);
        new Dragger(core,block3,block3spot);

        core.start();
    }

    static class Dragger {
        double sx = 0;
        double sy = 0;
        boolean started = false;
        public Dragger(final Core runner, final BlockImageView target, final Node spot) {
            runner.listen("MOUSE_PRESS", null, new Callback<MEvent>() {
                public void call(MEvent e) {
                    if(e.node == target) {
                        started = true;
                        Group p = (Group) target.getParent();
                        p.remove(target);
                        p.add(target);
                    }
                    sx = e.x;
                    sy = e.y;
                }
            });
            runner.listen("MOUSE_DRAG", null, new Callback<MEvent>() {
                public void call(MEvent e) {
                    if(e.node != null && started) {
                        double dx = e.x-sx;
                        double dy = e.y-sy;
                        sx = e.x;
                        sy = e.y;
                        double x = e.node.getX() + dx;
                        double y = e.node.getY() + dy;
                        e.node.setX(x);
                        e.node.setY(y);
                        if(within(target.getX(),spot.getX(),10)) {
                            if(within(target.getY(),spot.getY(),10)) {
                                target.setX(spot.getX());
                                target.setY(spot.getY());
                            }
                        }
                    }
                }
            });

            runner.listen("MOUSE_RELEASE", null, new Callback<MEvent>() {
                public void call(MEvent e) {
                    started = false;
                    if(within(target.getX(),spot.getX(),10)) {
                        if(within(target.getY(),spot.getY(),10)) {
                            target.solved = true;
                        }
                    }
                }
            });

        };

        boolean within(double a,double b,double thresh) {
            return Math.abs(b-a) <= thresh;
        }
    }
}
