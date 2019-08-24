package qmul.gvgai.engine.ontology.sprites;

import qmul.gvgai.engine.core.content.SpriteContent;
import qmul.gvgai.engine.core.game.Game;
import qmul.gvgai.engine.core.vgdl.VGDLSprite;
import qmul.gvgai.engine.tools.Vector2d;

import java.awt.Dimension;


public class OrientedFlicker extends Flicker{

    public OrientedFlicker(){}

    public OrientedFlicker(Vector2d position, Dimension size, SpriteContent cnt)
    {
        //Init the sprite
        this.init(position, size);

        //Specific class default parameter values.
        loadDefaults();

        //Parse the arguments.
        this.parseParameters(cnt);
    }

    protected void loadDefaults()
    {
        super.loadDefaults();
        draw_arrow = true;
        speed = 0;
        is_oriented = true;
    }

    public void update(Game game)
    {
        super.update(game);
    }

    public VGDLSprite copy()
    {
        OrientedFlicker newSprite = new OrientedFlicker();
        this.copyTo(newSprite);
        return newSprite;
    }

    public void copyTo(VGDLSprite target)
    {
        OrientedFlicker targetSprite = (OrientedFlicker) target;
        super.copyTo(targetSprite);
    }
}
