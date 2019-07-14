package tracks.multiPlayer.tools.heuristics;

import qmul.gvgai.engine.core.game.StateObservationMulti;
import qmul.gvgai.engine.ontology.Types;


public class WinScoreHeuristic extends StateHeuristicMulti {

    private static final double HUGE_NEGATIVE = -1000.0;
    private static final double HUGE_POSITIVE =  1000.0;

    double initialNpcCounter = 0;

    public WinScoreHeuristic(StateObservationMulti stateObs) {

    }

    public double evaluateState(StateObservationMulti stateObs, int playerID) {
        boolean gameOver = stateObs.isGameOver();
        Types.WINNER win = stateObs.getMultiGameWinner()[playerID];
        Types.WINNER oppWin = stateObs.getMultiGameWinner()[(playerID + 1) % stateObs.getNoPlayers()];
        double rawScore = stateObs.getGameScore(playerID);

        if(gameOver && (win == Types.WINNER.PLAYER_LOSES || oppWin == Types.WINNER.PLAYER_WINS))
            return HUGE_NEGATIVE;

        if(gameOver && (win == Types.WINNER.PLAYER_WINS || oppWin == Types.WINNER.PLAYER_LOSES))
            return HUGE_POSITIVE;

        return rawScore;
    }


}


