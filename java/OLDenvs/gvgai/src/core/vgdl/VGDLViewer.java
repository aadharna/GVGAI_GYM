package core.vgdl;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.GdxNativesLoader;
import core.game.Game;
import core.player.LearningPlayer;
import core.player.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.util.ArrayList;


/**
 * Created with IntelliJ IDEA.
 * User: Diego
 * Date: 24/10/13
 * Time: 10:54
 * This is a Java port from Tom Schaul's VGDL - https://github.com/schaul/py-vgdl
 */
public class VGDLViewer extends JComponent {
    /**
     * Reference to the game to be painted.
     */
    public Game game;

    /**
     * Dimensions of the window.
     */
    private Dimension size;

    /**
     * Sprites to draw
     */
    public SpriteGroup[] spriteGroups;

    /**
     * Player of the game
     */
    public Player player;

    private Pixmap image;

    /**
     * Creates the viewer for the game.
     *
     * @param game game to be displayed
     */
    public VGDLViewer(Game game, Player player) {
        this.game = game;
        this.size = game.getScreenSize();
        this.player = player;

        if (player instanceof LearningPlayer) {

            if (((LearningPlayer) player).isRequiresImage()) {

                image = new Pixmap((int)size.getWidth(), (int)size.getHeight(), Pixmap.Format.RGB888);
                image.setBlending(Pixmap.Blending.SourceOver);
                image.setFilter(Pixmap.Filter.BiLinear);

                updateObservationForLearningPlayer();
            }
        }
    }

    private void updateObservationForLearningPlayer() {
        LearningPlayer learningPlayer = (LearningPlayer) player;
        paintFrameBuffer();
        learningPlayer.setObservation(image);
    }

    /**
     * Main method to paint the game
     *
     * @param gx Graphics object.
     */
    public void paintComponent(Graphics gx) {
        byte[] observationBuffer = new byte[image.getHeight()*image.getWidth()*3];
        image.getPixels().position(0);
        image.getPixels().get(observationBuffer);

        gx.drawImage(new ImageIcon(observationBuffer).getImage(), 0,0, null);
    }

    public void paintFrameBuffer() {
        //For a better graphics, enable this: (be aware this could bring performance issues depending on your HW & OS).
        //g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //g.setColor(Types.LIGHTGRAY);

        image.setColor(Color.BLACK);
        image.fill();

        int[] gameSpriteOrder = game.getSpriteOrder();
        if (this.spriteGroups != null) for (Integer spriteTypeInt : gameSpriteOrder) {
            if (spriteGroups[spriteTypeInt] != null) {
                ArrayList<VGDLSprite> spritesList = spriteGroups[spriteTypeInt].getSprites();
                for (VGDLSprite sp : spritesList) {
                    if (sp != null) sp.draw(image, game);
                }

            }
        }


        player.draw(image);

    }


    /**
     * Paints the sprites.
     *
     * @param spriteGroupsGame sprites to paint.
     */
    public void paint(SpriteGroup[] spriteGroupsGame) {

        if (player instanceof LearningPlayer) {

            if (!((LearningPlayer) player).isRequiresImage()) {
                return;
            }

            this.spriteGroups = spriteGroupsGame;
//            for (int i = 0; i < this.spriteGroups.length; ++i) {
//                this.spriteGroups[i] = new SpriteGroup(spriteGroupsGame[i].getItype());
//                this.spriteGroups[i].copyAllSprites(spriteGroupsGame[i].getSprites());
//            }

            updateObservationForLearningPlayer();
        } else {
            this.repaint();
        }
    }

    /**
     * Gets the dimensions of the window.
     *
     * @return the dimensions of the window.
     */
    public Dimension getPreferredSize() {
        return size;
    }

}
