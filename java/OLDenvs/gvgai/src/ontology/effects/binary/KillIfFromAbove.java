package qmul.gvgai.engine.ontology.effects.binary;

import qmul.gvgai.engine.core.vgdl.VGDLSprite;
import qmul.gvgai.engine.core.content.InteractionContent;
import qmul.gvgai.engine.core.game.Game;
import qmul.gvgai.engine.core.logging.Logger;
import qmul.gvgai.engine.core.logging.Message;
import qmul.gvgai.engine.ontology.effects.Effect;


public class KillIfFromAbove extends Effect
{

    public KillIfFromAbove(InteractionContent cnt)
    {
        is_kill_effect = true;
        this.parseParameters(cnt);
    }

    @Override
    public void execute(VGDLSprite sprite1, VGDLSprite sprite2, Game game)
    {
	if(sprite1 == null || sprite2 == null){
	    Logger.getInstance().addMessage(new Message(Message.WARNING, "Neither the 1st nor 2nd sprite can be EOS with KillIfFromAbove interaction."));
	    return;
	}
	
        //Kills the sprite, only if the other one is higher and moving down.
        boolean otherHigher = sprite1.lastrect.getMinY() > (sprite2.lastrect.getMinY()+(sprite2.rect.height/2));
        boolean goingDown = sprite2.rect.getMinY() > sprite2.lastrect.getMinY();

        applyScore=false;
        if (otherHigher && goingDown){
            applyScore=true;
            //boolean variable set to false to indicate the sprite was not transformed
            game.killSprite(sprite1, false);
        }
    }
}
