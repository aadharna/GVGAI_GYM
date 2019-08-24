package qmul.gvgai.engine.ontology.effects.unary;

import java.util.ArrayList;
import java.util.Iterator;

import qmul.gvgai.engine.core.vgdl.VGDLRegistry;
import qmul.gvgai.engine.core.vgdl.VGDLSprite;
import qmul.gvgai.engine.core.content.InteractionContent;
import qmul.gvgai.engine.core.game.Game;
import qmul.gvgai.engine.ontology.effects.Effect;


public class UndoAll extends Effect
{
    /**
     * List of sprites that do NOT respond to UndoAll. This list can be specified
     * with sprite string identifiers separated by commas.
     */
    public String notStype;

    //List of IDs of the sprites not affected by UndoAll. ArrayList for efficiency.
    private ArrayList<Integer> notItypes;

    public UndoAll(InteractionContent cnt)
    {
        this.parseParameters(cnt);
        int notItypesArray[] = VGDLRegistry.GetInstance().explode(notStype);
        notItypes = new ArrayList<>();
        for(Integer it : notItypesArray)
            notItypes.add(it);
    }

    @Override
    public void execute(VGDLSprite sprite1, VGDLSprite sprite2, Game game)
    {
        int[] gameSpriteOrder = game.getSpriteOrder();
        int spriteOrderCount = gameSpriteOrder.length;
        for(int i = 0; i < spriteOrderCount; ++i)
        {
            int spriteTypeInt = gameSpriteOrder[i];

            if(notItypes.contains(spriteTypeInt))
                continue;

            Iterator<VGDLSprite> spriteIt = game.getSpriteGroup(spriteTypeInt);
            if(spriteIt != null) while(spriteIt.hasNext())
            {
                VGDLSprite sp = spriteIt.next();
                sp.setRect(sp.lastrect);
            }
        }
    }
}
