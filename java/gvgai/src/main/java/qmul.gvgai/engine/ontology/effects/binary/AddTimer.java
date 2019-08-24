package qmul.gvgai.engine.ontology.effects.binary;

import qmul.gvgai.engine.core.vgdl.VGDLFactory;
import qmul.gvgai.engine.core.vgdl.VGDLSprite;
import qmul.gvgai.engine.core.content.InteractionContent;
import qmul.gvgai.engine.core.game.Game;
import qmul.gvgai.engine.ontology.effects.Effect;
import qmul.gvgai.engine.ontology.effects.TimeEffect;


public class AddTimer extends TimeEffect
{
    //Name of the effect this TimeEffect inserts in the queue of time effects.
    public String ftype;

    //Effect to be execuced in a number of time steps.
    public Effect timerDelegate;

    public AddTimer() {}

    public AddTimer(InteractionContent cnt) throws Exception
    {
        this.parseParameters(cnt);

        //We need to build the interaction content for the delegated effect.
        InteractionContent icDelegate = new InteractionContent(cnt.line);
        icDelegate.function = ftype;

        //Create the new effect with the function specified in "ftype" (no paramterizations allowed yet).
        timerDelegate = VGDLFactory.GetInstance().createEffect(null, icDelegate);
    }

    @Override
    public void execute(VGDLSprite sprite1, VGDLSprite sprite2, Game game)
    {
        //Adds a timer with the inner effect as delegate.
        TimeEffect tef = new TimeEffect(timerDelegate);
        super.copyTo(tef);
        tef.delegate = timerDelegate;
        tef.itype = -1; //Triggered by time, not by collision. It cannot depend on a particular sprite.
        tef.isNative = false;
        tef.planExecution(game);
        game.addTimeEffect(tef);
    }

    public TimeEffect copy()
    {
        AddTimer newTimer = new AddTimer();
        this.copyTo(newTimer);
        return newTimer;
    }

    public void copyTo(TimeEffect adT)
    {
        AddTimer timer = (AddTimer) adT;
        timer.delegate = this.delegate;
        timer.itype = this.itype;
        timer.isNative = this.isNative;

        super.copyTo(timer);
    }
}
