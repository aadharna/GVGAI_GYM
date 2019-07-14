package qmul.gvgai.engine.ontology.sprites.producer;

import java.awt.Dimension;

import com.badlogic.gdx.graphics.Color;
import qmul.gvgai.engine.core.vgdl.VGDLSprite;
import qmul.gvgai.engine.core.content.SpriteContent;
import qmul.gvgai.engine.ontology.Types;
import qmul.gvgai.engine.tools.Vector2d;


public class Bomber extends SpawnPoint
{
    public Bomber(){}

    public Bomber(Vector2d position, Dimension size, SpriteContent cnt)
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
        color = Color.ORANGE;
        is_static = false;
        is_oriented = true;
        orientation = Types.DRIGHT.copy();
        is_npc = true;
    }


    public VGDLSprite copy()
    {
        Bomber newSprite = new Bomber();
        this.copyTo(newSprite);
        return newSprite;
    }

    public void copyTo(VGDLSprite target)
    {
        Bomber targetSprite = (Bomber) target;
        super.copyTo(targetSprite);
    }
}
