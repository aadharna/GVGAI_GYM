package qmul.gvgai.engine.ontology.effects.unary;

import lombok.extern.slf4j.Slf4j;
import qmul.gvgai.engine.core.vgdl.VGDLRegistry;
import qmul.gvgai.engine.core.vgdl.VGDLSprite;
import qmul.gvgai.engine.core.content.InteractionContent;
import qmul.gvgai.engine.core.game.Game;
import qmul.gvgai.engine.ontology.avatar.MovingAvatar;
import qmul.gvgai.engine.ontology.effects.Effect;

import java.util.Iterator;
import java.util.Objects;

@Slf4j
public class RemoveScore extends Effect {

    //Indicates if the second sprite should be killed.
    public boolean killSecond = false;
    public String stype = "";
    public int itype = -1;

    public RemoveScore(InteractionContent cnt) {
        this.parseParameters(cnt);
        if (!Objects.equals(stype, "")) {
            itype = VGDLRegistry.GetInstance().getRegisteredSpriteValue(stype);
        }
    }

    @Override
    public void execute(VGDLSprite sprite1, VGDLSprite sprite2, Game game) {
        if (sprite1 == null) {
            log.warn("1st sprite can't be EOS with RemoveScore interaction.");
            return;
        }

        if (itype == -1) {
            if (sprite1.is_avatar) {
                MovingAvatar a = (MovingAvatar) sprite1;
                a.setScore(0);
                if (killSecond && sprite2 != null)
                    game.killSprite(sprite2, true);
            }
        } else {
            Iterator<VGDLSprite> spriteIt = game.getSpriteGroup(itype);

            if (spriteIt != null) while (spriteIt.hasNext()) {
                VGDLSprite s = spriteIt.next();
                if (s.is_avatar) {
                    MovingAvatar a = (MovingAvatar) s;
                    a.setScore(0);
                    if (killSecond && sprite2 != null)
                        game.killSprite(sprite2, true);
                }
            }
        }
    }

}
