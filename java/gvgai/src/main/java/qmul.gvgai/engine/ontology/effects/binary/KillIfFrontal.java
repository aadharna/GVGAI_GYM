package qmul.gvgai.engine.ontology.effects.binary;

import lombok.extern.slf4j.Slf4j;
import qmul.gvgai.engine.core.vgdl.VGDLSprite;
import qmul.gvgai.engine.core.content.InteractionContent;
import qmul.gvgai.engine.core.game.Game;
import qmul.gvgai.engine.ontology.Types;
import qmul.gvgai.engine.ontology.effects.Effect;
import qmul.gvgai.engine.tools.Direction;
import qmul.gvgai.engine.tools.Vector2d;

@Slf4j
public class KillIfFrontal extends Effect {

    public KillIfFrontal(InteractionContent cnt) {
        is_kill_effect = true;
        this.parseParameters(cnt);
    }

    @Override
    public void execute(VGDLSprite sprite1, VGDLSprite sprite2, Game game) {
        if (sprite1 == null || sprite2 == null) {
            log.warn("Neither the 1st nor 2nd sprite can be EOS with KillIfFrontal interaction.");
            return;
        }

        //Kills the sprite, only if they are going in opposite directions or sprite1 is static.
        Vector2d firstV = sprite1.lastDirection();
        Vector2d otherV = sprite2.lastDirection();

        firstV.normalise();
        otherV.normalise();

        //If the sum of the two vectors (normalized) is (0.0), directions are opposite.
        Direction sumDir = new Direction(firstV.x + otherV.x, firstV.y + otherV.y);
        Direction firstDir = new Direction(firstV.x, firstV.y);

        applyScore = false;
        if (firstDir.equals(Types.DNONE) || (sumDir.equals(Types.DNONE))) {
            applyScore = true;
            //boolean variable set to false to indicate the sprite was not transformed
            game.killSprite(sprite1, false);
        }

    }
}
