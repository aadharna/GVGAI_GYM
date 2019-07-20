package qmul.gvgai.engine.ontology.effects.unary;

import lombok.extern.slf4j.Slf4j;
import qmul.gvgai.engine.core.vgdl.VGDLSprite;
import qmul.gvgai.engine.core.content.InteractionContent;
import qmul.gvgai.engine.core.game.Game;
import qmul.gvgai.engine.ontology.effects.Effect;

@Slf4j
public class KillIfScoreNegative extends Effect {

    public KillIfScoreNegative(InteractionContent cnt) {
        is_kill_effect = true;
        this.parseParameters(cnt);
    }

    @Override
    public void execute(VGDLSprite sprite1, VGDLSprite sprite2, Game game) {
        if (sprite1 == null) {
            log.warn("1st sprite can't be EOS with KillSprite interaction.");
            return;
        }

        //boolean variable set to false to indicate the sprite was not transformed
        if (game.getScore() < 0.0) {
            game.killSprite(sprite1, false);
        }
    }
}
