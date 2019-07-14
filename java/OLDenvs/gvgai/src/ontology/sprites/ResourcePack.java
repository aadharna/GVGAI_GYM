package qmul.gvgai.engine.ontology.sprites;

import java.awt.Dimension;

import qmul.gvgai.engine.core.vgdl.VGDLSprite;
import qmul.gvgai.engine.core.content.SpriteContent;
import qmul.gvgai.engine.tools.Vector2d;


public class ResourcePack extends Resource
{
    public ResourcePack(){}

    public ResourcePack(Vector2d position, Dimension size, SpriteContent cnt)
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
    }


    public VGDLSprite copy()
    {
        ResourcePack newSprite = new ResourcePack();
        super.copyTo(newSprite);
        return newSprite;
    }
}
