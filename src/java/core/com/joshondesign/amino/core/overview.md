@overview Amino: Java Scenegraph

This is the Java version of Amino, a portable 2D scenegraph.

=== A note on Properties ===

Properties in Java are implemented with getters and setters following the
JavaBeans spec. However, they are *not* technically JavaBean properties because
they do not file property change events. This is mainly for speed reasons and
because there are usually better approaches than listening for property changes.

All property setters return an instance of the node you set the property on. 
This means you can easily chain them. ex:

    Shape rect = new Rect().setWidth(100).setFill(Color.RED)

@end

