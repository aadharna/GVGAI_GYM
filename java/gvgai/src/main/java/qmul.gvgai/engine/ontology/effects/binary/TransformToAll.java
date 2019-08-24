package qmul.gvgai.engine.ontology.effects.binary;

import lombok.extern.slf4j.Slf4j;
import qmul.gvgai.engine.core.content.InteractionContent;
import qmul.gvgai.engine.core.game.Game;
import qmul.gvgai.engine.core.vgdl.VGDLRegistry;
import qmul.gvgai.engine.core.vgdl.VGDLSprite;
import qmul.gvgai.engine.ontology.effects.unary.TransformTo;

import java.util.ArrayList;
import java.util.Iterator;

@Slf4j
public class TransformToAll extends TransformTo {

    //This effect transforms all sprites of type stype to stype2.
    // It DOES NOTHING to the sprites that cause the effect (unless they are the specified types stype or stype2)
    public String stypeTo;
    public int itypeTo;

    public TransformToAll(InteractionContent cnt) throws Exception {
        super(cnt);
        itypeTo = VGDLRegistry.GetInstance().getRegisteredSpriteValue(stypeTo);
        if (itypeTo == -1) {
            throw new Exception("Undefined sprite " + stypeTo);
        }
    }

    @Override
    public void execute(VGDLSprite sprite1, VGDLSprite sprite2, Game game) {
        //First, we need to get all sprites of type stype.
        Iterator<VGDLSprite> spriteIt = game.getSpriteGroup(itype);

        if (spriteIt != null) while (spriteIt.hasNext()) {
            VGDLSprite s = spriteIt.next();
            //Last argument: forces the creation. This could be a parameter of the effect too, if needed.
            VGDLSprite newSprite = game.addSprite(itypeTo, s.getPosition(), true);
            //newSprite inherits things from 's'. Maybe sprite2 gets killed in the process.
            super.transformTo(newSprite, s, sprite2, game);
        }
    }

    @Override
    public ArrayList<String> getEffectSprites() {
        ArrayList<String> result = new ArrayList<String>();
        if (stype != null) result.add(stype);

        return result;
    }
}
