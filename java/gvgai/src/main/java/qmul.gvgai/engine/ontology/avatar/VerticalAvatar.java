package qmul.gvgai.engine.ontology.avatar;

import qmul.gvgai.engine.core.content.SpriteContent;
import qmul.gvgai.engine.core.game.Game;
import qmul.gvgai.engine.core.vgdl.VGDLSprite;
import qmul.gvgai.engine.ontology.Types;
import qmul.gvgai.engine.tools.Vector2d;

import java.awt.*;


public class VerticalAvatar extends MovingAvatar
{
    public VerticalAvatar(){}

    public VerticalAvatar(Vector2d position, Dimension size, SpriteContent cnt)
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
        //Define actions here first.
        if(actions.size()==0)
        {
            actions.add(Types.ACTIONS.ACTION_UP);
            actions.add(Types.ACTIONS.ACTION_DOWN);
        }

        super.postProcess();
    }

    protected void loadDefaults()
    {
        super.loadDefaults();
    }

    public VGDLSprite copy()
    {
        VerticalAvatar newSprite = new VerticalAvatar();
        this.copyTo(newSprite);
        return newSprite;
    }

    public void copyTo(VGDLSprite target)
    {
        VerticalAvatar targetSprite = (VerticalAvatar) target;
        super.copyTo(targetSprite);
    }
}
