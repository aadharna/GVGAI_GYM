package qmul.gvgai.engine.ontology.effects.binary;

import lombok.extern.slf4j.Slf4j;
import qmul.gvgai.engine.core.vgdl.VGDLRegistry;
import qmul.gvgai.engine.core.vgdl.VGDLSprite;
import qmul.gvgai.engine.core.content.InteractionContent;
import qmul.gvgai.engine.core.game.Game;
import qmul.gvgai.engine.ontology.effects.Effect;

@Slf4j
public class ChangeResource extends Effect {
    public String resource;
    public int resourceId;
    public int value;
    public boolean killResource;

    public ChangeResource(InteractionContent cnt) {
        value = 1;
        resourceId = -1;
        killResource = false;
        this.parseParameters(cnt);
        resourceId = VGDLRegistry.GetInstance().getRegisteredSpriteValue(resource);
        is_kill_effect = killResource;
    }

    @Override
    public void execute(VGDLSprite sprite1, VGDLSprite sprite2, Game game) {
        if (sprite1 == null || sprite2 == null) {
            log.warn("Neither the 1st nor 2nd sprite can be EOS with ChangeResource interaction.");
            return;
        }

        int numResources = sprite1.getAmountResource(resourceId);
        applyScore = false;
        if (numResources + value <= game.getResourceLimit(resourceId)) {
            sprite1.modifyResource(resourceId, value);
            applyScore = true;

            if (killResource)
                //boolean variable set to true, as the sprite was transformed
                game.killSprite(sprite2, true);
        }
    }
}
