package qmul.gvgai.engine.ontology.effects.unary;

import lombok.extern.slf4j.Slf4j;
import qmul.gvgai.engine.core.content.InteractionContent;
import qmul.gvgai.engine.core.game.Game;
import qmul.gvgai.engine.core.vgdl.VGDLSprite;
import qmul.gvgai.engine.ontology.effects.Effect;

@Slf4j
public class KillIfAlive extends Effect {

    public KillIfAlive(InteractionContent cnt) {
        is_kill_effect = true;
        this.parseParameters(cnt);
    }

    @Override
    public void execute(VGDLSprite sprite1, VGDLSprite sprite2, Game game) {
        if (sprite1 == null || sprite2 == null) {
            log.warn("Neither 1st not 2nd sprite can be EOS with KillIfAlive interaction.");
            return;
        }

        //boolean variable set to false to indicate the sprite was not transformed
        if (!game.kill_list.contains(sprite2))
            game.killSprite(sprite1, false);
    }
}
