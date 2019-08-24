package qmul.gvgai.engine.ontology.effects.unary;

import lombok.extern.slf4j.Slf4j;
import qmul.gvgai.engine.core.vgdl.VGDLSprite;
import qmul.gvgai.engine.core.content.InteractionContent;
import qmul.gvgai.engine.core.game.Game;
import qmul.gvgai.engine.ontology.effects.Effect;

@Slf4j
public class WrapAround extends Effect {

    public double offset;

    public WrapAround(InteractionContent cnt)
    {
        this.parseParameters(cnt);
    }

    @Override
    public void execute(VGDLSprite sprite1, VGDLSprite sprite2, Game game) {
	if(sprite1 == null){
	    log.warn("1st sprite can't be EOS with WrapAround interaction.");
	    return;
	}
	
        if(sprite1.orientation.x() > 0)
        {
            sprite1.rect.x = (int) (offset * sprite1.rect.width);
        }
        else if(sprite1.orientation.x() < 0)
        {
            sprite1.rect.x = (int) (game.getScreenSize().width - sprite1.rect.width * (1+offset));
        }
        else if(sprite1.orientation.y() > 0)
        {
            sprite1.rect.y = (int) (offset * sprite1.rect.height);
        }
        else if(sprite1.orientation.y() < 0)
        {
            sprite1.rect.y = (int) (game.getScreenSize().height- sprite1.rect.height * (1+offset));
        }

        sprite1.lastmove = 0;
    }
}
