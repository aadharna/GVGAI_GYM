package tracks.singlePlayer.simple.sampleonesteplookahead;


import tracks.singlePlayer.tools.Heuristics.SimpleStateHeuristic;
import qmul.gvgai.engine.core.game.StateObservation;
import qmul.gvgai.engine.core.player.AbstractPlayer;
import qmul.gvgai.engine.ontology.Types;
import qmul.gvgai.engine.tools.ElapsedCpuTimer;
import qmul.gvgai.engine.tools.Utils;

import java.util.Random;


public class Agent extends AbstractPlayer {

    public double epsilon = 1e-6;
    public Random m_rnd;

    public Agent(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {

        m_rnd = new Random();


    }

    /**
     *
     * Very simple one step lookahead agent.
     *
     * @param stateObs Observation of the current state.
     * @param elapsedTimer Timer when the action returned is due.
     * @return An action for the current state
     */
    public Types.ACTIONS act(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {

        Types.ACTIONS bestAction = null;
        double maxQ = Double.NEGATIVE_INFINITY;
        SimpleStateHeuristic heuristic =  new SimpleStateHeuristic(stateObs);
        for (Types.ACTIONS action : stateObs.getAvailableActions()) {

            StateObservation stCopy = stateObs.copy();
            stCopy.advance(action);
            double Q = heuristic.evaluateState(stCopy);
            Q = Utils.noise(Q, this.epsilon, this.m_rnd.nextDouble());

            //System.out.println("Action:" + action + " score:" + Q);
            if (Q > maxQ) {
                maxQ = Q;
                bestAction = action;
            }


        }

        //System.out.println("======== "  + maxQ + " " + bestAction + "============");
        return bestAction;



    }


}
