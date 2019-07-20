package qmul.gvgai.engine.ontology.effects.unary;

import lombok.extern.slf4j.Slf4j;
import qmul.gvgai.engine.core.vgdl.VGDLSprite;
import qmul.gvgai.engine.core.content.InteractionContent;
import qmul.gvgai.engine.core.game.Game;
import qmul.gvgai.engine.ontology.effects.Effect;

@Slf4j
public class HalfSpeed extends Effect {

    public HalfSpeed(InteractionContent cnt) {
        this.parseParameters(cnt);
    }

    @Override
    public void execute(VGDLSprite sprite1, VGDLSprite sprite2, Game game) {
        if (sprite1 == null) {
            log.warn("1st sprite can't be EOS with HalfSpeed interaction.");
            return;
        }

        sprite1.speed *= 0.5;
    }
}
