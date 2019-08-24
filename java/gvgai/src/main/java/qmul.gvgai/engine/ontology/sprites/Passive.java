package qmul.gvgai.engine.ontology.sprites;

import java.awt.Dimension;

import com.badlogic.gdx.graphics.Color;
import qmul.gvgai.engine.core.vgdl.VGDLSprite;
import qmul.gvgai.engine.core.content.SpriteContent;
import qmul.gvgai.engine.ontology.Types;
import qmul.gvgai.engine.tools.Vector2d;


public class Passive extends VGDLSprite
{
    public Passive(){}

    public Passive(Vector2d position, Dimension size, SpriteContent cnt)
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
        color = Color.RED;
    }

    public VGDLSprite copy()
    {
        Passive newSprite = new Passive();
        this.copyTo(newSprite);
        return newSprite;
    }

    public void copyTo(VGDLSprite target)
    {
        Passive targetSprite = (Passive) target;
        super.copyTo(targetSprite);
    }
}
