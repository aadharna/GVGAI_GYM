package qmul.gvgai.engine.ontology.effects.binary;

import qmul.gvgai.engine.core.vgdl.VGDLSprite;
import qmul.gvgai.engine.core.content.InteractionContent;
import qmul.gvgai.engine.core.game.Game;
import qmul.gvgai.engine.core.logging.*;
import qmul.gvgai.engine.ontology.Types;
import qmul.gvgai.engine.ontology.effects.Effect;

import java.awt.*;


public class AttractGaze extends Effect
{
    public boolean align = false;

    public AttractGaze(InteractionContent cnt)
    {
        this.parseParameters(cnt);
        setStochastic();
    }

    @Override
    public void execute(VGDLSprite sprite1, VGDLSprite sprite2, Game game)
    {
	if(sprite1 == null || sprite2 == null){
	    Logger.getInstance().addMessage(new Message(Message.WARNING, "Neither 1st not 2nd sprite can be EOS with AttractGaze interaction."));
	    return;
	}
	
        if(sprite1.is_oriented && sprite2.is_oriented)
        {
            if(game.getRandomGenerator().nextDouble() < prob) {
                sprite1.orientation = sprite2.orientation.copy();

                if(align)
                {
                    if(sprite1.orientation.equals(Types.DLEFT) || sprite1.orientation.equals(Types.DRIGHT))
                    {
                        //Need to align on the Y coordinate.
                        sprite1.rect = new Rectangle(sprite1.rect.x, sprite2.rect.y,
                                sprite1.rect.width, sprite1.rect.height);

                    }else{
                        //Need to align on the X coordinate.
                        sprite1.rect = new Rectangle(sprite2.rect.x, sprite1.rect.y,
                                sprite1.rect.width, sprite1.rect.height);
                    }
                }


            }
        }
    }
}
