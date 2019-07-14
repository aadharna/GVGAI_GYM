package qmul.gvgai.engine.ontology.effects.unary;

import qmul.gvgai.engine.core.content.InteractionContent;
import qmul.gvgai.engine.core.game.Game;
import qmul.gvgai.engine.core.logging.Logger;
import qmul.gvgai.engine.core.logging.Message;
import qmul.gvgai.engine.core.vgdl.VGDLSprite;
import qmul.gvgai.engine.ontology.effects.Effect;


public class KillIfAlive extends Effect {

    public KillIfAlive(InteractionContent cnt)
    {
        is_kill_effect = true;
        this.parseParameters(cnt);
    }

    @Override
    public void execute(VGDLSprite sprite1, VGDLSprite sprite2, Game game) {
	if(sprite1 == null || sprite2 == null){
            Logger.getInstance().addMessage(new Message(Message.WARNING, "Neither 1st not 2nd sprite can be EOS with KillIfAlive interaction."));
            return;
        }
	
        //boolean variable set to false to indicate the sprite was not transformed
    	if (!game.kill_list.contains(sprite2))
        	game.killSprite(sprite1, false);
    }
}
