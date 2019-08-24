package qmul.gvgai.engine.ontology.effects.binary;

import lombok.extern.slf4j.Slf4j;
import qmul.gvgai.engine.core.vgdl.VGDLSprite;
import qmul.gvgai.engine.core.content.InteractionContent;
import qmul.gvgai.engine.core.game.Game;
import qmul.gvgai.engine.ontology.effects.Effect;
import qmul.gvgai.engine.ontology.sprites.Resource;


@Slf4j
public class CollectResource extends Effect {

    public boolean killResource;

    public CollectResource(InteractionContent cnt) {
        killResource = true;
        this.parseParameters(cnt);
        is_kill_effect = killResource;
    }

    @Override
    public void execute(VGDLSprite sprite1, VGDLSprite sprite2, Game game) {
        if (sprite1 == null || sprite2 == null) {
            log.warn("Neither the 1st nor 2nd sprite can be EOS with CollectResource interaction.");
            return;
        }

        if (sprite1.is_resource) {
            Resource r = (Resource) sprite1;
            applyScore = false;
            int numResources = sprite2.getAmountResource(r.resource_type);

            if (numResources < game.getResourceLimit(r.resource_type)) {
                int topup = Math.min(r.value, game.getResourceLimit(r.resource_type) - numResources);
                applyScore = true;
                sprite2.modifyResource(r.resource_type, topup);

                if (killResource)
                    //boolean variable set to false to indicate the sprite was not transformed
                    game.killSprite(sprite1, true);
            }

        }
    }
}
