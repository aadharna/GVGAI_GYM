package qmul.gvgai.engine.ontology.sprites;

import java.awt.Dimension;
import java.util.ArrayList;

import qmul.gvgai.engine.core.vgdl.VGDLRegistry;
import qmul.gvgai.engine.core.vgdl.VGDLSprite;
import qmul.gvgai.engine.core.content.SpriteContent;
import qmul.gvgai.engine.core.game.Game;
import qmul.gvgai.engine.ontology.Types;
import qmul.gvgai.engine.tools.Vector2d;


public class Spreader extends Flicker
{
    public double spreadprob;

    public String stype;

    public int itype;

    public Spreader(){}

    public Spreader(Vector2d position, Dimension size, SpriteContent cnt)
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
        spreadprob = 1.0;
    }

    public void postProcess()
    {
        super.postProcess();
        itype = -1;
        if(stype != null)
            itype = VGDLRegistry.GetInstance().getRegisteredSpriteValue(stype);
    }

    public void update(Game game)
    {
        super.update(game);
        if(age == 2)
        {
            for(Vector2d u : Types.BASEDIRS)
            {
                if(game.getRandomGenerator().nextDouble() < spreadprob)
                {
                    int newType = (itype == -1) ? this.getType() : itype;
                    game.addSprite(newType, new Vector2d(this.lastrect.x + u.x*this.lastrect.width,
                                                    this.lastrect.y + u.y*this.lastrect.height));
                }
            }
        }

    }


    public VGDLSprite copy()
    {
        Spreader newSprite = new Spreader();
        this.copyTo(newSprite);
        return newSprite;
    }

    public void copyTo(VGDLSprite target)
    {
        Spreader targetSprite = (Spreader) target;
        targetSprite.spreadprob = this.spreadprob;
        targetSprite.stype = this.stype;
        ((Spreader) target).itype = this.itype;
        super.copyTo(targetSprite);
    }
    
    @Override
    public ArrayList<String> getDependentSprites(){
    	ArrayList<String> result = new ArrayList<String>();
    	if(stype != null) result.add(stype);
    	
    	return result;
    }
}
