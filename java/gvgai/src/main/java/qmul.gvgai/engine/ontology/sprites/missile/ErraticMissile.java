package qmul.gvgai.engine.ontology.sprites.missile;

import java.awt.Dimension;

import qmul.gvgai.engine.core.vgdl.VGDLSprite;
import qmul.gvgai.engine.core.content.SpriteContent;
import qmul.gvgai.engine.tools.Vector2d;


public class ErraticMissile extends Missile
{
    public ErraticMissile(){}

    public ErraticMissile(Vector2d position, Dimension size, SpriteContent cnt)
    {
        //Init the sprite
        this.init(position, size);

        //Specific class default parameter values.
        loadDefaults();

        //Parse the arguments.
        this.parseParameters(cnt);

        System.out.println("WARNING: ErraticMissile.java, this class must set prob value, " +
          "and is_stochastic must be adjusted according to the value in the parameters is_stochastic=(>0 && <1)");
    }

    protected void loadDefaults()
    {
        super.loadDefaults();
    }

    public VGDLSprite copy()
    {
        ErraticMissile newSprite = new ErraticMissile();
        this.copyTo(newSprite);
        return newSprite;
    }

    public void copyTo(VGDLSprite target)
    {
        ErraticMissile targetSprite = (ErraticMissile) target;
        super.copyTo(targetSprite);
    }
}
