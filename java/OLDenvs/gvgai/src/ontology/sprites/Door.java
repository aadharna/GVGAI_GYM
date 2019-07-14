package qmul.gvgai.engine.ontology.sprites;

import java.awt.Dimension;

import qmul.gvgai.engine.core.vgdl.VGDLSprite;
import qmul.gvgai.engine.core.content.SpriteContent;
import qmul.gvgai.engine.tools.Vector2d;


public class Door extends Immovable
{
    public Door() {}

    public Door(Vector2d position, Dimension size, SpriteContent cnt)
    {
        //Init the sprite
        this.init(position, size);

        //Specific class default parameter values.
        loadDefaults();

        //Parse the arguments.
        this.parseParameters(cnt);
    }

    public void postProcess()
    {
        super.postProcess();
    }

    protected void loadDefaults()
    {
        super.loadDefaults();
        portal = true;
    }

    public VGDLSprite copy()
    {
        Door newSprite = new Door();
        this.copyTo(newSprite);
        return newSprite;
    }

    public void copyTo(VGDLSprite target)
    {
        Door targetSprite = (Door) target;
        super.copyTo(targetSprite);
    }


}
