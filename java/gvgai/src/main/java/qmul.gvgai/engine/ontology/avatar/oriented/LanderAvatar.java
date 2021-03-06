package qmul.gvgai.engine.ontology.avatar.oriented;

import java.awt.Dimension;

import qmul.gvgai.engine.core.content.SpriteContent;
import qmul.gvgai.engine.core.game.Game;
import qmul.gvgai.engine.core.vgdl.VGDLSprite;
import qmul.gvgai.engine.ontology.Types;
import qmul.gvgai.engine.tools.Direction;
import qmul.gvgai.engine.tools.Utils;
import qmul.gvgai.engine.tools.Vector2d;


public class LanderAvatar extends OrientedAvatar
{

    public double angle_diff = 0.3;

    public LanderAvatar(){}

    public LanderAvatar(Vector2d position, Dimension size, SpriteContent cnt)
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
        speed=5;
        orientation = Types.DNONE;
    }

    /**
     * This update call is for the game tick() loop.
     * @param game current state of the game.
     */
    public void updateAvatar(Game game, boolean requestInput, boolean[] actionMask)
    {
        super.updateAvatar(game, requestInput, actionMask);
        aim();
        move();
    }
    
    public void applyMovement(Game game, Direction action)
    {
    	//this.physics.passiveMovement(this);
    	if (physicstype != 0)
    		super.updatePassive();
    }

    
    public void aim()
    {
    	double angle = this.rotation;

    	if(Utils.processMovementActionKeys(getKeyHandler().getMask(), getPlayerID()) == Types.DLEFT) 
    	{
    		angle -= angle_diff;
    	}
    	else if (Utils.processMovementActionKeys(getKeyHandler().getMask(), getPlayerID()) == Types.DRIGHT) 
    	{
    		angle += angle_diff;
    	}
    	this._updateRotation(angle);
    }
    
    public void move()
    {
    	Direction facing = new Direction(0,0);

    	if (Utils.processMovementActionKeys(getKeyHandler().getMask(), getPlayerID()) == Types.DUP) 
    	{
    		facing = new Direction(Math.cos(this.rotation), Math.sin(this.rotation));
    		this.physics.activeMovement(this, facing, speed);
    	}
    }

    public VGDLSprite copy()
    {
        LanderAvatar newSprite = new LanderAvatar();
        this.copyTo(newSprite);
        return newSprite;
    }

    public void copyTo(VGDLSprite target)
    {
        LanderAvatar targetSprite = (LanderAvatar) target;
        targetSprite.angle_diff = this.angle_diff;
        super.copyTo(targetSprite);
    }

}
