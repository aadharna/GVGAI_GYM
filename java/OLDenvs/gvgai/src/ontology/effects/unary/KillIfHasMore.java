package qmul.gvgai.engine.ontology.effects.unary;

import qmul.gvgai.engine.core.vgdl.VGDLRegistry;
import qmul.gvgai.engine.core.vgdl.VGDLSprite;
import qmul.gvgai.engine.core.content.InteractionContent;
import qmul.gvgai.engine.core.game.Game;
import qmul.gvgai.engine.core.logging.Logger;
import qmul.gvgai.engine.core.logging.Message;
import qmul.gvgai.engine.ontology.effects.Effect;


public class KillIfHasMore extends Effect
{
    public String resource;
    public int resourceId;
    public int limit;

    public KillIfHasMore(InteractionContent cnt) throws Exception
    {
        is_kill_effect = true;
        resourceId = -1;
        this.parseParameters(cnt);
        resourceId = VGDLRegistry.GetInstance().getRegisteredSpriteValue(resource);
        if(resourceId == -1){
            throw new Exception("Undefined sprite " + resource);
        }
    }

    @Override
    public void execute(VGDLSprite sprite1, VGDLSprite sprite2, Game game)
    {
	if(sprite1 == null){
	    Logger.getInstance().addMessage(new Message(Message.WARNING, "1st sprite can't be EOS with KillIfHasMore interaction."));
	    return;
	}
	
        applyScore = false;
        if(sprite1.getAmountResource(resourceId) >= limit)
        {
            //boolean variable set to false to indicate the sprite was not transformed
            applyScore = true;
            game.killSprite(sprite1, false);
        }
    }
}
