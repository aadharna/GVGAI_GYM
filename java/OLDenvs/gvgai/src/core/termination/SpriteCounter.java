package qmul.gvgai.engine.core.termination;

import java.util.ArrayList;

import qmul.gvgai.engine.core.vgdl.VGDLRegistry;
import qmul.gvgai.engine.core.content.TerminationContent;
import qmul.gvgai.engine.core.game.Game;


public class SpriteCounter extends Termination
{
    public String stype;
    public int itype;

    public SpriteCounter(){}

    public SpriteCounter(TerminationContent cnt) throws Exception
    {
        //Parse the arguments.
        this.parseParameters(cnt);
        itype = VGDLRegistry.GetInstance().getRegisteredSpriteValue(stype);
        if(itype == -1){
            String[] className = this.getClass().getName().split("\\.");
            throw new Exception("[" + className[className.length - 1] + "] Undefined sprite " + stype);
        }
    }

    @Override
    public boolean isDone(Game game) {

        boolean ended = super.isFinished(game);
        if(ended)
            return true;

        if(itype != -1 && game.getNumSprites(itype) - game.getNumDisabledSprites(itype) <= limit && canEnd) {
            countScore(game);
            return true;
        }

        return false;
    }

    @Override
    public ArrayList<String> getTerminationSprites() {
        ArrayList<String> result = new ArrayList<String>();
        result.add(stype);

        return result;
    }

}
