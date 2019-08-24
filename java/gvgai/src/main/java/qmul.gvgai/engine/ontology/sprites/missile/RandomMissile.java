package qmul.gvgai.engine.ontology.sprites.missile;

import java.awt.Dimension;

import qmul.gvgai.engine.core.vgdl.VGDLSprite;
import qmul.gvgai.engine.core.content.SpriteContent;
import qmul.gvgai.engine.core.game.Game;
import qmul.gvgai.engine.ontology.Types;
import qmul.gvgai.engine.tools.Direction;
import qmul.gvgai.engine.tools.Utils;
import qmul.gvgai.engine.tools.Vector2d;


public class RandomMissile extends Missile
{
    public RandomMissile(){}

    public RandomMissile(Vector2d position, Dimension size, SpriteContent cnt)
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
        orientation = Types.DNIL;
    }

    public void update(Game game)
    {
        if(orientation.equals(Types.DNIL))
        {
            orientation = (Direction) Utils.choice(Types.DBASEDIRS, game.getRandomGenerator());
        }

        this.updatePassive();
    }

    public VGDLSprite copy()
    {
        RandomMissile newSprite = new RandomMissile();
        this.copyTo(newSprite);
        return newSprite;
    }

    public void copyTo(VGDLSprite target)
    {
        RandomMissile targetSprite = (RandomMissile) target;
        super.copyTo(targetSprite);
    }
}
