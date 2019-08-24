package qmul.gvgai.engine.ontology.effects.binary;

import qmul.gvgai.engine.core.vgdl.VGDLRegistry;
import qmul.gvgai.engine.core.vgdl.VGDLSprite;
import qmul.gvgai.engine.core.content.InteractionContent;
import qmul.gvgai.engine.core.game.Game;
import qmul.gvgai.engine.ontology.effects.Effect;

import java.util.ArrayList;
import java.util.Iterator;


public class DecreaseSpeedToAll extends Effect
{
    public String stype; // decreases the speed of all sprites of type stype
    public int itype;
    public double value=0.1;

    public DecreaseSpeedToAll(InteractionContent cnt) throws Exception
    {
        is_stochastic = true;
        this.parseParameters(cnt);
        itype = VGDLRegistry.GetInstance().getRegisteredSpriteValue(stype);
        if(itype == -1){
            throw new Exception("Undefined sprite " + stype);
        }
    }

    @Override
    public void execute(VGDLSprite sprite1, VGDLSprite sprite2, Game game)
    {
        ArrayList<Integer> subtypes = game.getSubTypes(itype);
        for (Integer i: subtypes) {
            Iterator<VGDLSprite> spriteIt = game.getSpriteGroup(i);
            if (spriteIt != null) while (spriteIt.hasNext()) {
                try {
                    VGDLSprite s = spriteIt.next();
                    if (s.speed - value < 0) s.speed = 0;
                    else s.speed -= value;
                } catch (ClassCastException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
