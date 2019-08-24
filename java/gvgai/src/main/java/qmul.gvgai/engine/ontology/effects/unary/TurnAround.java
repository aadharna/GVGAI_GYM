package qmul.gvgai.engine.ontology.effects.unary;

import lombok.extern.slf4j.Slf4j;
import qmul.gvgai.engine.core.vgdl.VGDLSprite;
import qmul.gvgai.engine.core.content.InteractionContent;
import qmul.gvgai.engine.core.game.Game;
import qmul.gvgai.engine.ontology.Types;
import qmul.gvgai.engine.ontology.effects.Effect;

@Slf4j
public class TurnAround extends Effect {
    public TurnAround(InteractionContent cnt) {
        this.parseParameters(cnt);
    }

    @Override
    public void execute(VGDLSprite sprite1, VGDLSprite sprite2, Game game) {
        if (sprite1 == null) {
            log.warn("1st sprite can't be EOS with TurnAround interaction.");
            return;
        }

        sprite1.setRect(sprite1.lastrect);
        sprite1.lastmove = sprite1.cooldown;
        sprite1.physics.activeMovement(sprite1, Types.DDOWN, sprite1.speed);
        sprite1.lastmove = sprite1.cooldown;
        sprite1.physics.activeMovement(sprite1, Types.DDOWN, sprite1.speed);
        game.reverseDirection(sprite1);
        game._updateCollisionDict(sprite1);
    }
}
