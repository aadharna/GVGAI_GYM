package qmul.gvgai.engine.ontology.sprites.missile;

import java.awt.Dimension;

import qmul.gvgai.engine.core.vgdl.VGDLSprite;
import qmul.gvgai.engine.core.content.SpriteContent;
import qmul.gvgai.engine.tools.Vector2d;

/**
 * Created with IntelliJ IDEA.
 * User: Diego
 * Date: 21/10/13
 * Time: 17:35
 * This is a Java port from Tom Schaul's VGDL - https://github.com/schaul/py-vgdl
 */
public class Missile extends VGDLSprite
{
    public Missile(){}

    public Missile(Vector2d position, Dimension size, SpriteContent cnt)
    {
        this.init(position, size);

        //Specific class default parameter values.
        loadDefaults();

        //Parse the arguments.
        this.parseParameters(cnt);
    }

    protected void loadDefaults()
    {
        super.loadDefaults();
        speed = 1;
        is_oriented = true;
    }


    public VGDLSprite copy()
    {
        Missile newSprite = new Missile();
        this.copyTo(newSprite);
        return newSprite;
    }

    public void copyTo(VGDLSprite target)
    {
        Missile targetSprite = (Missile) target;
        super.copyTo(targetSprite);
    }
}
