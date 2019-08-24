package qmul.gvgai.engine.ontology.sprites;

import java.awt.Dimension;

import com.badlogic.gdx.graphics.Color;
import qmul.gvgai.engine.core.vgdl.VGDLSprite;
import qmul.gvgai.engine.core.content.SpriteContent;
import qmul.gvgai.engine.ontology.Types;
import qmul.gvgai.engine.tools.Vector2d;


public class Conveyor extends VGDLSprite
{
    public Conveyor(){}

    public Conveyor(Vector2d position, Dimension size, SpriteContent cnt)
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
        is_static = true;
        color = Color.BLUE;
        jump_strength = 1;
        draw_arrow = true;
        is_oriented = true;
    }


    public VGDLSprite copy()
    {
        Conveyor newSprite = new Conveyor();
        this.copyTo(newSprite);
        return newSprite;
    }

    public void copyTo(VGDLSprite target)
    {
        Conveyor targetSprite = (Conveyor) target;
        super.copyTo(targetSprite);
    }
}
