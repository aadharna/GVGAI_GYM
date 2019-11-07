package qmul.gvgai.engine.core.vgdl;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import qmul.gvgai.engine.core.player.Player;
import qmul.gvgai.engine.core.game.Game;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;



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

    private final VGDLRenderer renderer;

    /**
     * Creates the viewer for the game.
     *
     * @param game game to be displayed
     */
    public VGDLViewer(Game game) {
        this.game = game;
        this.size = game.getScreenSize();

        renderer = new VGDLRenderer(game);

    }

    /**
     * Main method to paint the game
     *
     * @param gx Graphics object.
     */
    public void paintComponent(Graphics gx) {
        gx.drawImage(new ImageIcon(renderer.getBuffer()).getImage(), 0,0, null);
    }

    /**
     * Paints the sprites.
     *
     * @param spriteGroupsGame sprites to paint.
     */
    public void paint(SpriteGroup[] spriteGroupsGame) {
        renderer.renderPixelObservation();
        this.repaint();
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
