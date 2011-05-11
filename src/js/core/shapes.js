/* 
@class Shape The base of all shapes. Shapes are geometric shapes which have a *fill*, a *stroke*, and *opacity*. They may be filled or unfilled.
@category shape
*/
function Shape() {
    Node.call(this);
    this.hasChildren = function() { return false; }
    
    //@property fill The color, gradient, or texture used to fill the shape. May be set to "" or null.
    this.fill = "red";
    this.setFill = function(fill) {
        this.fill = fill;
        this.setDirty();
        return this;
    };
    this.getFill = function() {
        return this.fill;
    };
    
    //@property strokeWidth The width of the shape's outline stroke. Set it to 0 to not draw a stroke.
    this.strokeWidth = 0;
    this.setStrokeWidth = function(sw) {  this.strokeWidth = sw;  this.setDirty();  return this;  };
    this.getStrokeWidth = function() { return this.strokeWidth; };
    
    //@property stroke  The color of the stroke. Will only be drawn if `strokeWidth` is greater than 0.
    this.stroke = "black";
    this.setStroke = function(stroke) { this.stroke = stroke; return this; }
    this.getStroke = function() { return this.stroke; }
    
    return true;
}
Shape.extend(Node);





/* 
@class Text A shape which draws a single line of text with the specified content (a string), font, and color.
@category shape
*/
function Text() {
    Shape.call(this);
    //this.parent = null;
    
    //@property x  The X coordinate of the left edge of the text.
    this.x = 0;
    this.setX = function(x) { this.x = x; this.setDirty(); return this; };
    
    //@property y  The Y coordinate of the bottom edge of the text.
    this.y = 0;
    this.setY = function(y) { this.y = y; this.setDirty(); return this; };
    
    //@property text The actual text string which will be drawn.
    this.text = "-no text-";
    this.setText = function(text) { this.text = text;  this.setDirty();  return this;  };
    
    
    this.draw = function(ctx) {
        var f = ctx.font;
        ctx.font = this.font;
        ctx.fillStyle = this.fill;
        ctx.fillText(this.text,this.x,this.y);
        ctx.font = f;
        this.clearDirty();
    };
    
    //@property font  The font description used to draw the text. Use the CSS font format. ex: *20pt Verdana*
    this.font = "20pt Verdana";
    this.setFont = function(font) { this.font = font; this.setDirty(); return this; }
    
    this.contains = function() { return false; }
    return true;    
};
Text.extend(Shape);






/*
@class Circle  A circle shape.
@category shape
*/
function Circle() {
    Shape.call(this);
    
    //@property x  The X coordinate of the *center* of the circle (not it's left edge).
    this.x = 0.0;
    this.getX = function() { return this.x; };
    this.setX = function(x) { this.x = x; this.setDirty(); return this; };
    
    //@property y  The Y coordinate of the *center* of the circle (not it's top edge).
    this.y = 0.0;
    this.getY = function() { return this.y; };
    this.setY = function(y) { this.y = y; this.setDirty(); return this; };
    
    //@property radius The radius of the circle
    this.radius = 10.0;
    this.getRadius = function() { return this.radius; };
    
    var self = this;
    
    //@method Set the x, y, and radius of the circle all in one step
    this.set = function(x,y,radius) {
        self.x = x;
        self.y = y;
        self.radius = radius;
        self.setDirty();
        return self;
    };
    
    //this.fill = "black";
    /*
    this.setFill = function(fill) {
        self.fill = fill;
        self.setDirty();
        return self;
    };*/
    
    this.draw = function(ctx) {
        if(!this.isVisible()) return;
        ctx.fillStyle = self.fill;
        ctx.beginPath();
        ctx.arc(self.x, self.y, self.radius, 0, Math.PI*2, true); 
        ctx.closePath();
        ctx.fill();
        if(self.getStrokeWidth() > 0) {
            ctx.strokeStyle = self.getStroke();
            ctx.lineWidth = self.getStrokeWidth();
            ctx.stroke();
        }
        this.clearDirty();
    };
    this.contains = function(x,y) {
        if(x >= this.x-this.radius && x <= this.x + this.radius) {
            if(y >= this.y-this.radius && y<=this.y + this.radius) {
                return true;
            }
        }
        return false;
    };
    this.getVisualBounds = function() {
        return new Bounds(this.x-this.radius
            ,this.y-this.radius
            ,this.radius*2
            ,this.radius*2);
    };
    return true;
};
Circle.extend(Shape);



/*
@class Rect A rectangular shape. May be rounded or have straight corners.
@category shape
*/
function Rect() {
    Shape.call(this);
    
    //@method Set the x, y, w, h at the same time.
    this.set = function(x,y,w,h) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        this.setDirty();
        return this;
    };
    
    //@property width The width of the rectangle.
    this.width = 100.0;
    this.getWidth = function() { return this.width; };
    this.setWidth = function(w) {
        this.width = w;
        this.setDirty();
        return this;
    };
    
    //@property height The height of the rectangle.
    this.height = 100.0;
    this.getHeight = function() { return this.height; };
    this.setHeight = function(h) {
        this.height = h;
        this.setDirty();
        return this;
    };
    
    //@property x The X coordinate of the rectangle.
    this.x = 0.0;
    this.setX = function(x) {
        this.x = x;
        this.setDirty();
        return this;
    };
    this.getX = function() { return this.x; };
    
    //@property y The Y coordinate of the rectangle.
    this.y = 0.0;
    this.setY = function(y) {
        this.y = y;
        this.setDirty();
        return this;
    };
    this.getY = function() { return this.y; };
    
    
    //@property corner  The radius of the corner, if it's rounded. The radius is the same for all corners. If zero, then the rectangle will not be rounded.
    this.corner = 0;
    this.setCorner = function(c) {
        this.corner = c;
        this.setDirty();
        return this;
    };
    
    this.contains = function(x,y) {
        //console.log("comparing: " + this.x + " " + this.y + " " + this.width + " " + this.height + " --- " + x + " " + y);
        if(x >= this.x && x <= this.x + this.width) {
            if(y >= this.y && y<=this.y + this.height) {
                return true;
            }
        }
        return false;
    };
    this.draw = function(ctx) {
        ctx.fillStyle = this.fill;
        if(this.corner > 0) {
            var x = this.x;
            var y = this.y;
            var w = this.width;
            var h = this.height;
            var r = this.corner;
            ctx.beginPath();
            ctx.moveTo(x+r,y);
            ctx.lineTo(x+w-r, y);
            ctx.bezierCurveTo(x+w-r/2,y,   x+w,y+r/2,   x+w,y+r);
            ctx.lineTo(x+w,y+h-r);
            ctx.bezierCurveTo(x+w,y+h-r/2, x+w-r/2,y+h, x+w-r, y+h);
            ctx.lineTo(x+r,y+h);
            ctx.bezierCurveTo(x+r/2,y+h,   x,y+h-r/2,   x,y+h-r);
            ctx.lineTo(x,y+r);
            ctx.bezierCurveTo(x,y+r/2,     x+r/2,y,     x+r,y);
            ctx.closePath();
            ctx.fill();
            if(this.strokeWidth > 0) {
                ctx.lineWidth = this.strokeWidth;
                ctx.stroke();
            }
        } else {
            ctx.fillRect(this.x,this.y,this.width,this.height);
            if(this.strokeWidth > 0) {
                ctx.lineWidth = this.strokeWidth;
                ctx.strokeRect(this.x,this.y,this.width,this.height);
            }
        }
        this.clearDirty();
    };
    this.getVisualBounds = function() {
        return new Bounds(this.x,this.y,this.width,this.height);
    };
    return true;
};
Rect.extend(Shape);



var SEGMENT_MOVETO = 1;
var SEGMENT_LINETO = 2;
var SEGMENT_CLOSETO = 3;
var SEGMENT_CURVETO = 4;
function Segment(kind,x,y,a,b,c,d) {
    this.kind = kind;
    this.x = x;
    this.y = y;
    if(kind == SEGMENT_CURVETO) {
        this.cx1 = x;
        this.cy1 = y;
        this.cx2 = a;
        this.cy2 = b;
        this.x = c;
        this.y = d;
    }
};

/*
@class Path A Path is a sequence of line and curve segments. It is used for drawing arbitrary shapes and animating.  Path objects are immutable. You should create them and then reuse them.
@category resource
*/
function Path() {
    this.segments = [];
    this.closed = false;
    
    //@doc jump directly to the x and y. This is usually the first thing in your path.
    this.moveTo = function(x,y) { this.segments.push(new Segment(SEGMENT_MOVETO,x,y)); return this; };
    
    //@doc draw a line from the previous x and y to the new x and y.
    this.lineTo = function(x,y) { this.segments.push(new Segment(SEGMENT_LINETO,x,y)); return this; };
    
    //@doc close the path. It will draw a line from the last x,y to the first x,y if needed.
    this.closeTo = function(x,y) {
        this.segments.push(new Segment(SEGMENT_CLOSETO,x,y)); 
        this.closed = true;
        return this;
    };
    
    //@doc draw a beizer curve from the previous x,y to a new point (x2,y2) using the four control points (cx1,cy1,cx2,cy2).
    this.curveTo = function(cx1,cy1,cx2,cy2,x2,y2) {
        this.segments.push(new Segment(SEGMENT_CURVETO,cx1,cy1,cx2,cy2,x2,y2));
        return this;
    };
    
    //@doc build the final path object.
    this.build = function() {
        return this;
    };
    
    this.pointAtT = function(fract) {
        if(fract >= 1.0 || fract < 0) return [0,0];

        var segIndex = 0;
        segIndex = Math.floor(fract*(this.segments.length-1));
        var segFract = (fract*(this.segments.length-1))-segIndex;
        //console.log("seg index = " + (segIndex+1) + " f=" + fract + " sgf=" + segFract);// + " type=" + this.segments[segIndex+1].kind);
        var seg = this.segments[segIndex+1];
        var prev;
        var cur;
        switch (seg.kind) {
            case SEGMENT_MOVETO: return [0,0];
            case SEGMENT_LINETO:
                prev = this.segments[segIndex];
                cur = seg;
                return this.interpLinear(prev.x,prev.y,cur.x,cur.y,segFract);
            case SEGMENT_CURVETO:
                prev = this.segments[segIndex];
                cur = seg;
                return this.interpCurve(prev.x,prev.y,cur.cx1,cur.cy1,cur.cx2,cur.cy2, cur.x, cur.y,segFract);
            case SEGMENT_CLOSETO:
                prev = this.segments[segIndex];
                cur = this.segments[0];
                return this.interpLinear(prev.x,prev.y,cur.x,cur.y,segFract);
        }
        return [10,10];
    };

    this.interpLinear = function(x1, y1, x2, y2, fract) {
        return [ (x2-x1)*fract + x1, (y2-y1)*fract + y1 ];
    }
    
    this.interpCurve = function( x1, y1, cx1, cy1, cx2, cy2, x2, y2, fract) {
        return getBezier(fract, [x2,y2], [cx2,cy2], [cx1,cy1], [x1,y1] );
    }
    
    return true;
};

function B1(t) { return t*t*t; }
function B2(t) { return 3*t*t*(1-t); }
function B3(t) { return 3*t*(1-t)*(1-t); }
function B4(t) { return (1-t)*(1-t)*(1-t); }
function getBezier(percent, C1, C2, C3, C4) {
    var pos = [];
    pos[0] = C1[0]*B1(percent) + C2[0]*B2(percent) + C3[0]*B3(percent) + C4[0]*B4(percent);
    pos[1] = C1[1]*B1(percent) + C2[1]*B2(percent) + C3[1]*B3(percent) + C4[1]*B4(percent);
    return pos;
}


/*
@class PathNode  Draws a path.
@category shape
*/
function PathNode() {
    Shape.call(this);
    //@property path  the Path to draw
    this.path = null;
    this.setPath = function(path) {
        this.path = path;
        this.setDirty();
        return this;
    };
    this.getPath = function() {
        return this.path;
    };
    
    this.draw = function(ctx) {
        if(!this.isVisible()) return;
        ctx.fillStyle = this.fill;
        ctx.beginPath();
        for(var i=0; i<this.path.segments.length; i++) {
            var s = this.path.segments[i];
            if(s.kind == SEGMENT_MOVETO) 
                ctx.moveTo(s.x,s.y);
            if(s.kind == SEGMENT_LINETO) 
                ctx.lineTo(s.x,s.y);
            if(s.kind == SEGMENT_CURVETO)
                ctx.bezierCurveTo(s.cx1,s.cy1,s.cx2,s.cy2,s.x,s.y);
            if(s.kind == SEGMENT_CLOSETO)
                ctx.closePath();
        }
        if(this.path.closed) {
            ctx.fill();
        }
        if(this.strokeWidth > 0) {
            ctx.strokeStyle = this.stroke;
            ctx.lineWidth = this.strokeWidth;
            ctx.stroke();
            ctx.lineWidth = 1;
        }
        this.clearDirty();
    };
    return true;
};
PathNode.extend(Shape);





