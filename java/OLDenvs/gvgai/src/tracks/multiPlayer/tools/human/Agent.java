package tracks.multiPlayer.tools.human;

import qmul.gvgai.engine.core.game.Game;
import qmul.gvgai.engine.core.game.StateObservation;
import qmul.gvgai.engine.core.game.StateObservationMulti;
import qmul.gvgai.engine.core.player.AbstractMultiPlayer;
import qmul.gvgai.engine.ontology.Types;
import qmul.gvgai.engine.tools.Direction;
import qmul.gvgai.engine.tools.ElapsedCpuTimer;
import qmul.gvgai.engine.tools.Utils;

/**
 * Created by diego on 06/02/14.
 */
public class Agent extends AbstractMultiPlayer
{
    int id; //this player's ID

    /**
     * Public constructor with state observation and time due.
     * @param so state observation of the current game.
     * @param elapsedTimer Timer for the controller creation.
     * @param playerID ID if this agent
     */
    public Agent(StateObservationMulti so, ElapsedCpuTimer elapsedTimer, int playerID)
    {
        id = playerID;
    }


    /**
     * Picks an action. This function is called every game step to request an
     * action from the player.
     * @param stateObs Observation of the current state.
     * @param elapsedTimer Timer when the action returned is due.
     * @return An action for the current state
     */
    public Types.ACTIONS act(StateObservationMulti stateObs, ElapsedCpuTimer elapsedTimer)
    {
        //int id = (getPlayerID() + 1) % stateObs.getNoPlayers();
        Direction move = Utils.processMovementActionKeys(Game.ki.getMask(), id);
        boolean useOn = Utils.processUseKey(Game.ki.getMask(), id);

        //In the keycontroller, move has preference.
        Types.ACTIONS action = Types.ACTIONS.fromVector(move);
        if(action == Types.ACTIONS.ACTION_NIL && useOn)
            action = Types.ACTIONS.ACTION_USE;

        return action;
    }

    public void result(StateObservation stateObservation, ElapsedCpuTimer elapsedCpuTimer)
    {
        //System.out.println("Thanks for playing! " + stateObservation.isAvatarAlive());
    }
}
