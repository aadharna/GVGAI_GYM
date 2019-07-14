package qmul.gvgai.engine.ontology.sprites.npc;

import java.awt.Dimension;

import qmul.gvgai.engine.core.vgdl.VGDLSprite;
import qmul.gvgai.engine.core.content.SpriteContent;
import qmul.gvgai.engine.tools.Vector2d;


public class Fleeing extends Chaser
{
    public Fleeing(){}

    public Fleeing(Vector2d position, Dimension size, SpriteContent cnt)
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
        fleeing = true;
    }

    public VGDLSprite copy()
    {
        Fleeing newSprite = new Fleeing();
        this.copyTo(newSprite);
        return newSprite;
    }

    public void copyTo(VGDLSprite target)
    {
        Fleeing targetSprite = (Fleeing) target;
        super.copyTo(targetSprite);
    }
}
