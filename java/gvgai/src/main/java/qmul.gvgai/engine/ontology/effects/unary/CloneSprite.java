package qmul.gvgai.engine.ontology.effects.unary;

import lombok.extern.slf4j.Slf4j;
import qmul.gvgai.engine.core.vgdl.VGDLSprite;
import qmul.gvgai.engine.core.content.InteractionContent;
import qmul.gvgai.engine.core.game.Game;
import qmul.gvgai.engine.ontology.effects.Effect;
import qmul.gvgai.engine.tools.Vector2d;

@Slf4j
public class CloneSprite extends Effect {

    public CloneSprite(InteractionContent cnt) {
        this.parseParameters(cnt);
    }

    @Override
    public void execute(VGDLSprite sprite1, VGDLSprite sprite2, Game game) {
        if (sprite1 == null) {
            log.warn("1st sprite can't be EOS with CloneSprite interaction.");
            return;
        }

        int itype = sprite1.getType();
        Vector2d pos = sprite1.getPosition();
        game.addSprite(itype, pos);
    }
}
