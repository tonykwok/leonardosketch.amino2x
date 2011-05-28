
function LineChart() {
    Node.call(this);
    this.data = {};
    this.colors = ["blue","gray","red","green","cyan"];
    
    this.maxDataSetSize = 0;
    this.minValue = 1000000;
    this.maxValue = -1000000;
    this.vlabel = "v";
    this.setVLabel = function(v) {
        this.vlabel = v;
    };
    this.hlabel = "h";
    this.setHLabel = function(h) {
        this.hlabel = h;
    };
    this.addDataSet = function(name,values) {
        this.data[name]=values;
        this.maxDataSetSize = Math.max(values.length,this.maxDataSetSize);
        for(var i=0; i<values.length; i++) {
            this.minValue = Math.min(values[i],this.minValue);
            this.maxValue = Math.max(values[i],this.maxValue);
        }
    };
    
    this.autoscale = true;
    this.setAutoScale = function(as) {
        this.autoScale = as;
        this.setDirty();
        return this;
    };
    
    this.width = 100;
    this.setWidth = function(w) {
        this.width = w;
        this.setDirty();
    };
    this.height = 100;
    this.setHeight = function(h) {
        this.height = h;
        this.setDirty();
    };
    this.indent = 50.5;
    
    this.draw = function(ctx) {
        ctx.fillStyle = "white";
        ctx.fillRect(0,0,this.width,this.height);
        
        //draw axes
        this.drawHAxis(ctx);
        this.drawVAxis(ctx);
        
        //draw legend background
        
        this.drawLegend(ctx);
        
        //draw data sets and their legends
        var color = 0;
        var count = 0;
        for(var name in this.data) {
            var d = this.data[name];
            this.drawGraph(ctx,name,d,count,this.colors[color],5);
            color = (color+1) % this.colors.length;
            count++;
        }
    };
    
    this.drawLegend = function(ctx) {
        ctx.strokeStyle = "gray";
        var w = 100;
        var h = 100;
        ctx.strokeRect(this.width-this.indent-w,0.5,w,h);
        ctx.fillStyle = "black";
        ctx.fillText(this.vlabel + " vs " + this.hlabel, 100,20);
    };
    
    this.drawHAxis = function(ctx) {
        ctx.strokeStyle = "black";
        ctx.fillStyle = "black";
        ctx.lineWidth = 1;
        ctx.beginPath();
        ctx.moveTo(this.indent,this.indent);
        ctx.lineTo(this.indent,this.height-this.indent);
        ctx.stroke();
        for(var i=0; i<this.maxDataSetSize; i++) {
            ctx.beginPath();
            var x = this.indent + i*this.width/this.maxDataSetSize;
            var y = this.height-this.indent;
            ctx.moveTo(x,y);
            ctx.lineTo(x,y+10);
            ctx.stroke();
            ctx.fillText(""+i,x+3,y+10);
        }
        ctx.fillText(this.hlabel,this.indent,this.height-this.indent + 20);
    };
    this.drawVAxis = function(ctx) {
        ctx.strokeStyle = "black";
        ctx.lineWidth = 1;
        ctx.beginPath();
        ctx.moveTo(this.indent,this.height-this.indent);
        ctx.lineTo(this.width-this.indent,this.height-this.indent);
        ctx.stroke();
        
        ctx.fillStyle = "black";
        for(var i=0; i<=10; i++) {
            var x = this.indent-35;
            var y = this.indent+(10-i)*(this.height-this.indent*2)/10;
            var v = i;
            if(this.autoScale) {
                v = i*(this.maxValue-this.minValue)/10;
            } else {
                v = i*(this.maxValue-0)/10;
            }
            ctx.fillText(""+v,x,y+4);
            ctx.beginPath();
            ctx.moveTo(this.indent-10,y);
            ctx.lineTo(this.indent,y);
            ctx.stroke();
        }
        ctx.fillStyle = "black";
        ctx.fillText(this.vlabel,this.indent-30,this.indent-10);
    };
    
    this.drawGraph = function(ctx,name,d,count,color,stroke) {
        ctx.strokeStyle = color;
        ctx.lineWidth = stroke;
        ctx.beginPath();
        ctx.moveTo(0,0);
        var size = this.width/this.maxDataSetSize;
        for(var i=0; i<d.length; i++) {
            var xv = i;
            var yv = d[i];
            xv = this.indent + xv*size;
            var hv = this.maxValue-this.minValue;
            if(this.autoScale) {
                yv = this.height-this.indent-(yv-this.minValue)/hv*(this.height-this.indent*2);
            } else {
                yv = this.height-this.indent-yv/this.maxValue*(this.height-this.indent*2);
            }
            if(i==0) {
                ctx.moveTo(xv,yv);
            } else {
                ctx.lineTo(xv,yv);
            }
        }
        ctx.stroke();
        ctx.fillStyle = color;
        ctx.fillText(name,this.width-100-this.indent+10,20+20*count);
    };
    
    this.contains = function() { return false; }
    this.hasChildren = function() { return false; }
    
    return true;
};
LineChart.extend(Node);

