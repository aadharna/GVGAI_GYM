package tracks.singlePlayer.simple.doNothing;

import qmul.gvgai.engine.core.game.StateObservation;
import qmul.gvgai.engine.core.player.AbstractPlayer;
import qmul.gvgai.engine.ontology.Types;
import qmul.gvgai.engine.ontology.Types.ACTIONS;
import qmul.gvgai.engine.tools.ElapsedCpuTimer;

public class Agent extends AbstractPlayer{


	/**
	 * initialize all variables for the agent
	 * @param stateObs Observation of the current state.
     * @param elapsedTimer Timer when the action returned is due.
	 */
	public Agent(StateObservation stateObs, ElapsedCpuTimer elapsedTimer){
	}
	
	/**
	 * return ACTION_NIL on every call to simulate doNothing player
	 * @param stateObs Observation of the current state.
     * @param elapsedTimer Timer when the action returned is due.
	 * @return 	ACTION_NIL all the time
	 */
	@Override
	public ACTIONS act(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {
		return Types.ACTIONS.ACTION_NIL;
	}
}
