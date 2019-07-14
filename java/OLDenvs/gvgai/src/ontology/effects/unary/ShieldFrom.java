package qmul.gvgai.engine.ontology.effects.unary;

import qmul.gvgai.engine.core.vgdl.VGDLRegistry;
import qmul.gvgai.engine.core.vgdl.VGDLSprite;
import qmul.gvgai.engine.core.content.InteractionContent;
import qmul.gvgai.engine.core.game.Game;
import qmul.gvgai.engine.core.logging.Logger;
import qmul.gvgai.engine.core.logging.Message;
import qmul.gvgai.engine.ontology.effects.Effect;


public class ShieldFrom extends Effect {


    public String stype;
    public int istype;

    public String ftype;
    public long iftype;


    public ShieldFrom(InteractionContent cnt) throws Exception
    {
        this.parseParameters(cnt);
        istype = VGDLRegistry.GetInstance().getRegisteredSpriteValue(stype);
        if(istype == -1){
            throw new Exception("Undefined sprite " + stype);
        }
        iftype = ftype.hashCode();
    }

    @Override
    public void execute(VGDLSprite sprite1, VGDLSprite sprite2, Game game)
    {
	if(sprite1 == null){
	    Logger.getInstance().addMessage(new Message(Message.WARNING, "1st sprite can't be EOS with ShieldFrom interaction."));
	    return;
	}
	
        game.addShield(sprite1.getType(), istype, iftype);
    }

}
