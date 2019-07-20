package qmul.gvgai.engine.ontology.effects.unary;

import lombok.extern.slf4j.Slf4j;
import qmul.gvgai.engine.core.content.InteractionContent;
import qmul.gvgai.engine.core.game.Game;
import qmul.gvgai.engine.core.vgdl.VGDLRegistry;
import qmul.gvgai.engine.core.vgdl.VGDLSprite;
import qmul.gvgai.engine.ontology.effects.Effect;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

@Slf4j
public class SubtractHealthPoints extends Effect {
    public int value; //healthpoints removed from sprite1
    public String stype = "";
    public int itype = -1;
    public int limit; //kills sprite1 when less or equal to this value (default=0).
    public String scoreChangeIfKilled;
    private String defScoreChange;

    public SubtractHealthPoints(InteractionContent cnt) throws Exception {
        is_kill_effect = true;
        limit = 0;
        value = 1;
        scoreChangeIfKilled = "0";
        this.parseParameters(cnt);
        if (!Objects.equals(stype, "")) {
            itype = VGDLRegistry.GetInstance().getRegisteredSpriteValue(stype);
            if (itype == -1) {
                throw new Exception("Undefined sprite " + stype);
            }
        }
        defScoreChange = scoreChange;
    }

    @Override
    public void execute(VGDLSprite sprite1, VGDLSprite sprite2, Game game) {
        VGDLSprite s = sprite1;
        if (itype != -1) {
            ArrayList<Integer> subtypes = game.getSubTypes(itype);
            for (Integer i : subtypes) {
                Iterator<VGDLSprite> spriteIt = game.getSpriteGroup(i);
                if (spriteIt != null) while (spriteIt.hasNext()) {
                    try {
                        s = spriteIt.next();
                        break;
                    } catch (ClassCastException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            if (sprite1 == null) {
                log.warn("1st sprite can't be EOS with SubtractHealthPoints interaction.");
                return;
            }
        }
        s.healthPoints -= value;
        if (s.healthPoints <= limit) {
            //boolean variable set to false to indicate the sprite was not transformed
            game.killSprite(s, false);
            scoreChange = scoreChangeIfKilled;
        } else {
            scoreChange = defScoreChange;
        }
    }
}
