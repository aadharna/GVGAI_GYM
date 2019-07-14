package qmul.gvgai.engine.ontology.sprites.producer;

import java.awt.Dimension;

import com.badlogic.gdx.graphics.Color;
import qmul.gvgai.engine.core.vgdl.VGDLSprite;
import qmul.gvgai.engine.core.content.SpriteContent;
import qmul.gvgai.engine.core.game.Game;
import qmul.gvgai.engine.ontology.Types;
import qmul.gvgai.engine.tools.Direction;
import qmul.gvgai.engine.tools.Utils;
import qmul.gvgai.engine.tools.Vector2d;

/**
 * Created with IntelliJ IDEA.
 * User: Diego
 * Date: 21/10/13
 * Time: 18:26
 * This is a Java port from Tom Schaul's VGDL - https://github.com/schaul/py-vgdl
 */
public class RandomBomber extends SpawnPoint
{
    public RandomBomber(){}

    public RandomBomber(Vector2d position, Dimension size, SpriteContent cnt)
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
        color = Color.ORANGE;
        is_static = false;
        is_oriented = true;
        orientation = Types.DRIGHT.copy();
        is_npc = true;
        is_stochastic = true;
        speed = 1.0;
    }

    public void update(Game game)
    {
        Direction act = (Direction) Utils.choice(Types.DBASEDIRS, game.getRandomGenerator());
        this.physics.activeMovement(this, act, this.speed);
        super.update(game);
    }

    public VGDLSprite copy()
    {
        RandomBomber newSprite = new RandomBomber();
        this.copyTo(newSprite);
        return newSprite;
    }

    public void copyTo(VGDLSprite target)
    {
        RandomBomber targetSprite = (RandomBomber) target;
        super.copyTo(targetSprite);
    }
}
