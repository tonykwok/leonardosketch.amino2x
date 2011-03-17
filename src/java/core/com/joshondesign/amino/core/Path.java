package com.joshondesign.amino.core;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 3/14/11
 * Time: 5:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class Path {

    private List<Segment> segments;

    private Path(List<Segment> segments) {
        this.segments = segments;
    }

    public static PathBuilder moveTo(double x, double y) {
        PathBuilder path = new PathBuilder();
        path.append(new Segment(Type.MoveTo,x,y));
        return path;
    }

    public Path2D toPath2D() {
        Path2D pth = new Path2D.Double();
        for(Segment s : segments) {
            //u.p("seg = " + s.type);
            switch(s.type) {
                case MoveTo: pth.moveTo(s.x,s.y); break;
                case LineTo: pth.lineTo(s.x,s.y); break;
                case CloseTo: pth.closePath(); break;
                case CurveTo: pth.curveTo(s.cx1,s.cy1, s.cx2,s.cy2,s.x,s.y); break;
                default: throw new Error("unknown segment type");
            }
        }
        return pth;
    }

    public Point2D pointAtT(double fract) {
        if(fract >= 1.0 || fract < 0) return new Point2D.Double(0,0);

        int segIndex = (int)(fract*(segments.size()-1));
        double segFract = (fract*(segments.size()-1))-segIndex;
        //u.p("seg index = " + (segIndex+1) + " f=" + fract + " sgf=" + segFract + " type=" + segments.get(segIndex+1).type);
        Segment seg = segments.get(segIndex+1);
        Segment prev;
        Segment cur;
        switch (seg.type) {
            case MoveTo: return new Point2D.Double(0,0);
            case LineTo:
                prev = segments.get(segIndex);
                cur = seg;
                //u.p("prev = " + prev.type + " " + prev.x + " " + prev.y);
                //u.p("cur = " + cur.type + " " + cur.x + " " + cur.y);
                return interpLinear(prev.x,prev.y,cur.x,cur.y,segFract);
            case CurveTo:
                prev = segments.get(segIndex);
                cur = seg;
                //u.p("prev = " + prev.type + " " + prev.x + " " + prev.y);
                //u.p("cur = " + cur.type + " " + cur.x + " " + cur.y + " " + cur.cx1 + " " + cur.cy1 + " " + cur.cx2 + "  " + cur.cy2);
                return interpCurve(prev.x,prev.y,cur.cx1,cur.cy1,cur.cx2,cur.cy2, cur.x, cur.y,segFract);
            case CloseTo:
                prev = segments.get(segIndex);
                cur = segments.get(0);
                //u.p("prev = " + prev.type + " " + prev.x + " " + prev.y);
                //u.p("cur = " + cur.type + " " + cur.x + " " + cur.y);
                return interpLinear(prev.x,prev.y,cur.x,cur.y,segFract);
        }
        return new Point2D.Double(10,10);
    }

    private Point2D interpCurve(double x1, double y1, double cx1, double cy1, double cx2, double cy2, double x2, double y2, double fract) {
        return getBezier(fract,
                new Point2D.Double(x2,y2),
                new Point2D.Double(cx2,cy2),
                new Point2D.Double(cx1,cy1),
                new Point2D.Double(x1,y1)
                );
    }

    private Point2D interpLinear(double x1, double y1, double x2, double y2, double fract) {
        return new Point2D.Double(
                (x2-x1)*fract + x1,
                (y2-y1)*fract + y1
        );
    }


    public static class PathBuilder {
        private List<Segment> segments;

        public PathBuilder() {
            segments = new ArrayList<Segment>();
        }
        private void append(Segment segment) {
            segments.add(segment);
        }

        public PathBuilder lineTo(double x, double y) {
            append(new Segment(Type.LineTo,x,y));
            return this;
        }

        public PathBuilder closeTo() {
            append(new Segment(Type.CloseTo));
            return this;
        }

        public PathBuilder curveTo(double cx1, double cy1, double cx2, double cy2, double x2, double y2) {
            append(new Segment(Type.CurveTo,cx1,cy1,cx2,cy2,x2,y2));
            return this;
        }

        public Path build() {
            return new Path(segments);
        }

    }

    enum Type {
        LineTo, CloseTo, CurveTo, MoveTo
    }

    static class Segment {
        private Type type;
        private double x;
        private double y;
        private double cx1;
        private double cy1;
        private double cx2;
        private double cy2;

        public Segment(Type type, double x, double y) {
            this.type = type;
            this.x = x;
            this.y = y;
        }

        public Segment(Type type) {
            this.type = type;
        }

        public Segment(Type type, double cx1, double cy1, double cx2, double cy2, double x2, double y2) {
            this.type = type;
            this.cx1 = cx1;
            this.cy1 = cy1;
            this.cx2 = cx2;
            this.cy2 = cy2;
            this.x = x2;
            this.y = y2;
        }
    }

    private static double B1(double t) { return t*t*t; }
    private static double B2(double t) { return 3*t*t*(1-t); }
    private static double B3(double t) { return 3*t*(1-t)*(1-t); }
    private static double B4(double t) { return (1-t)*(1-t)*(1-t); }

    private Point2D getBezier(double percent,
                              Point2D.Double C1,
                              Point2D.Double C2,
                              Point2D.Double C3,
                              Point2D.Double C4) {
        Point2D.Double pos = new Point2D.Double();
        pos.x = C1.x*B1(percent) + C2.x*B2(percent) + C3.x*B3(percent) + C4.x*B4(percent);
        pos.y = C1.y*B1(percent) + C2.y*B2(percent) + C3.y*B3(percent) + C4.y*B4(percent);
        return pos;
    }

}
