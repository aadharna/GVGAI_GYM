package qmul.gvgai.engine.ontology.effects.unary;

import qmul.gvgai.engine.core.content.InteractionContent;
import qmul.gvgai.engine.core.game.Game;
import qmul.gvgai.engine.core.logging.Logger;
import qmul.gvgai.engine.core.logging.Message;
import qmul.gvgai.engine.core.vgdl.VGDLRegistry;
import qmul.gvgai.engine.core.vgdl.VGDLSprite;
import qmul.gvgai.engine.ontology.effects.Effect;
import qmul.gvgai.engine.tools.Vector2d;

import java.util.ArrayList;

import static qmul.gvgai.engine.ontology.Types.DOWN;

public class SpawnBelow extends Effect {

    public String stype;
    public int itype;
    public boolean stepBack;

    public SpawnBelow(InteractionContent cnt)
    {
        stepBack = false;
        this.parseParameters(cnt);
        itype = VGDLRegistry.GetInstance().getRegisteredSpriteValue(stype);
    }

    @Override
    public void execute(VGDLSprite sprite1, VGDLSprite sprite2, Game game)
    {
	if(sprite2 == null){
	    Logger.getInstance().addMessage(new Message(Message.WARNING, "1st sprite can't be EOS with SpawnBehind interaction."));
	    return;
	}
	
        if(game.getRandomGenerator().nextDouble() >= prob) return;
        Vector2d currentPos;
        if (stepBack)
            currentPos = sprite2.getLastPosition();
        else
            currentPos = sprite2.getPosition();
        Vector2d dir = new Vector2d(0,1).mul(game.getBlockSize());
        if (currentPos != null) {
            Vector2d nextPos = currentPos.add(dir);
            game.addSprite(itype, nextPos);
        }

    }
    
    @Override
    public ArrayList<String> getEffectSprites(){
    	ArrayList<String> result = new ArrayList<String>();
    	if(stype!=null) result.add(stype);
    	
    	return result;
    }
}
