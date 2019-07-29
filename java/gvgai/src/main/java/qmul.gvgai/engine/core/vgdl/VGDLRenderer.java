package qmul.gvgai.engine.core.vgdl;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import qmul.gvgai.engine.core.game.Game;

import java.util.ArrayList;

public class VGDLRenderer {

    private final Pixmap image;
    private final Game game;

    public VGDLRenderer(Game game) {

        this.game = game;
        var size = game.getScreenSize();

        image = new Pixmap((int) size.getWidth(), (int) size.getHeight(), Pixmap.Format.RGB888);
        image.setBlending(Pixmap.Blending.SourceOver);
        image.setFilter(Pixmap.Filter.BiLinear);
    }

    public void paintFrameBuffer() {

        image.setColor(Color.BLACK);
        image.fill();

        var gameSpriteOrder = game.getSpriteOrder();
        var spriteGroups = game.getSpriteGroups();


        for (Integer spriteTypeInt : gameSpriteOrder) {
            if (spriteGroups[spriteTypeInt] != null) {
                ArrayList<VGDLSprite> spritesList = spriteGroups[spriteTypeInt].getSprites();
                for (VGDLSprite sp : spritesList) {
                    if (sp != null) sp.draw(image, game);
                }

            }
        }

        //player.draw(image);
    }


    public byte[] getBuffer() {
        byte[] observationBuffer = new byte[image.getHeight() * image.getWidth() * 3];
        image.getPixels().position(0);
        image.getPixels().get(observationBuffer);

        return observationBuffer;
    }

    public void dispose() {
        image.dispose();
    }
}
