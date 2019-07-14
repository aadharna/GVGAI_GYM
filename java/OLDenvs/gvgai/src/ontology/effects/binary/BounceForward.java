package qmul.gvgai.engine.ontology.effects.binary;

import qmul.gvgai.engine.core.vgdl.VGDLSprite;
import qmul.gvgai.engine.core.content.InteractionContent;
import qmul.gvgai.engine.core.game.Game;
import qmul.gvgai.engine.core.logging.Logger;
import qmul.gvgai.engine.core.logging.Message;
import qmul.gvgai.engine.ontology.effects.Effect;
import qmul.gvgai.engine.tools.Direction;
import qmul.gvgai.engine.tools.Vector2d;


public class BounceForward extends Effect
{

    public BounceForward(InteractionContent cnt)
    {
        this.parseParameters(cnt);
    }

    @Override
    public void execute(VGDLSprite sprite1, VGDLSprite sprite2, Game game)
    {
        if(sprite1 == null || sprite2 == null){
            Logger.getInstance().addMessage(new Message(Message.WARNING, "Neither the 1st nor 2nd sprite can be EOS with BounceForward interaction."));
            return;
        }

        Vector2d dir = new Vector2d(sprite2.lastDirection());
        dir.normalise();

        if(sprite2.lastDirection().x * sprite2.orientation.x() < 0)
            dir.x *= -1;

        if(sprite2.lastDirection().y * sprite2.orientation.y() < 0)
            dir.y *= -1;

        //Rectangle r = new Rectangle(sprite1.rect);
        sprite1.physics.activeMovement(sprite1, new Direction(dir.x, dir.y), sprite2.speed);
        //sprite1.lastrect = r;
        sprite1.orientation = new Direction(dir.x, dir.y);
        game._updateCollisionDict(sprite1);
    }
}
