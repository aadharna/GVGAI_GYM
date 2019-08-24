package qmul.gvgai.engine.ontology.physics;

import java.awt.Rectangle;

import qmul.gvgai.engine.core.vgdl.VGDLSprite;
import qmul.gvgai.engine.ontology.Types;
import qmul.gvgai.engine.tools.Direction;


public interface Physics
{
    public Types.MOVEMENT passiveMovement(VGDLSprite sprite);
    public Types.MOVEMENT activeMovement(VGDLSprite sprite, Direction action, double speed);
    public double distance(Rectangle r1, Rectangle r2);
}
