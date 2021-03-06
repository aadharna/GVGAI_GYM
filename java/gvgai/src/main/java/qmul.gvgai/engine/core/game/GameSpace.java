package qmul.gvgai.engine.core.game;

import qmul.gvgai.engine.core.content.GameContent;
import qmul.gvgai.engine.core.content.ParameterContent;

import java.util.HashMap;


public class GameSpace extends BasicGame {

    /**
     * Default constructor for a basic game.
     *
     * @param content Contains parameters for the game.
     */
    public GameSpace(GameContent content) {
        super(content);
        parameters = new HashMap<>();
    }


    /**
     * Builds a level, receiving a file name.
     * @param gamelvl file name containing the level.
     */
    public void buildLevel(String gamelvl, int randomSeed){

        //Need to extract my parameters here.
        super.buildLevel(gamelvl, randomSeed);
    }

    public void addParameterContent(ParameterContent pc)
    {
        if(parameters == null)
            parameters = new HashMap<>();
        parameters.put(pc.identifier, pc);
    }

    public HashMap<String, ParameterContent> getParameters()
    {
        return parameters;
    }
}
