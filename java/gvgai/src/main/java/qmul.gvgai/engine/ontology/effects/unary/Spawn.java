package qmul.gvgai.engine.ontology.effects.unary;

import lombok.extern.slf4j.Slf4j;
import qmul.gvgai.engine.core.vgdl.VGDLRegistry;
import qmul.gvgai.engine.core.vgdl.VGDLSprite;
import qmul.gvgai.engine.core.content.InteractionContent;
import qmul.gvgai.engine.core.game.Game;
import qmul.gvgai.engine.ontology.effects.Effect;

import java.util.ArrayList;

@Slf4j
public class Spawn extends Effect {

    public String stype;
    public int itype;

    public Spawn(InteractionContent cnt) throws Exception {
        this.parseParameters(cnt);
        itype = VGDLRegistry.GetInstance().getRegisteredSpriteValue(stype);
        if (itype == -1) {
            throw new Exception("Undefined sprite " + stype);
        }
    }

    @Override
    public void execute(VGDLSprite sprite1, VGDLSprite sprite2, Game game) {
        if (sprite1 == null) {
            log.warn("1st sprite can't be EOS with Spawn interaction.");
            return;
        }
        if (game.getRandomGenerator().nextDouble() >= prob) return;
        game.addSprite(itype, sprite1.getPosition());
    }

    @Override
    public ArrayList<String> getEffectSprites() {
        ArrayList<String> result = new ArrayList<String>();
        if (stype != null) result.add(stype);

        return result;
    }
}
