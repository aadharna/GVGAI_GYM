package qmul.gvgai.engine.ontology.sprites.npc;

import java.awt.Dimension;

import qmul.gvgai.engine.core.vgdl.VGDLSprite;
import qmul.gvgai.engine.core.content.SpriteContent;
import qmul.gvgai.engine.core.game.Game;
import qmul.gvgai.engine.ontology.Types;
import qmul.gvgai.engine.tools.Direction;
import qmul.gvgai.engine.tools.Utils;
import qmul.gvgai.engine.tools.Vector2d;

/**
 * Created by Diego on 24/02/14.
 */
public class RandomAltChaser extends AlternateChaser{

    public double epsilon;

    public RandomAltChaser(){}

    public RandomAltChaser(Vector2d position, Dimension size, SpriteContent cnt)
    {
        //Init the sprite
        this.init(position, size);

        //Specific class default parameter values.
        loadDefaults();

        //Parse the arguments.
        this.parseParameters(cnt);
    }

    protected void loadDefaults()
    {
        super.loadDefaults();
        epsilon = 0.0;
    }

    public void postProcess()
    {
        super.postProcess();
    }

    public void update(Game game)
    {
        double roll = game.getRandomGenerator().nextDouble();
        if(roll < epsilon)
        {
            //do a sampleRandom move.
            super.updatePassive();
            Direction act = (Direction) Utils.choice(Types.DBASEDIRS, game.getRandomGenerator());
            this.physics.activeMovement(this, act, this.speed);
        }else
        {
            super.update(game);
        }
    }

    public VGDLSprite copy()
    {
        RandomAltChaser newSprite = new RandomAltChaser();
        this.copyTo(newSprite);
        return newSprite;
    }

    public void copyTo(VGDLSprite target)
    {
        RandomAltChaser targetSprite = (RandomAltChaser) target;
        targetSprite.epsilon = this.epsilon;
        super.copyTo(targetSprite);
    }

}
