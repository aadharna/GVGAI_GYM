package qmul.gvgai.engine.core.vgdl;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import qmul.gvgai.engine.core.game.Game;

import java.util.ArrayList;

public class VGDLRenderer {

    private final Pixmap image;
    private final Game game;
    private final int gridHeight;
    private final int gridWidth;
    private final int numSprites;
    private final int blockSize;

    public VGDLRenderer(Game game) {

        this.game = game;
        var size = game.getScreenSize();
        this.blockSize = game.getBlockSize();

        this.gridHeight = (int)size.getHeight() / blockSize;
        this.gridWidth = (int)size.getHeight() / blockSize;

        this.numSprites = game.getSpriteGroups().length;

        image = new Pixmap((int) size.getWidth(), (int) size.getHeight(), Pixmap.Format.RGB888);
        image.setBlending(Pixmap.Blending.SourceOver);
        image.setFilter(Pixmap.Filter.BiLinear);
    }

    public byte[] paintOneHotBuffer() {
        var gameSpriteOrder = game.getSpriteOrder();
        var spriteGroups = game.getSpriteGroups();

        var oneHotBuffer = new byte[numSprites*gridWidth*gridHeight];

        for (Integer spriteTypeInt : gameSpriteOrder) {
            if (spriteGroups[spriteTypeInt] != null) {
                ArrayList<VGDLSprite> spritesList = spriteGroups[spriteTypeInt].getSprites();
                for (VGDLSprite sp : spritesList) {
                    int spriteX = (int) sp.getPosition().x / blockSize;
                    int spriteY = (int) sp.getPosition().y / blockSize;

                    int spriteIdx = spriteTypeInt;

                    oneHotBuffer[spriteY*numSprites*gridWidth + spriteX*numSprites + spriteIdx] = 1;
                }
            }
        }

        return oneHotBuffer;
    }

    public byte[] paintFrameBuffer() {

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

        return getBuffer();
    }


    public byte[] getBuffer() {
        byte[] observationBuffer = new byte[image.getHeight() * image.getWidth() * 3];
        image.getPixels().position(0);
        image.getPixels().get(observationBuffer);

        return observationBuffer;
    }

    public void dispose() {
        if (!image.isDisposed()) {
            image.dispose();
        }
    }
}
