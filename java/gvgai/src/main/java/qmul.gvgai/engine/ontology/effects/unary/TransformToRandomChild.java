package qmul.gvgai.engine.ontology.effects.unary;

import lombok.extern.slf4j.Slf4j;
import qmul.gvgai.engine.core.content.InteractionContent;
import qmul.gvgai.engine.core.game.Game;
import qmul.gvgai.engine.core.vgdl.VGDLRegistry;
import qmul.gvgai.engine.core.vgdl.VGDLSprite;
import qmul.gvgai.engine.tools.Utils;

import java.util.ArrayList;

@Slf4j
public class TransformToRandomChild extends TransformTo {

    public TransformToRandomChild(InteractionContent cnt) throws Exception {
        super(cnt);
        itype = VGDLRegistry.GetInstance().getRegisteredSpriteValue(stype);
        if (itype == -1) {
            throw new Exception("Undefined sprite " + stype);
        }
    }

    @Override
    public void execute(VGDLSprite sprite1, VGDLSprite sprite2, Game game) {
        if (sprite1 == null) {
            log.warn("1st sprite can't be EOS with TransformToRandomChild interaction.");
            return;
        }

        ArrayList<Integer> subtypes = game.getSubTypes(itype);
        if (subtypes.size() > 1) {
            int[] types = new int[subtypes.size() - 1];
            int j = -1;
            for (Integer i : subtypes) {
                if (i != itype) {
                    types[++j] = i;
                }
            }
            try {
                VGDLSprite newSprite = game.addSprite(Utils.choice(types, game.getRandomGenerator()), sprite1.getPosition());
                transformTo(newSprite, sprite1, sprite2, game);
            } catch (Exception e) {
                log.warn("Can't construct a parent node to the child " + stype + " sprite in TransformToRandomChild interaction.");
                return;
            }
        }
    }

    @Override
    public ArrayList<String> getEffectSprites() {
        ArrayList<String> result = new ArrayList<String>();
        if (stype != null) result.add(stype);

        return result;
    }
}
