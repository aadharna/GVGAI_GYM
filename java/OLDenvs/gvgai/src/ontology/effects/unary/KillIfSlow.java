package qmul.gvgai.engine.ontology.effects.unary;

import qmul.gvgai.engine.core.vgdl.VGDLSprite;
import qmul.gvgai.engine.core.content.InteractionContent;
import qmul.gvgai.engine.core.game.Game;
import qmul.gvgai.engine.core.logging.Logger;
import qmul.gvgai.engine.core.logging.Message;
import qmul.gvgai.engine.ontology.effects.Effect;
import qmul.gvgai.engine.tools.Vector2d;


public class KillIfSlow extends Effect
{
	public double limspeed;
	
    public KillIfSlow(InteractionContent cnt)
    {
        is_kill_effect = true;
        limspeed = 1.0;
        this.parseParameters(cnt);
    }

    @Override
    public void execute(VGDLSprite sprite1, VGDLSprite sprite2, Game game)
    {
	if(sprite1 == null || sprite2 == null){
            Logger.getInstance().addMessage(new Message(Message.WARNING, "Neither 1st not 2nd sprite can be EOS with KillIfSlow interaction."));
            return;
        }
	
    	double relspeed = 0.0;
    	if (sprite1.is_static){
    		relspeed = sprite2.speed;
    	}
    	else if (sprite2.is_static){
    		relspeed = sprite1.speed;
    	}
    	else{
    		double vvx = sprite1.orientation.x() - sprite2.orientation.x();
    		double vvy = sprite1.orientation.y() - sprite2.orientation.y();
    		Vector2d vv = new Vector2d(vvx,vvy);
    		relspeed = vv.mag();
    		}
    	if (relspeed < limspeed){
    		game.killSprite(sprite1, false);
    	}
    }
}
