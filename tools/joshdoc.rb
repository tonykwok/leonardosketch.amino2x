class JProp
    def setName(name)
        @name = name
    end
    def getName()
        return @name
    end
    def setDoc(doc)
        @doc = doc
    end
    def getDoc()
        return @doc
    end
end

class JClass
    def initialize
        @properties = {}
        @methods = []
        @constructor = nil
        @categories = []
    end
    def setName(name)
        @name = name
    end
    def getName()
        return @name
    end
    def setDoc(doc) 
        @doc = doc
    end
    def getDoc()
        return @doc
    end
    def getProperties()
        return @properties
    end
    def getMethods() 
        return @methods
    end
    def setConstructor(c)
        @constructor = c
    end
    def getConstructor()
        return @constructor
    end
    def addCategory(s) 
        @categories.push(s)
    end
    def getCategories
        return @categories
    end
end

class JMethod 
    def setDoc(doc)
        @doc = doc
    end
    def setDec(dec)
        @dec = dec
    end
    def getDoc()
        return @doc
    end
    def getDec()
        return @dec
    end
end

class Overview
    def initialize
        @text = []
    end
    def setTitle(title)
        @title = title;
    end
    def getTitle() 
        return @title
    end
    def append(string) 
        @text.push(string)
    end
    def getText()
        return @text
    end
end

def processMarkdown(d) 
    d = d.gsub(/</,"&lt")
    d = d.gsub(/>/,"&gt")
    d = d.gsub(/===(.+)===/,"<h3>\\1</h3>")
    d = d.gsub(/(\*(.*?)\*)/,"<b>\\2</b>")
    d = d.gsub(/(`(.*?)`)/,"<code>\\2</code>")
    return d
end

class JoshParser
    def initialize
        @inOverview = false
        @inDoc = false
        @inSample = false
        @inClass = false
        @overview = nil;
        @language = "unknown"
        @klasses = []
    end
    
    def setFilename(fn)
        @rootfile = fn
    end
    def getFilename()
        return @rootfile
    end
    
    def setOutfile(fn)
        @outfile = fn
    end
    def getOutfile()
        return @outfile
    end
    
    def parse()
        if File.directory?(@rootfile)
            puts "is a directory"
            dir = Dir.getwd
            Dir.chdir(@rootfile)
            Dir.glob("**/*") do |file|
                if file =~ /md$/
                    parseFile(file)
                end
                if file =~ /java$/
                    parseFile(file)
                end
            end
            Dir.chdir(dir)
            outputFile(@outfile)
        else 
            parseFile(@rootfile)
            outputFile(@outfile)
        end
    end
    
    def parseFile(filename)
        klass = nil;
        met = nil
        
        puts "--- #{filename}"
        File.readlines(filename).each do |line|
            m = line.match(/@(\w+)(\s(\w+))*/)
            if m
                case m.captures[0]
                when "language"
                    @language = m.captures[2]
                when "class"
                    m2 = line.match(/@class\s+(\w+)(.*)$/)
                    klass = JClass.new
                    puts "    class #{m2.captures[0]}";
                    klass.setName(m2.captures[0])
                    klass.setDoc(m2.captures[1])
                    @klasses.push(klass);
                    @inClass = true
                when "category"
                    #puts "    category is #{m.captures[2]}"
                    klass.addCategory(m.captures[2])
                when "property"
                    #puts " looking at a property line: #{m}"
                    m2 = line.match(/@property\s+(\w+)(\s+(.+))*$/)
                    name = m2.captures[0]
                    puts "        property #{name}"
                    if !@klasses.last().getProperties()[name]
                        prop = JProp.new
                        prop.setName(name)
                        if m2 
                            prop.setDoc(m2.captures[1])
                        else 
                            prop.setDoc("--")
                        end
                        @klasses.last().getProperties()[name] = prop
                    end
                when "constructor"
                    # document the thing on the next line
                    puts "        found a constructor"
                    met = JMethod.new
                    met.setDoc(line.match(/@#{m.captures[0]}(.*)/).captures[0])
                    klass.setConstructor(met)
                    @inDoc = true
                when "overview"
                    @inOverview = true
                    @overview = Overview.new
                    m2 = line.match(/@overview\s+(.+)$/)
                    @overview.setTitle(m2.captures[0])
                when "end"
                    @inOverview = false
                    @inSample = false
                    @inClass = false
                when "doc", "method"
                    # document the thing on the next line
                    met = JMethod.new
                    met.setDoc(line.match(/@#{m.captures[0]}(.*)/).captures[0])
                    puts "        method"
                    @klasses.last().getMethods().push(met)
                    @inDoc = true
                when "sample"
                    @inSample = true
                    #puts "<p>#{line.match(/@sample(.+)/).captures[0]}</p>"
                    #puts "<pre><code>"
                end
            else
                
                if @inDoc 
                    decStr = line
                    m = line.match(/\s*this\.(\S+)\s*=\s*function\((.*)\)/) 
                    if m
                        name = m.captures[0]
                        args = m.captures[1]
                        #puts "matched string #{name}"
                        #puts "args = #{args}"
                        decStr = "#{name}(#{args})"
                    end
                    #@klasses.last().getMethods().last().setDec(decStr)
                    met.setDec(decStr)
                    @inDoc = false
                end
                
                if @inSample
                    #print line
                end
                
                m = line.match(/^.*class\s+(\w+)\s*\{/)
                if m
                    #puts "started class " + m.captures[0]
                    #puts "<h2>class #{m.captures[0]}</h2"
                end
                
                if @inOverview
                    @overview.append(line)
                    if (line =~ /^\s+$/)
                        #puts "</p>"
                        #puts "<p>"
                    else
                        #puts line.sub(/\*(\w+)\*/,"<em>\\1</em>")
                    end
                end
            end
        end
    end
    
    def outputFile(outfile) 
        of = File.new(outfile,"w+");
        
        of.puts "<html>"
        of.puts "<head> <link rel='stylesheet' href='doc.css'/> </head>"
        of.puts "<body>"
        
        
        of.puts "<h1>#{@overview.getTitle()}</h1>"
        of.puts "<div class='overview'>"
        of.puts "<h2>Overview</h2>"
        lastWasCode = false;
        for l in @overview.getText()
            #puts " line = #{l}"
            #puts "last was code = #{lastWasCode}"
            
            # if indented code
            if(l.match(/^\s\s\s\s(\s*)\S(.*)$/))
                #puts "code line"
                if(!lastWasCode)
                    #puts "start code"
                    of.print "<pre><code>"
                end
                if(l.length > 10)
                    l = l.slice(4..-1)
                end 
                lastWasCode = true;
                of.puts processMarkdown(l)
                next
            end
            
            # if whitespace
            if(l.match(/^\s*$/))
                #puts "only whitespace"
                #of.puts "<p class='overview'>"
                #of.puts processMarkdown(l)
                of.puts ""
                next
            end
            
            
            if lastWasCode
                #puts "end code"
                of.puts "</code></pre>"
            end
            lastWasCode = false;
            of.puts processMarkdown(l)
        end
        if lastWasCode
            of.puts "</code></pre>"
        end
        of.puts "</div>"
        
        
        
        #of.puts "<h1>Class Overview</h1>"
        categories = {}
        for k in @klasses
            for c in k.getCategories()
                if !categories[c]
                    categories[c] = []
                end
                categories[c].push(k)
            end
        end
        
        of.puts "<div class='toc'>"
        of.puts "<h2>Classes by Category</h2>"
        of.puts "<ul class='categories'>"
        categories.each do |name,ks|
            of.puts "<li><b>#{name}</b>"
            of.puts "<ul>"
            for k in ks
                of.puts "<li><a href='##{k.getName()}'>#{k.getName()}</a></li>"
            end
            of.puts "</ul>"
            of.puts "</li>"
        end
        of.puts "</ul>"
        of.puts "</div>"
        
         # of.puts "<ul>"
        # for k in @klasses
            # of.puts "<li><a href='##{k.getName()}'>#{k.getName()}</a></li>"
        # end
        # of.puts "</ul>"
        
        for k in @klasses
            of.puts "<div class='classdef'>"
            of.puts "<h2><a name='#{k.getName}'>#{k.getName()}</a></h2>"
            
            of.puts "<p class='description'>#{processMarkdown(k.getDoc())}</p>"
            
            if (k.getConstructor())
                of.puts "<h3>Constructor</h3>"
                of.puts "<ul class='constructors'>"
                of.puts "<li><b class='name'>#{k.getConstructor().getDec()}</b>"
                of.puts "<p>#{k.getConstructor().getDoc()}</p></li>"
                of.puts "</ul>"
            end
            
            if (k.getProperties().length > 0)
                of.puts "<h3>Properties</h3>"
                of.puts "<ul class='properties'>"
                k.getProperties().each do |name,prop|
                    propdoc = ""
                    if prop.getDoc()
                        propdoc = processMarkdown(prop.getDoc())
                    end
                    of.puts "<li><b class='name'>#{name}</b> #{propdoc}</li>"
                end
                of.puts "</ul>"
            end
            
            if (k.getMethods().length > 0) 
                of.puts "<h3>Methods</h3>"
                of.puts "<ul class='methods'>"
                for m in k.getMethods()
                    of.puts "<li><b class='name'>"+m.getDec()+"</b>"
                    of.puts "<p>#{processMarkdown(m.getDoc())}</p></li>"
                end
                of.puts "</ul>"
            end
        
            of.puts "</div>"
            of.puts "\n\n\n"
        end
        
        
        of.puts "</body></html>"
    end
end

p = JoshParser.new
p.setFilename(ARGV[0])
p.setOutfile(ARGV[1]);
puts "using input file: #{p.getFilename()}"
puts "using output dir: #{p.getOutfile()}"

p.parse()


