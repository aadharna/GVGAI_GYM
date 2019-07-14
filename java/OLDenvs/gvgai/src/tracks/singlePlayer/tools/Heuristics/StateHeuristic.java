package tracks.singlePlayer.tools.Heuristics;

import qmul.gvgai.engine.core.game.StateObservation;


public abstract class StateHeuristic {

    abstract public double evaluateState(StateObservation stateObs);
}
