package tracks.multiPlayer.tools.heuristics;

import qmul.gvgai.engine.core.game.StateObservationMulti;


public abstract class StateHeuristicMulti {

    abstract public double evaluateState(StateObservationMulti stateObs, int playerID);
}
