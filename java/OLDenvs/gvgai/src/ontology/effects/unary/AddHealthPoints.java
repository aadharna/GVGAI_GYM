package qmul.gvgai.engine.ontology.effects.unary;

import qmul.gvgai.engine.core.vgdl.VGDLSprite;
import qmul.gvgai.engine.core.content.InteractionContent;
import qmul.gvgai.engine.core.game.Game;
import qmul.gvgai.engine.core.logging.Logger;
import qmul.gvgai.engine.core.logging.Message;
import qmul.gvgai.engine.ontology.effects.Effect;


public class AddHealthPoints extends Effect
{
    public int value; //healthpoints added from sprite1
    public boolean killSecond = false;

    public AddHealthPoints(InteractionContent cnt)
    {
        value = 1;
        this.parseParameters(cnt);
    }

    @Override
    public void execute(VGDLSprite sprite1, VGDLSprite sprite2, Game game)
    {
	if(sprite1 == null){
	    Logger.getInstance().addMessage(new Message(Message.WARNING, "1st sprite can't be EOS with AddHealthPoints interaction."));
	    return;
	}
	
        applyScore = false;
        if(sprite1.healthPoints + value < sprite1.limitHealthPoints) {
            sprite1.healthPoints += value;

            if (sprite1.healthPoints > sprite1.maxHealthPoints)
                sprite1.maxHealthPoints = sprite1.healthPoints;

            applyScore = true;

            if(killSecond && sprite2 != null)
                //boolean variable set to false to indicate the sprite was not transformed
                game.killSprite(sprite2, false);
        }
    }
}
