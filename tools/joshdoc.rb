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
    d = d.gsub(/(\*(.*)\*)/,"<b>\\2</b>")
    d = d.gsub(/(`(.*)`)/,"<code>\\2</code>");
    return d
end



filename = ARGV[0];#"src/js/core/amino.js"
outfile = ARGV[1];
puts "using input file: #{filename}"
puts "using output dir: #{outfile}"

language = "unknown"
inOverview = false
inDoc = false
inSample = false
inClass = false
overview = nil;


klasses = []

File.readlines(filename).each do |line|
    m = line.match(/@(\w+)(\s(\w+))*/)
    if m
        case m.captures[0]
        when "language"
            language = m.captures[2]
        when "class"
            m2 = line.match(/@class\s+(\w+)(.*)$/)
            klass = JClass.new
            klass.setName(m2.captures[0])
            klass.setDoc(m2.captures[1])
            klasses.push(klass);
            inClass = true
        when "property"
            m2 = line.match(/@property\s+(\w+)\s+(.+)$/)
            #puts "m1 = #{m2.captures[0]}"
            name = m2.captures[0]
            if !klasses.last().getProperties()[name]
                prop = JProp.new
                prop.setName(name)
                if m2 
                    prop.setDoc(m2.captures[1])
                else 
                    prop.setDoc("--")
                end
                klasses.last().getProperties()[name] = prop
            end
        when "overview"
            inOverview = true
            overview = Overview.new
            m2 = line.match(/@overview\s+(.+)$/)
            overview.setTitle(m2.captures[0])
        when "end"
            if inOverview
                #puts "</p></div>"
            end
            if inSample
                #puts "</code></pre>"
            end
            inOverview = false
            inSample = false
            inClass = false
        when "doc", "method"
            # document the thing on the next line
            met = JMethod.new
            met.setDoc(line.match(/@#{m.captures[0]}(.*)/).captures[0])
            klasses.last().getMethods().push(met)
            inDoc = true
        when "sample"
            inSample = true
            #puts "<p>#{line.match(/@sample(.+)/).captures[0]}</p>"
            #puts "<pre><code>"
        end
    else
        
        if inDoc 
            decStr = line
            m = line.match(/\s*this\.(\S+)\s*=\s*function\((.*)\)/) 
            if m
                name = m.captures[0]
                args = m.captures[1]
                #puts "matched string #{name}"
                #puts "args = #{args}"
                decStr = "#{name}(#{args})"
            end
            klasses.last().getMethods().last().setDec(decStr)
            inDoc = false
        end
        
        if inSample
            #print line
        end
        
        m = line.match(/^.*class\s+(\w+)\s*\{/)
        if m
            #puts "started class " + m.captures[0]
            #puts "<h2>class #{m.captures[0]}</h2"
        end
        
        if inOverview
            overview.append(line)
            if (line =~ /^\s+$/)
                #puts "</p>"
                #puts "<p>"
            else
                #puts line.sub(/\*(\w+)\*/,"<em>\\1</em>")
            end
        end
    end
end

of = File.new(outfile,"w+");

of.puts "<html>"
of.puts "<head> <link rel='stylesheet' href='doc.css'/> </head>"
of.puts "<body>"



of.puts "<h1>#{overview.getTitle()}</h1>"
inCode = false;
of.puts "<div class='overview'>"
for l in overview.getText()
    if(l.match(/^\s*$/))
        if(inCode)
            of.puts "</code></pre>"
            inCode = false
        end
        of.puts "<p class='overview'>"
    end
    if(l.match(/^\s\s\s\s\S(.*)$/))
        if(!inCode)
            of.print "<pre><code>"
            inCode = true
        else
            if(l.length > 10)
                l = l.slice(4..-1)
            end 
        end
    end
    of.puts processMarkdown(l)
end
of.puts "</div>"



of.puts "<h1>Class Overview</h1>"
of.puts "<ul>"
for k in klasses
    of.puts "<li><a href='##{k.getName()}'>#{k.getName()}</a></li>"
end
of.puts "</ul>"

for k in klasses
    of.puts "<h2><a name='#{k.getName}'>#{k.getName()}</a></h2>"
    
    of.puts "<p class='description'>#{processMarkdown(k.getDoc())}</p>"
    
    
    if (k.getProperties().length > 0)
        of.puts "<h3>Properties</h3>"
        of.puts "<ul class='properties'>"
        k.getProperties().each do |name,prop|
            of.puts "<li><b class='name'>#{name}</b> #{processMarkdown(prop.getDoc())}</li>"
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

    of.puts "\n\n\n"
end




of.puts "</body></html>"

