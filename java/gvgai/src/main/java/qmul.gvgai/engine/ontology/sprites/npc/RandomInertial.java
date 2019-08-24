package qmul.gvgai.engine.ontology.sprites.npc;

import java.awt.Dimension;

import qmul.gvgai.engine.core.vgdl.VGDLSprite;
import qmul.gvgai.engine.core.content.SpriteContent;
import qmul.gvgai.engine.ontology.Types;
import qmul.gvgai.engine.tools.Vector2d;


public class RandomInertial extends RandomNPC
{
    public RandomInertial(){}

    public RandomInertial(Vector2d position, Dimension size, SpriteContent cnt)
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
        physicstype = Types.CONT;
        is_oriented = true;
    }

    public VGDLSprite copy()
    {
        RandomInertial newSprite = new RandomInertial();
        this.copyTo(newSprite);
        return newSprite;
    }

    public void copyTo(VGDLSprite target)
    {
        RandomInertial targetSprite = (RandomInertial) target;
        super.copyTo(targetSprite);
    }
}
