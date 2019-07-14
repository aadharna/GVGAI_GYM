package qmul.gvgai.engine.ontology.effects.binary;

import java.awt.Rectangle;
import java.util.ArrayList;

import qmul.gvgai.engine.core.vgdl.VGDLSprite;
import qmul.gvgai.engine.core.content.InteractionContent;
import qmul.gvgai.engine.core.game.Game;
import qmul.gvgai.engine.core.logging.Logger;
import qmul.gvgai.engine.core.logging.Message;
import qmul.gvgai.engine.ontology.Types;
import qmul.gvgai.engine.ontology.effects.Effect;
import qmul.gvgai.engine.ontology.physics.ContinuousPhysics;
import qmul.gvgai.engine.ontology.physics.GridPhysics;
import qmul.gvgai.engine.tools.Direction;
import qmul.gvgai.engine.tools.Vector2d;


public class PullWithIt extends Effect
{
    private int lastGameTime;

    private ArrayList<VGDLSprite> spritesThisCycle;

    public boolean pixelPerfect;

    public PullWithIt(InteractionContent cnt)
    {
        pixelPerfect = false;
        lastGameTime = -1;
        spritesThisCycle = new ArrayList<VGDLSprite>();
        this.parseParameters(cnt);
    }

    @Override
    public void execute(VGDLSprite sprite1, VGDLSprite sprite2, Game game)
    {
        if(sprite1 == null || sprite2 == null){
            Logger.getInstance().addMessage(new Message(Message.WARNING, "Neither the 1st nor 2nd sprite can be EOS with PullWithIt interaction."));
            return;
        }
        //Keep in the list, for the current cycle, the sprites that have triggered this event.
        int currentGameTime = game.getGameTick();
        if(currentGameTime > lastGameTime)
        {
            spritesThisCycle.clear();
            lastGameTime = currentGameTime;
        }

        //the event gets triggered only once per time-step on each sprite.
        if(spritesThisCycle.contains(sprite1))
            return;

        spritesThisCycle.add(sprite1);

        //And go on.
        Rectangle r = sprite1.lastrect;
        Vector2d v = sprite2.lastDirection();
        v.normalise();

        int gridsize = 1;
        if(sprite1.physicstype == Types.GRID)
        {
            GridPhysics gp = (GridPhysics)(sprite1.physics);
            gridsize = gp.gridsize.width;
        }else
        {
            ContinuousPhysics gp = (ContinuousPhysics)(sprite1.physics);
            gridsize = gp.gridsize.width;
        }

        sprite1._updatePos(new Direction(v.x, v.y), (int) (sprite2.speed*gridsize));

        if(sprite1.physicstype != Types.GRID)
        {
            sprite1.rect.y = sprite2.rect.y-sprite2.rect.height;
            sprite1.orientation = new Direction(sprite1.orientation.x(),0.0);
        }

        sprite1.lastrect = new Rectangle(r);

        if(pixelPerfect)
        {
            sprite1.setRect(sprite2.rect);
        }
    }
}
