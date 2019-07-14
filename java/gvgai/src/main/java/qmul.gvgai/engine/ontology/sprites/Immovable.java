package qmul.gvgai.engine.ontology.sprites;

import com.badlogic.gdx.graphics.Color;
import qmul.gvgai.engine.core.content.SpriteContent;
import qmul.gvgai.engine.core.vgdl.VGDLSprite;
import qmul.gvgai.engine.tools.Vector2d;

import java.awt.Dimension;


public class Immovable extends VGDLSprite {
    public Immovable() {
    }

    public Immovable(Vector2d position, Dimension size, SpriteContent cnt) {
        //Init the sprite
        this.init(position, size);

        //Specific class default parameter values.
        loadDefaults();

        //Parse the arguments.
        this.parseParameters(cnt);
    }

    public void postProcess() {
        super.postProcess();
    }


    protected void loadDefaults() {
        super.loadDefaults();
        color = Color.GRAY;
        is_static = true;
    }


    public VGDLSprite copy() {
        Immovable newSprite = new Immovable();
        this.copyTo(newSprite);
        return newSprite;
    }

    public void copyTo(VGDLSprite target) {
        Immovable targetSprite = (Immovable) target;
        super.copyTo(targetSprite);
    }
}
