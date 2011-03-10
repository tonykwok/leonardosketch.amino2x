
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




/* The root node class. All nodes have a parent and could
potentially have children */

__node_hash_counter = 0;
function Node() {
    this.parent = null;
    __node_hash_counter++;
    this._hash = __node_hash_counter;
    var self = this;
    this.setParent = function(parent) { this.parent = parent; return this; };
    this.getParent = function() { return this.parent; };
    this.visible = true;
    this.dirty = true;
    this.blocksMouse = false;
    this.setVisible = function(visible) {
        self.visible = visible;
        self.setDirty();
        return self;
    };
    this.isVisible = function() {
        return this.visible;
    };
    this.isMouseBlocked = function() {
        return this.blocksMouse;
    };
    this.setMouseBlocked = function(m) {
        this.blocksMouse = m;
        return this;
    };
    this.setDirty = function() {
        this.dirty = true;
        if(this.getParent()) {
            this.getParent().setDirty();
        }
    };
    this.isDirty = function() {
        return self.dirty;
    };
    this.clearDirty = function() {
        self.dirty = false;
    };
    return true;
}




/* transforms the children inside of it */

function Transform(n) {
    Node.call(this);
    this.node = n;
    this.node.setParent(this);
    this.rotation = 0;
    this.translateX = 0;
    this.translateY = 0;
    this.setTranslateX = function(tx) {
        this.translateX = tx;
        return this;
    };
    this.setRotate = function(rotation) {
        this.rotation = rotation;
        return this;
    };
    this.setTranslateY = function(ty) {
        this.translateY = ty;
        return this;
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
        var a = -this.rotation * Math.PI/180;
        var x2 = x1*Math.cos(a) - y1*Math.sin(a);
        var y2 = x1*Math.sin(a) + y1*Math.cos(a);
        return [x2,y2];
    };
    this.draw = function(ctx) {
        ctx.save();
        ctx.translate(this.translateX,this.translateY);
        ctx.rotate(this.rotation*Math.PI/180.0,0,0);
        this.node.draw(ctx);
        ctx.restore();
        this.clearDirty();
    };
    return true;
}
Transform.extend(Node);





/* A Group holds a set of child nodes. It does not draw anything
by itself, but setting visible to false will hide the children.
*/

function Group() {
    Node.call(this);
    this.children = [];
    this.parent = null;
    var self = this;
    this.add = function(n) {
        self.children[self.children.length] = n;
        n.setParent(self);
        self.setDirty();
        return self;
    };
    this.remove = function(n) {
        var i = self.children.indexOf(n);
        if(i >= 0) {
            self.children.splice(i,1);
            n.setParent(null);
        }
        self.setDirty();
        return self;
    };
    this.draw = function(ctx) {
        if(!self.isVisible()) return;
        indent();
        for(var i=0; i<self.children.length;i++) {
            self.children[i].draw(ctx);
        }
        outdent();
        this.clearDirty();
    };
    this.clear = function() {
        self.children = [];
        self.setDirty();
        return self;
    };
    this.contains = function(x,y) {
        return false;
    };
    this.hasChildren = function() {
        return true;
    };
    this.convertToChildCoords = function(x,y) {
        return [x,y];
    };
    this.childCount = function() {
        return self.children.length;
    };
    this.getChild = function(n) {
        return self.children[n];
    };
    return true;
};
Group.extend(Node, {});




/* The base of all shapes. Shapes are geometric
shapes which have a fill, a stroke, and opacity. They
may be filled or unfilled.  */
function Shape() {
    Node.call(this);
    this.hasChildren = function() { return false; }
    
    this.fill = "red";
    this.setFill = function(fill) {
        this.fill = fill;
        this.setDirty();
        return this;
    };
    this.getFill = function() {
        return this.fill;
    };
    this.strokeWidth = 0;
    this.setStrokeWidth = function(sw) {
        this.strokeWidth = sw;
        this.setDirty();
        return this;
    };
    this.getStrokeWidth = function() {
        return this.strokeWidth;
    };
    this.contains = function() { return false; }
    return true;
}
Shape.extend(Node);





/* A text shape. draws text on the screen with the
desired content, font, and color.
*/
function Text() {
    Shape.call(this);
    this.x = 0;
    this.y = 0;
    this.text = "-no text-";
    this.parent = null;
    
    this.draw = function(ctx) {
        var f = ctx.font;
        ctx.font = this.font;
        ctx.fillStyle = this.fill;
        ctx.fillText(this.text,this.x,this.y);
        ctx.font = f;
        this.clearDirty();
    };
    this.setText = function(text) {
        this.text = text;
        this.setDirty();
        return this;
    };
    this.setX = function(x) {
        this.x = x;
        this.setDirty();
        return this;
    };
    this.setY = function(y) {
        this.y = y;
        this.setDirty();
        return this;
    };
    
    this.font = "20pt Verdana";
    this.setFont = function(font) {
        this.font = font;
        this.setDirty();
        return this;
    }
    
    this.contains = function() { return false; }
    return true;    
};
Text.extend(Shape);





/* A circle shape */
function Circle() {
    Shape.call(this);
    this.x = 0.0;
    this.y = 0.0;
    this.radius = 10.0;
    this.fill = "black";
    var self = this;
    this.set = function(x,y,radius) {
        self.x = x;
        self.y = y;
        self.radius = radius;
        self.setDirty();
        return self;
    };
    this.getX = function() { return this.x; };
    this.getY = function() { return this.y; };
    this.getRadius = function() { return this.radius; };
    this.setX = function(x) { this.x = x; this.setDirty(); return this; };
    this.setY = function(y) { this.y = y; this.setDirty(); return this; };
    this.setFill = function(fill) {
        self.fill = fill;
        self.setDirty();
        return self;
    };
    this.draw = function(ctx) {
        ctx.fillStyle = self.fill;
        ctx.beginPath();
        ctx.arc(self.x, self.y, self.radius, 0, Math.PI*2, true); 
        ctx.closePath();
        ctx.fill();
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
    return true;
};
Circle.extend(Shape);


function ImageView(url) {
    Node.call(this);
    this.src = url;
    this.img = new Image();
    this.loaded = false;
    this.x = 0.0;
    this.y = 0.0;
    this.width = 10;
    this.height = 10;
    this.setX = function(x) {
        this.x = x;
        this.setDirty();
        return this;
    };
    this.getX = function() { return this.x; };
    this.setY = function(y) {
        this.y = y;
        this.setDirty();
        return this;
    };
    this.getY = function() { return this.y; };
    
    var self = this;
    this.img.onload = function() {
        self.loaded = true;
        self.setDirty();
        self.width = self.img.width;
        self.height = self.img.height;
    }
    this.img.src = url;
    
    this.hasChildren = function() { return false; }
    
    this.draw = function(ctx) {
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
    return true;
};
ImageView.extend(Node);

/* A rectangle shape. May be rounded or have straight corners */
function Rect() {
    Shape.call(this);
    this.x = 0.0;
    this.y = 0.0;
    this.width = 100.0;
    this.height = 100.0;
    this.set = function(x,y,w,h) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        this.setDirty();
        return this;
    };
    this.getWidth = function() { return this.width; };
    this.setWidth = function(w) {
        this.width = w;
        this.setDirty();
        return this;
    };
    this.getHeight = function() { return this.height; };
    this.setHeight = function(h) {
        this.height = h;
        this.setDirty();
        return this;
    };
    this.setX = function(x) {
        this.x = x;
        this.setDirty();
        return this;
    };
    this.getX = function() { return this.x; };
    this.setY = function(y) {
        this.y = y;
        this.setDirty();
        return this;
    };
    this.getY = function() { return this.y; };
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
                //console.log("returning true");
                return true;
            }
        }
        //console.log("returning false");
        return false;
    };
    this.draw = function(ctx) {
        ctx.fillStyle = this.fill;
        if(this.corner > 0) {
            var x = this.x;
            var y = this.y;
            var width = this.width;
            var height = this.height;
            var radius = this.corner;
            ctx.beginPath();
            ctx.moveTo(x + radius, y);
            ctx.lineTo(x + width - radius, y);
            ctx.quadraticCurveTo(x + width, y, x + width, y + radius);
            ctx.lineTo(x + width, y + height - radius);
            ctx.quadraticCurveTo(x + width, y + height, x + width - radius, y + height);
            ctx.lineTo(x + radius, y + height);
            ctx.quadraticCurveTo(x, y + height, x, y + height - radius);
            ctx.lineTo(x, y + radius);
            ctx.quadraticCurveTo(x, y, x + radius, y);
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
    return true;
};
Rect.extend(Shape);






/* The base mouse event. Has a reference to the node that
the event was on (if any), and the x and y coords. Will have
more functionality in the future. */
function MEvent() {
    this.node = null;
    this.x = -1;
    this.y = -1;
    var self = this;
    this.getNode = function() {
        return this.node;
    }
}






/* An Anim is a property animation. It animates a property
on an object over time, optionally looping and reversing direction
at the end of each loop (making it oscillate).
*/
function Anim(n,prop,start,end,duration) {
    this.node = n;
    this.prop = prop;
    this.started = false;
    this.startValue = start;
    this.endValue = end;
    this.duration = duration;
    this.loop = false;
    this.autoReverse = false;
    this.forward = true;
    
    var self = this;
    
    this.isStarted = function() {
        return self.started;
    };
    
    this.setLoop = function(loop) {
        this.loop = loop;
        return this;
    };
    
    this.setAutoReverse = function(r) {
        this.autoReverse = r;
        return this;
    };
    
    this.start = function(time) {
        self.startTime = time;
        self.started = true;
        self.node[self.prop] = self.startValue;
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
                return;
            }
        }
        
        if(!self.forward) {
            fract = 1.0-fract;
        }
        var value = (self.endValue-self.startValue)*fract + self.startValue;
        self.node[self.prop] = value;
        self.node.setDirty();
        
    }
    return true;
}    








/* The core of Amino. It redraws the screen, processes events, and
processes animation. */

function Runner() {
    this.root = "";
    this.background = "gray";
    this.anims = [];
    this.callbacks = [];
    this.listeners = {};
    this.tickIndex = 0;
    this.tickSum = 0;
    this.tickSamples = 30;
    this.tickList = [];
    this.lastTick = 0;
    this.fps = 60;
    this.dirtyTrackingEnabled = true;
    this.clearBackground = true;
    
    var self = this;
    
    this.setBackground = function(background) {
        self.background = background;
        return self;
    };
    
    this.setFPS = function(fps) {
        self.fps = fps;
        return self;
    };
    
    this.setCanvas = function(canvas) {
        self.canvas = canvas;
        var _mouse_pressed = false;
        var _drag_target = null;
        canvas.addEventListener('mousedown',function(e){
            _mouse_pressed = true;
            //send target node event first
            var node = self.findNode(self.root,e.offsetX,e.offsetY);
            //p("---------- found node --------");
            //console.log(node);
            var evt = new MEvent();
            evt.node = node;
            evt.x = e.offsetX;
            evt.y = e.offsetY;
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
        },false);
        canvas.addEventListener('mousemove', function(e) {
            if(_mouse_pressed) {
                var node = self.findNode(self.root,e.offsetX,e.offsetY);
                var evt = new MEvent();
                
                //redirect events to current drag target, if applicable
                if(_drag_target) {
                    node = _drag_target;
                }
                evt.node = node;
                evt.x = e.offsetX;
                evt.y = e.offsetY;
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
        canvas.addEventListener('mouseup',function(e){
            _mouse_pressed = false;
            _drag_target = false;
            //send target node event first
            var node = self.findNode(self.root,e.offsetX,e.offsetY);
            //console.log(node);
            var evt = new MEvent();
            evt.node = node;
            evt.x = e.offsetX;
            evt.y = e.offsetY;
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
        },false);
        return self;
    };
    
    this.findNode = function(node,x,y) {
        //p("findNode:" + node._hash + " " + x + " " + y);
        //console.log(node);
        //don't descend into invisible nodes
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
                //p("looking at child: " + node.getChild(i));
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
            ctx.fillStyle = self.background;
            ctx.fillRect(0,0,self.canvas.width,self.canvas.height);
        }
        
        //draw the scene
        self.root.draw(ctx);
    }        
    
    this.update = function() {
        var time = new Date().getTime();
        //process animation
        for(i=0;i<self.anims.length; i++) {
            var a = self.anims[i];
            if(!a.isStarted()) {
                a.start(time);
                continue;
            }
            a.update(time);
        }
        //process callbacks
        for(i=0;i<self.callbacks.length;i++) {
            self.callbacks[i]();
        }
        
        var ctx = self.canvas.getContext("2d");
        
        if(self.dirtyTrackingEnabled) {
            if(self.root.isDirty()) {
                //console.log("drawing");
                self.drawScene(ctx);
            }
        } else {
            self.drawScene(ctx);
        }
        
        ctx.save();
        ctx.translate(0,self.canvas.height-50);
        ctx.fillStyle = "gray";
        ctx.fillRect(0,-10,200,60);
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
        ctx.restore();
    };
    
    this.start = function() {
        //palm specific
        if (window.PalmSystem) {
            window.PalmSystem.stageReady();
        }        
        
        self.lastTick = new Date().getTime();
        setInterval(this.update,1000/self.fps);
    };
    
    this.addAnim = function(anim) {
        this.anims[this.anims.length] = anim;
        return this;
    };
    
    this.addCallback = function(callback) {
        this.callbacks[this.callbacks.length] = callback;
        return this;
    };
    
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
    
    return true;
}





