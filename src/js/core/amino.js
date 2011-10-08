/*
@overview Amino: JavaScript Scenegraph

Amino is a scenegraph for drawing 2D graphics in JavaScript with the
HTML 5 Canvas API. By creating a tree of nodes, you can draw shapes, text, images special effects; complete with transforms and animation.
Amino takes care of all rendering, animation, and event handling
so you can build *rich* interactive graphics with very little code.
Using Amino is much more convenient than writing canvas code by hand.

Here's a quick example:    

    <canvas id="can" width="200" height="200"></canvas>
    <script>
    
    //attach a runner to the canvas
    var can = document.getElementById("can");
    var runner = new Runner().setCanvas(can);
    
    //create a rect and a circle
    var r = new Rect().set(0,0,50,50).setFill("green");
    var c = new Circle().set(100,100,30).setFill("blue");
    
    //add the shapes to a group
    var g = new Group().add(r).add(c);
    
    //make the rectangle go left and right every 5 seconds
    var anim = new Anim(g,"x",0,150,5);
    runner.addAnim(anim);
    
    //set the group as the root of the scenegraph, then start
    runner.root = g;
    runner.start();
    
    </script>

A note on properties. Most objects have properties like `x` or `width`.
Properties are accessed with getters.  For example, to access the `width`
property on a rectangle, call `rect.getWidth()`. Properties are set 
with setters. For example, to set the `width` property
on a rectangle, call `rect.setWidth(100)`. Most functions, especially 
property setters, are chainable. This means you
can set a bunch of properties at once like this:

    var c = new Rect()
        .setX(50)
        .setY(50)
        .setWidth(100)
        .setHeight(200)
        .setFill("green")
        .setStrokeWidth(5)
        .setStroke("black")
        ;
    
@end
*/

var win = window;
//@language javascript
var ROTATE_BACKWARDS = false;
if (window.PalmSystem) {
    ROTATE_BACKWARDS = true;
}



// 'extend' is From Jo lib, by Dave Balmer
// syntactic sugar to make it easier to extend a class
Function.prototype.extend = function(superclass, proto) {
	// create our new subclass
	this.prototype = new superclass();

	// optional subclass methods and properties
	if (proto) {
		for (var i in proto)
			this.prototype[i] = proto[i];
	}
};


var DEBUG = true;
var tabcount = 0;
function indent() {
    tabcount++;
}
function outdent() {
    tabcount--;
}
function p(s) {
    if(DEBUG) {
        var tab = "";
        for(i=0;i<tabcount;i++) {
            tab = tab + "  ";
        }
        console.log(tab+s);
    }
}




/*
@class Node The base class for all nodes. All nodes have a parent and can potentially have children if they implement *hasChildren*.
@category shape
*/
__node_hash_counter = 0;
function Node() {
    __node_hash_counter++;
    this._hash = __node_hash_counter;
    var self = this;
    
    //@property parent Get the parent of this node, or null if there is no parent.  A node not yet put into the scene may not have a parent. The top most node may not have a parent.
    this.parent = null;
    this.setParent = function(parent) { this.parent = parent; return this; };
    this.getParent = function() { return this.parent; };
    
    //@property visible Indicates if the node is visible. Non-visible nodes are not drawn on screen.      non visible nodes cannot intercept click events.
    this.visible = true;
    this.setVisible = function(visible) {
        self.visible = visible;
        self.setDirty();
        return self;
    };
    this.isVisible = function() {
        return this.visible;
    };
    
    //@property cached Indicates if the node should be automatically cached an a buffer. false by default
    this.cached = false;
    this.setCached = function(cached) {
        self.cached = cached;
        self.setDirty();
        return self;
    };
    this.isCached = function() {
        return self.cached;
    };
    
    
    // @property blocksMouse Indicates if this node will block mouse events from hitting nodes beneath it.
    this.blocksMouse = false;
    this.isMouseBlocked = function() {
        return this.blocksMouse;
    };
    this.setMouseBlocked = function(m) {
        this.blocksMouse = m;
        return this;
    };
    
    this.dirty = true;
    //@doc Marks this node as dirty, so it is scheduled to be redrawn
    this.setDirty = function() {
        this.dirty = true;
        if(this.getParent()) {
            this.getParent().setDirty();
        }
    };
    //@doc Returns if this node is dirty, meaning it still needs to be completely redrawn
    this.isDirty = function() {
        return self.dirty;
    };
    //@doc Clears the dirty bit. usually this is called by the node itself after it redraws itself
    this.clearDirty = function() {
        self.dirty = false;
    };
    //@method by default nodes don't contain anything
    this.contains = function(x,y) { return false; }
    //@method by default nodes don't have children
    this.hasChildren = function() { return false; }
    return true;
}

/*
@class Bounds  Represents the maximum bounds of something, usually the visible bounds of a node.
@category resource
*/
function Bounds(x,y,w,h) {
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
    var self = this;
    
    
    //@property x Return the x coordinate of the bounds.
    this.getX = function() { return this.x; };
    //@property y Return the y coordinate of the bounds.
    this.getY = function() { return this.y; };
    //@property width Return the width of the bounds.
    this.getWidth = function() { return this.w; }
    //@property height Return the height of the bounds.
    this.getHeight = function() { return this.h; }
    
    this.intersects = function(b) {
        
        var left = self.x;
        var top = self.y;
        var right = self.x + self.w;
        var bottom = self.y + self.h;
        
        var bleft = b.x;
        var btop = b.y;
        var bright = b.x + b.w;
        var bbottom = b.y + b.h;
        
        if(bleft < left && bright > right) return true;
        
        //too far left
        if(bright < left) return false;
        
        //too far right
        if(bleft > right) return false;
        
        //too high
        if(bbottom < top) return false;
        
        //too low
        if(btop > bottom) return false;
        
        
        
        return true;
    }
    
    this.union = function(b) {
        var al = self.x;
        var ar = self.x+self.w;
        var at = self.y;
        var ab = self.y+self.h;
        var bl = b.x;
        var br = b.x+b.w;
        var bt = b.y;
        var bb = b.y+b.h;
        
        var l = Math.min(al,bl);
        var r = Math.max(ar,br);
        var t = Math.min(at,bt);
        var b = Math.max(ab,bb);
        return new Bounds(l,t,r-l,b-t);
    }
     
    
    this.translate = function(x,y) {
        return new Bounds(self.x+x,self.y+y,self.w,self.h);
    }
    
    this.toString = function() {
        return "Bounds: " + x + ","+y+" "+w+"x"+h;
    }
    
    return true;
};

/*
@class Buffer An offscreen area that you can draw into. Used for special effects and caching.
@category resource
*/
function Buffer(w,h) {
    var self = this;    
    //@property width  The width of the buffer, set at creation time.
    this.w = w;
    this.getWidth = function() { return this.w; }
    
    //@property height  The height of the buffer, set at creation time.
    this.h = h;
    this.getHeight = function() { return this.h; }
    
    this.buffer = document.createElement("canvas");
    this.buffer.width = this.w;
    this.buffer.height = this.h;
    
    //@doc get the Canvas 2D context of the buffer, so you can draw on it
    this.getContext = function() { return self.buffer.getContext('2d'); }
    
    //@doc Get an canvas ImageData structure.
    this.getData = function() {
        var c = this.getContext();
        var data = c.getImageData(0,0,this.getWidth(), this.getHeight());
        return data;
    };
    
    //@method Return the *red* component at the specified x and y.
    this.getR = function(data, x, y) {
        var pi = x+y*data.width;
        return data.data[pi*4+0];
    };
    
    //@method Return the *green* component at the specified x and y.
    this.getG = function(data, x, y) {
        var pi = x+y*data.width;
        return data.data[pi*4+1];
    };
    
    //@method Return the *blue* component at the specified x and y.
    this.getB = function(data, x, y) {
        var pi = x+y*data.width;
        return data.data[pi*4+2];
    };
    
    //@method Return the *alpha* component at the specified x and y.
    this.getA = function(data, x, y) {
        var pi = x+y*data.width;
        return data.data[pi*4+3];
    };
    
    //@method Set the red, green, blue, and alpha components at the specified x and y.
    this.setRGBA = function(data,x,y,r,g,b,a) {
        var pi = (x+y*this.getWidth())*4;
        //console.log("pi = " + pi);
        data.data[pi+0] = r; //alpha
        data.data[pi+1] = g; //red
        data.data[pi+2] = b; //green
        data.data[pi+3] = a; //blue
        return this;
    };
    //@method Set the data structure back into the canvas. This should be the same value you got from *getData()*.
    this.setData = function(data) {
        this.getContext().putImageData(data,0,0);
        return this;
    };
    //@method Clear the buffer with transparent black.
    this.clear = function() {
        var ctx = this.getContext();
        ctx.clearRect(0,0,this.getWidth(),this.getHeight());
        return this;
    };
    return true;
};

/* 
@class BufferNode A node which draws its child into a buffer. Use it to cache children which are expensive to draw.
@category misc
*/
function BufferNode(n) {
	Node.call(this);
	this.node = n;
    this.node.setParent(this);
    this.buf = null;
    var self = this;
    this.draw = function(ctx) {
        var bounds = this.node.getVisualBounds();
        if(!this.buf) {
            this.buf = new Buffer(bounds.getWidth(),bounds.getHeight());
        }
        //redraw the child only if it's dirty
        if(this.isDirty()) {
            var ctx2 = this.buf.getContext();
            ctx2.save();
            ctx2.translate(-bounds.getX(),-bounds.getY());
            this.node.draw(ctx2);
            ctx2.restore();
        }
        ctx.save();
        ctx.translate(bounds.getX(),bounds.getY());
        ctx.drawImage(this.buf.buffer,0,0);
        ctx.restore();
        this.clearDirty();
    };
    return true;
};
BufferNode.extend(Node);




/*
@class Transform Transforms the child inside of it with a translation and/or rotation.
@category misc
*/
function Transform(n) {
    Node.call(this);
    this.node = n;
    this.node.setParent(this);
    var self = this;
    
    //@property translateX translate in the X direction
    this.translateX = 0;
    this.setTranslateX = function(tx) {
        self.translateX = tx;
        self.setDirty();
        return self;
    };
    this.getTranslateX = function() {
        return this.translateX;
    };
    
    //@property translateY translate in the Y direction
    this.translateY = 0;
    this.setTranslateY = function(ty) {
        this.translateY = ty;
        this.setDirty();
        return this;
    };
    this.getTranslateY = function() {
        return this.translateY;
    };
    
    //@property scaleX scale in the X direction
    this.scaleX = 1;
    this.setScaleX = function(sx) {
        this.scaleX = sx;
        this.setDirty();
        return this;
    };
    this.getScaleX = function() {
        return this.scaleX;
    };
        
        
    //@property scaleY scale in the X direction
    this.scaleY = 1;
    this.setScaleY = function(sy) {
        this.scaleY = sy;
        this.setDirty();
        return this;
    };
    this.getScaleY = function() {
        return this.scaleY;
    };
    
    //@property rotate set the rotation, in degrees
    this.rotate = 0;
    this.setRotate = function(rotate) {
        this.rotate = rotate;
        this.setDirty();
        return this;
    };
    this.getRotate = function() {
        return this.rotate;
    };
    
    this.contains = function(x,y) {
        return false;
    };
    this.hasChildren = function() {
        return true;
    };
    this.childCount = function() {
        return 1;
    };
    this.getChild = function(n) {
        return this.node;
    };
    
    this.convertToChildCoords = function(x,y) {
        var x1 = x-this.translateX;
        var y1 = y-this.translateY;
        var a = -this.rotate * Math.PI/180;
        var x2 = x1*Math.cos(a) - y1*Math.sin(a);
        var y2 = x1*Math.sin(a) + y1*Math.cos(a);
        x2 = x2/this.scaleX;
        y2 = y2/this.scaleY;
        return [x2,y2];
    };
    
    this.draw = function(ctx) {
        ctx.save();
        ctx.translate(self.translateX,this.translateY);
        var r = this.rotate % 360;
        if(ROTATE_BACKWARDS) {
            r = 360-r;
        }
        ctx.rotate(r*Math.PI/180.0,0,0);
        if(self.scaleX != 1 || self.scaleY != 1) {
            ctx.scale(self.scaleX,self.scaleY);
        }
        this.node.draw(ctx);
        ctx.restore();
        this.clearDirty();
    };
    
    this.getVisualBounds = function() {
        if(!this.node.getVisualBounds) return null;
        var b = this.node.getVisualBounds();
        if(b == null) return null;
        return this.node.getVisualBounds().translate(this.getTranslateX(),this.getTranslateY());
    }
    
    return true;
}
Transform.extend(Node);





/*
@class Group A parent node which holds an ordered list of child nodes. It does not draw anything by itself, but setting visible to false will hide the children.
@category shape
*/

function Group() {
    Node.call(this);
    this.children = [];
    this.parent = null;
    var self = this;
    
    //@property x set the x coordinate of the group.
    this.x = 0;
    this.setX = function(x) {
        self.x = x;
        self.setDirty();
        return self;
    };
    this.getX = function() {
        return self.x;
    };
    
    //@property y set the y coordinate of the group.
    this.y = 0;
    this.setY = function(y) {
        self.y = y;
        self.setDirty();
        return self;
    };
    this.getY = function() {
        return self.y;
    };
    
    this.opacity = 1.0;
    this.setOpacity = function(o) {
        self.opacity = o;
        return self;
    };
    this.getOpacity = function() {
        return self.opacity;
    };
    
    //@method Add the child `n` to this group.
    this.add = function(n) {
        self.children[self.children.length] = n;
        n.setParent(self);
        self.setDirty();
        return self;
    };
    //@method Remove the child `n` from this group.
    this.remove = function(n) {
        var i = self.children.indexOf(n);
        if(i >= 0) {
            self.children.splice(i,1);
            n.setParent(null);
        }
        self.setDirty();
        self._bounds = null;
        return self;
    };
    
    this.draw = function(ctx) {
        if(!self.isVisible()) return;
        indent();
        var ga = ctx.globalAlpha;
        ctx.globalAlpha = self.opacity;
        ctx.translate(self.x,self.y);
        for(var i=0; i<self.children.length;i++) {
            self.children[i].draw(ctx);
        }
        ctx.translate(-self.x,-self.y);
        ctx.globalAlpha = ga;
        outdent();
        this.clearDirty();
    };
    //@method Remove all children from this group.
    this.clear = function() {
        self.children = [];
        self.setDirty();
        return self;
    };
    //@method Always returns false. You should call contains on the children instead.
    this.contains = function(x,y) {
        return false;
    };
    //@method Always returns true, whether or not it actually has children at the time.
    this.hasChildren = function() {
        return true;
    };
    //@method Convert the `x` and `y` in to child coordinates.
    this.convertToChildCoords = function(x,y) {
        return [x-self.x,y-self.y];
    };
    //@method Returns the number of child nodes in this group.
    this.childCount = function() {
        return self.children.length;
    };
    //@method Returns the child node at index `n`.
    this.getChild = function(n) {
        return self.children[n];
    };
    
    this._bounds = null;
    this.getVisualBounds = function() {
        if(self.children.length <= 0) {
            return null;
        }
        
        if(self._bounds == null) {
            for(var i=0; i<self.children.length; i++) {
                var child = self.children[i];
                if(!child.getVisualBounds) {
                    if(child instanceof Group) {
                    } else {
                        //console.log("warning. no visual bounds: ");
                        //console.log(self.children[i]);
                    }
                    continue;
                }
                var b = child.getVisualBounds();
                if(b == null) {
                    if(child instanceof Group) {
                    } else {
                        //console.log("warning, vis bounds is null");
                        //console.log(self.children[i]);
                    }
                    continue;
                }
                if(self._bounds == null) {
                    self._bounds = b;
                } else {
                    self._bounds = self._bounds.union(b);
                }
            }
            if(self._bounds != null) {
                self._bounds = self._bounds.translate(self.x,self.y);
            }
        }
        return self._bounds;
    };
    
    this.setDirty = function() {
        self.dirty = true;
        self._bounds = null;
        if(self.getParent()) {
            self.getParent().setDirty();
        }
    };
    return true;
};
Group.extend(Node, {});





/*
@class ImageView  A node which draws an image. Takes a string URL in it's constructor. ex: new ImageView("blah.png")
@category shape
*/
function ImageView(url) {
    Node.call(this);
    var self = this;
    if(url instanceof Image) {
        this.img = url;
        this.loaded = true;
        this.width = url.width;
        this.height = url.height;
    } else {
        this.src = url;
        this.img = new Image();
        this.loaded = false;
        this.width = 10;
        this.height = 10;
        this.img.onload = function() {
            console.log("loaded");
            self.loaded = true;
            self.setDirty();
            self.width = self.img.width;
            self.height = self.img.height;
            console.log("self = " + self.width + " " + self.height);
        }
        this.img.src = url;
    }
    
    //@property x  The Y coordinate of the upper left corner of the image.
    this.x = 0.0;
    this.setX = function(x) { this.x = x;   this.setDirty();  return this;  };
    this.getX = function() { return this.x; };
    
    //@property y  The Y coordinate of the upper left corner of the image.
    this.y = 0.0;
    this.setY = function(y) {  this.y = y;  this.setDirty();  return this;  };
    this.getY = function() { return this.y; };
    
    
    
    this.hasChildren = function() { return false; }
    
    this.draw = function(ctx) {
        //self.loaded = false;
        if(self.loaded) {
            ctx.drawImage(self.img,self.x,self.y);
        } else {
            ctx.fillStyle = "red";
            ctx.fillRect(self.x,self.y,100,100);
        }
        self.clearDirty();
    };
    this.contains = function(x,y) {
        if(x >= this.x && x <= this.x + this.width) {
            if(y >= this.y && y<=this.y + this.height) {
                return true;
            }
        }
        return false;
    };
    this.getVisualBounds = function() {
        return new Bounds(this.x,this.y,this.width,this.height);
    };
    return true;
};
ImageView.extend(Node);



function LinearGradientFill(x,y,width,height) {
    var self = this;
    self.x = x;
    self.y = y;
    self.width = width;
    self.height = height;
    self.offsets = [];
    self.colors = [];
    self.addStop = function(offset, color) {
        self.offsets.push(offset);
        self.colors.push(color);
        return self;
    };
    self.generate = function(ctx) {
        var grad = ctx.createLinearGradient(self.x,self.y,self.width,self.height);
        for(var i in self.offsets) {
            grad.addColorStop(self.offsets[i],self.colors[i]);
        }
        return grad;
    }
};



function RadialGradientFill(x,y,radius) {
    var self = this;
    self.x = x;
    self.y = y;
    self.radius = radius;
    self.offsets = [];
    self.colors = [];
    self.addStop = function(offset, color) {
        self.offsets.push(offset);
        self.colors.push(color);
        return self;
    };
    self.generate = function(ctx) {
        var grad = ctx.createRadialGradient(self.x,self.y, 0, self.x, self.y, self.radius);
        for(var i in self.offsets) {
            grad.addColorStop(self.offsets[i],self.colors[i]);
        }
        return grad;
    }
};

function PatternFill(url, repeat) {
    var self = this;
    
    self.src = url;
    self.img = new Image();
    self.loaded = false;
    self.repeat = repeat;
    self.img.onload = function() {
        console.log("pattern loaded");
        self.loaded = true;
    };
    self.img.src = self.src;
    self.generate = function(ctx) {
        if(!self.loaded) {
            return "red";
        }
        return ctx.createPattern(self.img, self.repeat);
    };
    return true;
}


/*
 basic painting routine. just recursively draw
*/
function SimplePaintStrategy() {
    this.paintScene = function(ctx,runner) {
        var root = runner.root;
        if(root == null) return;
        root.draw(ctx);
    };
}

/*
 bounds painting routine. doesn't draw shapes which are outside the bounds of the viewport
*/
function BoundsPaintStrategy() {
    var self = this;
    var drawCount = 0;
    this.paintScene = function(ctx, runner) {
        self.drawCount = 0;
        var root = runner.root;
        var viewport = new Bounds(0,0,runner.canvas.width,runner.canvas.height);
        self.drawNode(ctx, runner, viewport, root);
        ADB.debugLine("draw count = " + self.drawCount);
        //console.log("---");
    };
    
    this.drawNode = function(ctx, runner, viewport, node) {
        //fast fails
        if(node == null) return;
        if(!node.isVisible()) return;
        //if no bounds then just draw it
        if(!node.getVisualBounds) {
            node.draw(ctx);
            node.clearDirty();
            self.drawCount++;
            return;
        }
        
        var bounds = node.getVisualBounds();
        if(bounds == null) return;
        if(false) {
            ctx.strokeStyle = "red";
            ctx.fillStyle = "red";
            ctx.strokeRect(bounds.x,bounds.y,bounds.w,bounds.h);
            ctx.fillText(""+bounds.toString() + "    " + viewport.toString(),bounds.x+500,bounds.y);
            p("bounds = " + bounds.toString() + " " + viewport.toString() + " " + viewport.intersects(bounds));
        }
        //if intersect then draw
        if(viewport.intersects(bounds)) {
            if(node instanceof Group) {
                ctx.save();
                ctx.translate(node.x,node.y);
                var v2 = viewport.translate(-node.x,-node.y);
                indent();
                for(var i=0; i<node.children.length;i++) {
                    self.drawNode(ctx,runner,v2,node.children[i]);
                }
                outdent();
                ctx.restore();
                node.clearDirty();
                self.drawCount++;
            } else {
                node.draw(ctx);
                self.drawCount++;
            }
        }
        //make the node clean
        node.clearDirty();
    }
}

/*
 advanced painting routine which handles recursion itself and caches nodes into canvas buffers.
*/
function CachingPaintStrategy() {
    var self = this;
    self.drawCount = 0;
    
    this.paintScene = function(ctx,root) {
        if(root == null) return;
        self.drawCount = 0;
        if(root instanceof Group) {
            self.drawGroup(ctx,root);
        } else {
            self.drawShape(ctx,root);
        }
        //console.log("caching paint node count = " + self.drawCount);
    };
    
    this.drawShape = function(ctx,node) {
        if(!node || !node.isVisible()) return;
        
        if(node.isCached()) {
            //draw using a buffer cache
            var bounds = node.getVisualBounds();
            var bufdirty = !node._amino_buf;
            if(bufdirty) {
                node._amino_buf = new Buffer(bounds.getWidth(),bounds.getHeight());
            }
            //redraw the child only if it's dirty
            if(node.isDirty() || bufdirty) {
                var ctx2 = node._amino_buf.getContext();
                ctx2.save();
                ctx2.translate(-bounds.getX(),-bounds.getY());
                self.drawCount++;
                node.draw(ctx2);
                ctx2.restore();
            }
            ctx.save();
            ctx.translate(bounds.getX(),bounds.getY());
            ctx.drawImage(node._amino_buf.buffer,0,0);
            ctx.restore();
            node.clearDirty();
        } else {
            //regular draw
            self.drawCount++;
            node.draw(ctx);
        }
    };
    
    this.drawGroup = function(ctx,group) {
        if(!group || !group.isVisible()) return;
        
        indent();
        ctx.save();
        var ga = ctx.globalAlpha;
        ctx.globalAlpha = group.opacity;
        ctx.translate(group.x,group.y);
        for(var i=0; i<group.children.length;i++) {
            self.drawShape(ctx,group.children[i]);
        }
        ctx.restore();
        outdent();
        group.clearDirty();
    };
}



/* 
@class MEvent The base mouse event. Has a reference to the node that the event was on (if any), and the x and y coords. Will have more functionality in the future. 
@category misc
*/
function MEvent() {
    this.node = null;
    this.x = -1;
    this.y = -1;
    var self = this;
    //@method Get the node that this mouse event actually happened on.
    this.getNode = function() {
        return this.node;
    };
    this.getX = function() {
        return this.x;
    };
    this.getY = function() {
        return this.y;
    };
    return true;
}

function KEvent() {
    this.key = 0;
}




/* 
    adapted from robert penner's easing equations.
    http://www.robertpenner.com/easing/
*/
var LINEAR = function(t) {
    return t;
}
var EASE_IN = function(t) {
    var t2 = t*t;
    return t2;
};
var EASE_OUT = function(t) {
    t = 1-t;
    var t2 = t*t;
    return -t2+1;
};
var EASE_IN_OVER = function(t) {
    var s = 1.70158;
    var t2 = t*t*((s+1)*t-s);
    return t2;
};
var EASE_OUT_OVER = function(t) {
    var s = 1.70158;
    t = 1-t;
    var t2 = t*t*((s+1)*t-s);
    return -t2+1;
};
/*
//x(t) = x0 cos(sqrt(k/m)*t)
//k = spring constant (stiffness)
//m = mass 
var SPRING = function(t) {
    var k = 0.01;
    var m = 1;
    return Math.cos(t*Math.PI*2);//Math.sqrt(k/m)*t);
};
*/

/* 
@class PropAnim A PropAnim is a single property animation. It animates a property on an object over time, optionally looping and reversing direction at the end of each loop (making it oscillate).
@category animation
*/
function PropAnim(n,prop,start,end,duration) {
    this.node = n;
    this.prop = prop;
    this.started = false;
    this.startValue = start;
    this.endValue = end;
    this.duration = duration;
    this.loop = false;
    this.autoReverse = false;
    this.forward = true;
    this.dead = false;
    
    
    var self = this;
    
    //@doc Indicates if this animation has started yet
    this.isStarted = function() {
        return self.started;
    };
    
    //@property loop  Determines if the animation will loop at the end rather than stopping.
    this.setLoop = function(loop) {
        this.loop = loop;
        return this;
    };
    
    //@property autoReverse Determines if the animation will reverse direction when it loops. This means it will oscillate.
    this.setAutoReverse = function(r) {
        this.autoReverse = r;
        return this;
    };
    
    this.start = function(time) {
        self.startTime = time;
        self.started = true;
        self.node[self.prop] = self.startValue;
    };
    
    this.setValue = function(tvalue) {
        value = (self.endValue-self.startValue)*tvalue + self.startValue;
        self.node[self.prop] = value;
        self.node.setDirty();
    };
    
    this.update = function(time) {
        var elapsed = time-self.startTime;
        var fract = 0.0;
        fract = elapsed/(duration*1000);
        if(fract > 1.0) {
            if(self.loop) {
                self.startTime = time;
                if(self.autoReverse) {
                    self.forward = !self.forward;
                }
                fract = 0.0;
            } else {
                //set the final value
                self.setValue(1.0);
                self.dead = true;
                return;
            }
        }
        
        if(!self.forward) {
            fract = 1.0-fract;
        }
        //var value = (self.endValue-self.startValue)*fract + self.startValue;
        //var value = self.tween(fract,self.startValue,self.endValue);
        var tvalue = self.tween(fract);
        self.setValue(tvalue);
    }
    this.tween = LINEAR;
    this.setTween = function(func) {
        this.tween = func;
        return this;
    };
    return true;
}    

/*
@class PathAnim  Animates a shape along a path. The Path can be composed of lines or curves. PathAnim can optionally loop or reverse direction at the end. Create it with the node, path, and duration like this: `new PathAnim(node,path,10);`
@category animation
*/
function PathAnim(n,path,duration) {
    this.node = n;
    this.path = path;
    this.duration = duration;
    this.loop = false;
    this.forward = true;
    this.dead = false;
    var self = this;
    //@method Returns true if the animation is currently running.
    this.isStarted = function() {
        return self.started;
    };
    //@property loop Indicates if the animation should repeat when it reaches the end.
    this.setLoop = function(loop) {
        this.loop = loop;
        return this;
    };
    this.tween = LINEAR;
    this.setTween = function(func) {
        this.tween = func;
        return this;
    };
    this.start = function(time) {
        self.started = true;
        self.startTime = time;
        //        self.node[self.prop] = self.startValue;
        return self;
    };
    this.update = function(time) {
        var elapsed = time-self.startTime;
        var fract = 0.0;
        fract = elapsed/(duration*1000);
        if(fract > 1.0) {
            if(self.loop) {
                self.startTime = time;
                if(self.autoReverse) {
                    self.forward = !self.forward;
                }
                fract = 0.0;
            } else {
                self.dead = true;
                return;
            }
        }

        if(!self.forward) {
            fract = 1.0-fract;
        }

        var tvalue = self.tween(fract);
        //value = (self.endValue-self.startValue)*tvalue + self.startValue;
        var pt = self.path.pointAtT(tvalue);
        if(self.node.setX){
            //console.log("setting x");
            self.node.setX(pt[0]);
            self.node.setY(pt[1]);
        }
        if(self.node.setTranslateX) {
            self.node.setTranslateX(pt[0]);
            self.node.setTranslateY(pt[1]);
        }
        self.node.setDirty();
    }
    return true;
}



var ADB = {
    dline:"",
    avgfps:0,
    getAverageFPS : function() {
        return ADB.avgfps;
    },
    debugLine : function(s) {
        ADB.dline = s;
    }
}


/* 
@class Runner The core of Amino. It redraws the screen, processes events, and executes animation. Create a new instance of it for your canvas, then call `start()` to start the event loop.
@category misc
*/
function Runner() {
    this.root = "";
    this.background = "gray";
    this.anims = [];
    this.callbacks = [];
    this.listeners = {};
    this.masterListeners = [];
    this.tickIndex = 0;
    this.tickSum = 0;
    this.tickSamples = 30;
    this.tickList = [];
    this.lastTick = 0;
    this.fps = 60;
    this.dirtyTrackingEnabled = true;
    this.clearBackground = true;
    this.DEBUG = true;
    
    //this.paintStrategy = new SimplePaintStrategy();
    //this.paintStrategy = new CachingPaintStrategy();
    this.paintStrategy = new BoundsPaintStrategy();
    
    var self = this;

    //@property root  The root node of the scene.
    this.setRoot = function(r) {
        self.root = r;
        if(self.root) {
            self.root.setDirty();
        }
        return self;
    };
    //@property background The background color of the scene.  May be set to null or "" to not draw a background.
    this.setBackground = function(background) {
        self.background = background;
        return self;
    };
    
    //@property fps  The target FPS (Frames Per Second). The system will try to hit this FPS and never go over it.
    this.setFPS = function(fps) {
        self.fps = fps;
        return self;
    };
    
    function attachEvent(node,name,func) {
        self.masterListeners.push(func);
        if(node.addEventListener) {
            node.addEventListener(name,func,false);
        } else if(node.attachEvent) {
            node.attachEvent(name,func);
        }
    };
    
    this.calcLocalXY = function(canvas,event) {
        var docX = -1;
        var docY = -1;
        if (event.pageX == null) {
            // IE case
            var d= (document.documentElement && document.documentElement.scrollLeft != null) ?
                 document.documentElement : document.body;
             docX= event.clientX + d.scrollLeft;
             docY= event.clientY + d.scrollTop;
        } else {
            // all other browsers
            docX= event.pageX;
            docY= event.pageY;
        }        
        docX -= canvas.offsetLeft;
        docY -= canvas.offsetTop;
        return [docX,docY];
    };
    //@property canvas  The canvas element that the scene will be placed in.
    this.setCanvas = function(canvas) {
        self.canvas = canvas;
        var _mouse_pressed = false;
        var _drag_target = null;
        attachEvent(canvas,'mousedown',function(e){
            var xy = self.calcLocalXY(canvas,e);
            _mouse_pressed = true;
            //send target node event first
            var node = self.findNode(self.root,xy[0],xy[1]);
            //p("---------- found node --------");
            //console.log(node);
            var evt = new MEvent();
            evt.node = node;
            evt.x = xy[0];
            evt.y = xy[1];
            if(node) {
                var start = node;
                _drag_target = node;
                while(start) {
                    self.fireEvent("MOUSE_PRESS",start,evt);
                    //p("blocked = " + start.isMouseBlocked());
                    if(start.isMouseBlocked()) return;
                    start = start.getParent();
                }
            }
            //send general events next
            self.fireEvent("MOUSE_PRESS",null,evt);
            //p("---------------");
        });
        attachEvent(canvas,'mousemove',function(e){
            if(_mouse_pressed) {
                var xy = self.calcLocalXY(canvas,e);
                var node = self.findNode(self.root,xy[0],xy[1]);
                var evt = new MEvent();
                
                //redirect events to current drag target, if applicable
                if(_drag_target) {
                    node = _drag_target;
                }
                evt.node = node;
                evt.x = xy[0];
                evt.y = xy[1];
                if(node) {
                    var start = node;
                    while(start) {
                        self.fireEvent("MOUSE_DRAG",start,evt);
                        if(start.isMouseBlocked()) return;
                        start = start.getParent();
                    }
                }
                //send general events next
                self.fireEvent("MOUSE_DRAG",null,evt);
            }
        });
        attachEvent(canvas, 'mouseup', function(e) {
            _mouse_pressed = false;
            _drag_target = false;
            //send target node event first
            var xy = self.calcLocalXY(canvas,e);
            var node = self.findNode(self.root,xy[0],xy[1]);
            //console.log(node);
            var evt = new MEvent();
            evt.node = node;
            evt.x = xy[0];
            evt.y = xy[1];
            if(node) {
                var start = node;
                while(start) {
                    self.fireEvent("MOUSE_RELEASE",start,evt);
                    if(start.isMouseBlocked()) return;
                    start = start.getParent();
                }
            }
            //send general events next
            self.fireEvent("MOUSE_RELEASE",null,evt);
        });
        attachEvent(document, 'keydown', function(e) {
            var evt = new KEvent();
            evt.key = e.keyCode;
            self.fireEvent("KEY_PRESSED",null,evt);
        });
        attachEvent(document, 'keyup', function(e) {
            var evt = new KEvent();
            evt.key = e.keyCode;
            self.fireEvent("KEY_RELEASED",null,evt);
        });
        attachEvent(window, 'onUnload', function(e) {
            console.log("got Unload");
        });
        attachEvent(window, 'onunload', function(e) {
            console.log("got unload");
        });
        return self;
    };
    
    this.findNode = function(node,x,y) {
        //p("findNode:" + node._hash + " " + x + " " + y);
        //console.log(node);
        //don't descend into invisible nodes
        //p("visible = " + node.isVisible());
        if(!node.isVisible()) {
            return null;
        }
        if(node.contains(x,y)) {
            //p("node contains it " + node._hash);
            return node;
        }
        if(node.hasChildren()) {
            //p("node has children");
            var nc = node.convertToChildCoords(x,y);
            indent();
            //descend from front to back
            for(var i=node.childCount()-1; i>=0; i--) {
                //p("looking at child: " + node.getChild(i)._hash);
                //console.log(node.getChild(i));
                var n = self.findNode(node.getChild(i),nc[0],nc[1]);
                if(n) {
                    //p("backing up. matched " + n._hash);
                    outdent();
                    return n;
                }
            }
            outdent();
        }
        //p("returning null");
        return null;
    };
    
    this.fireEvent = function(type,key,e) {
        //p("firing event for key: " + key + " type = " + type);
        //console.log(key);
        var k = key;
        if(key) {
            k = key._hash;
        } else {
            k = "*";
        }
        //p("Using real key: " + k);
        //p("firing event for key: " + k + " type = " + type);
        
        if(self.listeners[k]) {
            if(self.listeners[k][type]) {
                for(var i=0; i<self.listeners[k][type].length; i++) {
                    var l = self.listeners[k][type][i];
                    //p("listener = " + l);
                    l(e);
                }
            }
        }
    };

    this.drawScene = function(ctx) {
        //fill the background
        if(self.clearBackground) {
            if(self.transparentBackground) {
                ctx.clearRect(0,0,self.canvas.width,self.canvas.height);
            } else {
                ctx.fillStyle = self.background;
                ctx.fillRect(0,0,self.canvas.width,self.canvas.height);
            }
        }
        
        //draw the scene
        if(self.root) {
            self.paintStrategy.paintScene(ctx,self);
        }
    }        
    
    
    
    this.doNext = function() {
        if(window.webkitRequestAnimationFrame) {
            window.webkitRequestAnimationFrame(self.update);
            return;
        }
        if(window.mozRequestAnimationFrame) {
            window.mozRequestAnimationFrame(self.update);
            return;
        }
        console.log("can't repaint! not webkit");
        setTimeout(self.update,1000/self.fps);
        return;
    };
    
    this.update = function() {
        
        var time = new Date().getTime();
        self.processInput(time);
        self.processAnims(time);
        self.processCallbacks(time);
        var ctx = self.canvas.getContext("2d");
        
        self.processRepaint(time, ctx);
        self.processDebugOverlay(time, ctx);
        //drop to 4fps if in bg
        if(self.inBackground) {
            setTimeout(function() { self.doNext(); }, 1000);
        } else {
            self.doNext();
        }
    };
    
    this.interv = -1;
    this.inBackground = false;
    //@doc Start the scene. This is usually the last thing you call in your setup code.
    this.start = function() {
        //palm specific
        if (window.PalmSystem) {
            window.PalmSystem.stageReady();
            Mojo = {
                stageActivated: function() {
                    self.inBackground = false;
                },
                stageDeactivated: function() {
                    self.inBackground = true;
                },
            };
        }
        
        self.lastTick = new Date().getTime();
        self.doNext();
        //self.interv = setInterval(this.update,1000/self.fps);
    };
    
    
    this.stop = function() {
        clearInterval(self.interv);
        console.log("stopped the repaint");
    };
    this.cleanup = function() {
        console.log("cleaning up stuff");
        self.anims = [];
        self.callbacks = [];
        
        for(var i=0; i<self.listeners.length; i++) {
            self.listeners[i] = [];
        }
        self.listeners = [];
        self.root = null;
        for(var i=0; i<self.masterListeners.length; i++) {
            console.log("removing master listener");
            self.canvas.removeEventListener(self.masterListeners[i]);
        }
        self.canvas = null;
    };
    
    //@doc Add an animation to the scene.
    this.addAnim = function(anim) {
        this.anims[this.anims.length] = anim;
        return this;
    };
    
    //@doc Add a function callback to the scene. The function will be called on every frame redraw.
    this.addCallback = function(callback) {
        this.callbacks[this.callbacks.length] = callback;
        return this;
    };
    
    //@doc Listen to a particular type of event on a particular target. *eventTarget* may be null or "*" to listen on all nodes.
    this.listen = function(eventType, eventTarget, callback) {
        var key = "";
        if(eventTarget) {
            key = eventTarget._hash;
        } else {
            key = "*";
        }
        
        if(!this.listeners[key]) {
            this.listeners[key] = [];
        }
        if(!this.listeners[key][eventType]) {
            this.listeners[key][eventType] = [];
        }
        
        this.listeners[key][eventType].push(callback);
        //p("added listener. key = "+ key + " type = " + eventType + " = " + callback);
    };
    
    //@doc Do the function later, when the next frame is drawn.
    this.doLater = function(callback) {
        callback.doLater = true;
        callback.done = false;
        self.addCallback(callback);
        return self;
    };
    
    //@method gets the drawing context of the canvas. You *must* have already called _setCanvas()_ first.
    this.getContext = function() {
        return self.canvas.getContext('2d');
    };
    
    
    this.processInput = function() {
    };
    this.processAnims = function(time) {
        //process animation
        for(i=0;i<self.anims.length; i++) {
            var a = self.anims[i];
            if(a.dead) continue;
            if(!a.isStarted()) {
                a.start(time);
                continue;
            }
            a.update(time);
        }
    };
    
    this.processCallbacks = function(time) {
        //process callbacks
        for(i=0;i<self.callbacks.length;i++) {
            var cb = self.callbacks[i];
            if(!cb.done) {
                cb();
            }
            if(cb.doLater) {
                cb.doLater = false;
                cb.done = true;
            }
        }
    };
    
    this.processRepaint = function(time, ctx) {
        if(self.dirtyTrackingEnabled) {
            if(self.root && self.root.isDirty()) {
                self.drawScene(ctx);
            }
        } else {
            self.drawScene(ctx);
        }
    };
    
    this.processDebugOverlay = function(time, ctx) {
        if(self.DEBUG) {
            ctx.save();
            ctx.translate(0,self.canvas.height-60);
            ctx.fillStyle = "gray";
            ctx.fillRect(0,-10,200,70);
            //draw a debugging overlay
            ctx.fillStyle = "black";
            ctx.fillText("timestamp " + new Date().getTime(),10,0);
            
            //calc fps
            var delta = time-self.lastTick;
            self.lastTick = time;
            if(self.tickList.length <= self.tickIndex) {
                self.tickList[self.tickList.length] = 0;
            }
            self.tickSum -= self.tickList[self.tickIndex];
            self.tickSum += delta;
            self.tickList[self.tickIndex]=delta;
            ++self.tickIndex;
            if(self.tickIndex>=self.tickSamples) {
                self.tickIndex = 0;
            }
            var fpsAverage = self.tickSum/self.tickSamples;
            ctx.fillText("last msec/frame " + delta,10,10);
            ctx.fillText("last frame msec " + (new Date().getTime()-time),10,20);
            ctx.fillText("avg msec/frame  " + (fpsAverage).toPrecision(3),10,30);
            ctx.fillText("avg fps = " + ((1.0/fpsAverage)*1000).toPrecision(3),10,40);
            ctx.fillText("" + ADB.dline,10,50);
            ctx.restore();
            ADB.avgfps = ((1.0/fpsAverage)*1000);
        }
        
    };

    
    return true;
}


/*
@class Util  A class with some static utility functions.
@category misc
*/
function Util() {
    //@doc convert the canvas into a PNG encoded data url. Use this if your browser doesn't natively support data URLs
    this.toDataURL = function(canvas) {
	    console.log(" $$ Canvas = " + canvas);
	    var canWidth = canvas.width;
	    var canHeight = canvas.height;
        var c = canvas.getContext('2d');
        var data = c.getImageData(0,0,canWidth, canHeight);
        var p = new PNGlib(canWidth, canHeight, 2); 
        for (var j = 0; j < canHeight; j++) {
            for (var i = 0; i < canWidth; i++) {
                var n = i+j*canWidth;
                var pi = n*4; //pixel index 
                var r = data.data[pi+0];
                var g = data.data[pi+1];
                var b = data.data[pi+2];
                var a = data.data[pi+3];
                p.buffer[p.index(i,j)] = p.getColor(r,g,b,a);
            }
        }
        
        var url = "data:image/png;base64,"+p.getBase64();
        return url;
    };
    
    return true;
};




