package qmul.gvgai.engine.ontology.effects.unary;

import lombok.extern.slf4j.Slf4j;
import qmul.gvgai.engine.core.content.InteractionContent;
import qmul.gvgai.engine.core.game.Game;
import qmul.gvgai.engine.core.vgdl.VGDLSprite;
import qmul.gvgai.engine.ontology.effects.Effect;

@Slf4j
public class StepBack extends Effect {
    public boolean pixelPerfect;

    public StepBack(InteractionContent cnt) {
        pixelPerfect = false;
        this.parseParameters(cnt);
    }

    @Override
    public void execute(VGDLSprite sprite1, VGDLSprite sprite2, Game game) {
        if (sprite1 == null) {
            log.warn("1st sprite can't be EOS with StepBack interaction.");
            return;
        }

        if (pixelPerfect && sprite2 != null) //Sprite2 could be Null in an EOS case.
            sprite1.setRect(calculatePixelPerfect(sprite1, sprite2));
        else
            sprite1.setRect(sprite1.lastrect);
    }

}
