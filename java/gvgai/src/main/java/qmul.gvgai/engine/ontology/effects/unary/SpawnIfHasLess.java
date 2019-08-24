package qmul.gvgai.engine.ontology.effects.unary;

import java.util.ArrayList;

import lombok.extern.slf4j.Slf4j;
import qmul.gvgai.engine.core.vgdl.VGDLRegistry;
import qmul.gvgai.engine.core.vgdl.VGDLSprite;
import qmul.gvgai.engine.core.content.InteractionContent;
import qmul.gvgai.engine.core.game.Game;
import qmul.gvgai.engine.ontology.effects.Effect;

@Slf4j
public class SpawnIfHasLess extends Effect {

    public String resource;
    public int resourceId;
    public int limit;
    public String stype;
    public int itype;

    public SpawnIfHasLess(InteractionContent cnt) throws Exception {
        resourceId = -1;
        this.parseParameters(cnt);
        resourceId = VGDLRegistry.GetInstance().getRegisteredSpriteValue(resource);
        if (resourceId == -1) {
            throw new Exception("Undefined sprite " + resource);
        }
        itype = VGDLRegistry.GetInstance().getRegisteredSpriteValue(stype);
        if (itype == -1) {
            throw new Exception("Undefined sprite " + stype);
        }
    }

    @Override
    public void execute(VGDLSprite sprite1, VGDLSprite sprite2, Game game) {
        if (sprite1 == null) {
            log.warn("1st sprite can't be EOS with SpawnIfHasLess interaction.");
            return;
        }

        applyScore = false;

        if (game.getRandomGenerator().nextDouble() >= prob) return;

        if (sprite1.getAmountResource(resourceId) <= limit) {
            game.addSprite(itype, sprite1.getPosition());
            applyScore = true;
        }
    }

    @Override
    public ArrayList<String> getEffectSprites() {
        ArrayList<String> result = new ArrayList<String>();
        if (stype != null) result.add(stype);

        return result;
    }
}
