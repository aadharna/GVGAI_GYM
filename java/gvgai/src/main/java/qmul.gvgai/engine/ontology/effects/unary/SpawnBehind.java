package qmul.gvgai.engine.ontology.effects.unary;

import lombok.extern.slf4j.Slf4j;
import qmul.gvgai.engine.core.vgdl.VGDLRegistry;
import qmul.gvgai.engine.core.vgdl.VGDLSprite;
import qmul.gvgai.engine.core.content.InteractionContent;
import qmul.gvgai.engine.core.game.Game;
import qmul.gvgai.engine.ontology.effects.Effect;
import qmul.gvgai.engine.tools.Vector2d;

import java.util.ArrayList;

@Slf4j
public class SpawnBehind extends Effect {

    public String stype;
    public int itype;

    public SpawnBehind(InteractionContent cnt) throws Exception {
        this.parseParameters(cnt);
        itype = VGDLRegistry.GetInstance().getRegisteredSpriteValue(stype);
        if (itype == -1) {
            throw new Exception("Undefined sprite " + stype);
        }
    }

    @Override
    public void execute(VGDLSprite sprite1, VGDLSprite sprite2, Game game) {
        if (sprite2 == null) {
            log.warn("1st sprite can't be EOS with SpawnBehind interaction.");
            return;
        }

        if (game.getRandomGenerator().nextDouble() >= prob) return;
        Vector2d lastPos = sprite2.getLastPosition();
        if (lastPos != null) {
            game.addSprite(itype, lastPos);
        }
    }

    @Override
    public ArrayList<String> getEffectSprites() {
        ArrayList<String> result = new ArrayList<String>();
        if (stype != null) result.add(stype);

        return result;
    }
}
