package qmul.gvgai.engine.ontology.sprites.producer;

import java.awt.Dimension;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import qmul.gvgai.engine.core.vgdl.VGDLRegistry;
import qmul.gvgai.engine.core.vgdl.VGDLSprite;
import qmul.gvgai.engine.core.content.SpriteContent;
import qmul.gvgai.engine.ontology.Types;
import qmul.gvgai.engine.tools.Vector2d;


public class Portal extends SpriteProducer
{
    public String stype;
    public int itype;

    public Portal(){}

    public Portal(Vector2d position, Dimension size, SpriteContent cnt)
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
        is_static = true;
        portal = true;
        color = Color.BLUE;
    }

    public void postProcess()
    {
        super.postProcess();
        itype = VGDLRegistry.GetInstance().getRegisteredSpriteValue(stype);
    }

    public VGDLSprite copy()
    {
        Portal newSprite = new Portal();
        this.copyTo(newSprite);
        return newSprite;
    }

    public void copyTo(VGDLSprite target)
    {
        Portal targetSprite = (Portal) target;
        targetSprite.stype = this.stype;
        targetSprite.itype = this.itype;
        super.copyTo(targetSprite);
    }
    
    @Override
    public ArrayList<String> getDependentSprites(){
    	ArrayList<String> result = new ArrayList<String>();
    	if(stype != null) result.add(stype);
    	
    	return result;
    }
}
