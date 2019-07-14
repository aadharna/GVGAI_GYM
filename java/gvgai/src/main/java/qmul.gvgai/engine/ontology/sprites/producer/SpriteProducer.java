package qmul.gvgai.engine.ontology.sprites.producer;

import java.awt.Dimension;

import qmul.gvgai.engine.core.vgdl.VGDLSprite;
import qmul.gvgai.engine.core.content.SpriteContent;
import qmul.gvgai.engine.tools.Vector2d;


public class SpriteProducer extends VGDLSprite
{
    public SpriteProducer(){}

    public SpriteProducer(Vector2d position, Dimension size, SpriteContent cnt)
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
    }


    public VGDLSprite copy()
    {
        SpriteProducer newSprite = new SpriteProducer();
        this.copyTo(newSprite);
        return newSprite;
    }

    public void copyTo(VGDLSprite target)
    {
        SpriteProducer targetSprite = (SpriteProducer) target;
        super.copyTo(targetSprite);
    }

}
