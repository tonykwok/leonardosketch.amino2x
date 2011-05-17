var ghost = new Group()
.add(
  new Transform(
    new Circle().set(110.0,110.0,110.0)
    .setStrokeWidth(1.0)
    .setFill('rgb(180,179,180)')
    )
  .setTranslateX(254.0).setTranslateY(123.0)
)
.add(
  new Transform(
    new Circle().set(14.0,14.0,14.0)
    .setStrokeWidth(1.0)
    .setFill('rgb(0,0,0)')
    )
  .setTranslateX(325.0).setTranslateY(262.0)
)
.add(
  new Group().setX(296.5).setY(155.5)
    .add(
      new Transform(
        new Circle().set(24.0,24.0,24.0)
        .setStrokeWidth(1.0)
        .setFill('rgb(254,254,254)')
        )
      .setTranslateX(0.5).setTranslateY(0.5)
    )
    .add(
      new Transform(
        new Circle().set(11.5,11.5,11.5)
        .setStrokeWidth(1.0)
        .setFill('rgb(0,0,0)')
        )
      .setTranslateX(10.5).setTranslateY(19.5)
    )
)
.add(
  new Group().setX(380.5).setY(177.5)
    .add(
      new Transform(
        new Circle().set(24.0,24.0,24.0)
        .setStrokeWidth(1.0)
        .setFill('rgb(254,254,254)')
        )
      .setTranslateX(0.5).setTranslateY(0.5)
    )
    .add(
      new Transform(
        new Circle().set(11.5,11.5,11.5)
        .setStrokeWidth(1.0)
        .setFill('rgb(0,0,0)')
        )
      .setTranslateX(10.5).setTranslateY(20.5)
    )
)
;

var hill =   new Transform(
    new PathNode()
      .setPath(
        new Path()
          .moveTo(135.0,188.0)
          .curveTo(109.04261675795271,188.0,88.0,209.04261675795271,88.0,235.0)
          .curveTo(88.0,238.32176101420833,88.34459954095942,241.56303649309456,88.0000000077,234.0)
          .lineTo(88.0,244.6900278214804)
          .lineTo(88.0,486.0)
          .lineTo(182.0,486.0)
          .lineTo(182.0,234.0)
          .lineTo(181.98957261083083,234.0)
          .curveTo(181.4574062391759,208.5043247572393,160.62322394499301,188.0,135.0,188.0)
          .closeTo()
          .build()
      )
        .setStrokeWidth(3.0)
        .setStroke("#00cc00")
        .setFill('rgb(96,255,99)')
        )
  .setTranslateX(0.5).setTranslateY(0.5)

