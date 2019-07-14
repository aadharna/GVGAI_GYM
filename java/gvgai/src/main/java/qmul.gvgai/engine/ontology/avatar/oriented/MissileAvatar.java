package qmul.gvgai.engine.ontology.avatar.oriented;

import java.awt.Dimension;

import qmul.gvgai.engine.core.vgdl.VGDLSprite;
import qmul.gvgai.engine.core.content.SpriteContent;
import qmul.gvgai.engine.core.game.Game;
import qmul.gvgai.engine.tools.Direction;
import qmul.gvgai.engine.tools.Vector2d;


public class MissileAvatar extends OrientedAvatar
{
    public MissileAvatar(){}

    public MissileAvatar(Vector2d position, Dimension size, SpriteContent cnt)
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
        speed = 1;
        is_oriented = true;
    }

    /**
     * This update call is for the game tick() loop.
     * @param game current state of the game.
     */
    public void updateAvatar(Game game, boolean requestInput, boolean[] actionMask)
    {
        if (requestInput || actionMask == null) {
            //Get the input from the player.
            requestPlayerInput(game);
        }

        //MissileAvatar has no actions available. Just update movement.
        super.updatePassive();
    }

    public VGDLSprite copy()
    {
        MissileAvatar newSprite = new MissileAvatar();
        this.copyTo(newSprite);
        return newSprite;
    }

    public void copyTo(VGDLSprite target)
    {
        MissileAvatar targetSprite = (MissileAvatar) target;
        super.copyTo(targetSprite);
    }
}
