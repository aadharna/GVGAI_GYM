package qmul.gvgai.engine.ontology.effects.binary;

import lombok.extern.slf4j.Slf4j;
import qmul.gvgai.engine.core.content.InteractionContent;
import qmul.gvgai.engine.core.game.Game;
import qmul.gvgai.engine.core.vgdl.VGDLSprite;
import qmul.gvgai.engine.ontology.effects.Effect;

import java.awt.*;

@Slf4j
public class Align extends Effect {
    public boolean orient = true;

    public Align(InteractionContent cnt) {
        this.parseParameters(cnt);
        setStochastic();
    }

    @Override
    public void execute(VGDLSprite sprite1, VGDLSprite sprite2, Game game) {
        if (sprite1 == null || sprite2 == null) {
            log.warn("Neither 1st not 2nd sprite can be EOS with Align interaction.");
            return;
        }
        if (orient) {
            sprite1.orientation = sprite2.orientation.copy();
        }
        sprite1.rect = new Rectangle(sprite2.rect.x, sprite2.rect.y,
                sprite1.rect.width, sprite1.rect.height);
    }
}
