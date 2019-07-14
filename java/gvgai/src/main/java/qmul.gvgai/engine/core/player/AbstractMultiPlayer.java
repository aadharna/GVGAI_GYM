package qmul.gvgai.engine.core.player;

import qmul.gvgai.engine.core.game.StateObservation;
import qmul.gvgai.engine.ontology.Types;
import qmul.gvgai.engine.tools.ElapsedCpuTimer;



/**
 * Subclass of Player for multi player games.
 * Implements single players act method, returns NULL.
 * Keeps track of playerID and disqualification flag.
 */

public abstract class AbstractMultiPlayer extends Player {

    /**
     * Picks an action. This function is called every game step to request an
     * action from the player. The action returned must be contained in the
     * actions accessible from stateObs.getAvailableActions(), or no action
     * will be applied.
     * @param stateObs Observation of the current state.
     * @param elapsedTimer Timer when the action returned is due.
     * @return An action for the current state.
     */
    public Types.ACTIONS act(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {
        return Types.ACTIONS.ACTION_NIL;
    }
}
