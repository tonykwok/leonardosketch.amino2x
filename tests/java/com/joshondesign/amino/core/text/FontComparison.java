package com.joshondesign.amino.core.text;

import com.joshondesign.amino.core.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: josh
 * Date: 9/26/11
 * Time: 7:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class FontComparison implements Core.InitCallback {
    public static void main(String ... args) {
        Core.init(new FontComparison());
    }

    public void call(Core core) throws Exception {
        Window window = core.createResizableWindow(1024, 700);
        window.setBackgroundFill(AminoColor.WHITE);
        String text = "ABC abc 123 Greetings Earthling!";

        int size = 12;
        List<AminoFont> fonts = new ArrayList<AminoFont>();
        fonts.add(core.loadFont(this.getClass().getResource("OpenSans-Regular.ttf")).withSize(size));
        fonts.add(core.loadFont(this.getClass().getResource("DejaVuSans.ttf")).withSize(size));
        fonts.add(core.loadFont(this.getClass().getResource("DroidSans.ttf")).withSize(size));
        fonts.add(core.loadFont(this.getClass().getResource("FreeUniversal-Regular.ttf")).withSize(size));
        fonts.add(core.loadFont(this.getClass().getResource("LiberationSans-Regular.ttf")).withSize(size));
        fonts.add(core.loadFont(this.getClass().getResource("NewsCycle-Regular.ttf")).withSize(size));
        fonts.add(core.loadFont(this.getClass().getResource("Vera.ttf")).withSize(size));



        Group g = new Group();
        int count = 0;
        for(AminoFont fnt: fonts) {
            g.add(new Text()
                    .setFont(fnt)
                    .setText(text)
                    .setFill(AminoColor.BLACK)
                    .setX(50)
                    .setY(count*30+50));
            count++;
        }
        window.setRoot(g);

    }
}
