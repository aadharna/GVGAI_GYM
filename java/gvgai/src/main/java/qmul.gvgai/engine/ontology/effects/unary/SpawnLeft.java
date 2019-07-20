package qmul.gvgai.engine.ontology.effects.unary;

import lombok.extern.slf4j.Slf4j;
import qmul.gvgai.engine.core.content.InteractionContent;
import qmul.gvgai.engine.core.game.Game;
import qmul.gvgai.engine.core.vgdl.VGDLRegistry;
import qmul.gvgai.engine.core.vgdl.VGDLSprite;
import qmul.gvgai.engine.ontology.effects.Effect;
import qmul.gvgai.engine.tools.Vector2d;

import java.util.ArrayList;

import static qmul.gvgai.engine.ontology.Types.LEFT;

@Slf4j
public class SpawnLeft extends Effect {

    public String stype;
    public int itype;
    public boolean stepBack;

    public SpawnLeft(InteractionContent cnt)
    {
        stepBack = false;
        this.parseParameters(cnt);
        itype = VGDLRegistry.GetInstance().getRegisteredSpriteValue(stype);
    }

    @Override
    public void execute(VGDLSprite sprite1, VGDLSprite sprite2, Game game)
    {
	if(sprite2 == null){
	    log.warn("1st sprite can't be EOS with SpawnBehind interaction.");
	    return;
	}
	
        if(game.getRandomGenerator().nextDouble() >= prob) return;
        Vector2d currentPos;
        if (stepBack)
            currentPos = sprite2.getLastPosition();
        else
            currentPos = sprite2.getPosition();
        Vector2d dir = new Vector2d(-1,0).mul(game.getBlockSize());
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
