package qmul.gvgai.server;

import com.badlogic.gdx.graphics.Pixmap;
import lombok.extern.slf4j.Slf4j;
import qmul.gvgai.engine.core.game.Game;
import qmul.gvgai.engine.core.game.StateObservation;
import qmul.gvgai.engine.core.game.StateObservationMulti;
import qmul.gvgai.engine.core.game.serialization.FlatBufferStateObservation;
import qmul.gvgai.engine.core.player.Player;
import qmul.gvgai.engine.core.vgdl.VGDLRenderer;
import qmul.gvgai.engine.core.vgdl.VGDLViewer;
import qmul.gvgai.engine.ontology.Types;
import qmul.gvgai.engine.tools.ElapsedCpuTimer;
import qmul.gvgai.server.protocol.Comm;
import qmul.gvgai.server.protocol.Message;
import qmul.gvgai.server.protocol.SocketComm;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;


@Slf4j
public class LearningPlayer extends Player {

    /**
     * Server communication channel
     */
    private Comm comm;
    private String currentLevel;
    private boolean includeSemanticData = false;

    private VGDLRenderer renderer = null;

    /**
     * Learning Player constructor.
     * Creates a new server side communication channel for every player.
     */
    public LearningPlayer(int port) {
        this.comm = new SocketComm(port);

    }

    public Types.ACTIONS act() {
        return null;
    }

    /**
     * Picks an action. This function is called every game step to request an
     * action from the player. The action returned must be contained in the
     * actions accessible from stateObs.getAvailableActions(), or action NIL
     * will be applied.
     *
     * @param so           Observation of the current state.
     * @param elapsedTimer Timer when the action returned is due.
     * @return An action for the current state
     */
    @Override
    public Types.ACTIONS act(StateObservation so, ElapsedCpuTimer elapsedTimer) {

        log.debug("ACT");

        // Sending messages.
        try {
            renderer.paintFrameBuffer();
            FlatBufferStateObservation fbso = new FlatBufferStateObservation(so, includeSemanticData, renderer.getBuffer());

            Message message = new Message(Types.GamePhase.ACT_STATE.ordinal(), fbso.serialize());
            comm.commSend(message);

            so.currentGameState = Types.GamePhase.ACT_STATE;

            Message response = comm.commRecv();

            if (response.phase != Types.AgentPhase.ACT_STATE.ordinal()) {
                log.debug("Client not sending ACT phase message. Aborting game [{}]", response.phase);
                return Types.ACTIONS.ACTION_ESCAPE;
            }

            return Types.ACTIONS.fromInt(ByteBuffer.wrap(response.data).getInt());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * @param game the game that
     * @return true if initialization is OK
     */
    public boolean init(Game game) {

        log.debug("INIT");

        var so = game.getObservation();

        renderer = new VGDLRenderer(game);
        renderer.paintFrameBuffer();

        //Sending messages.
        try {
            // Set the game state to the appropriate state and the millisecond counter, then send the serialized observation.
            so.currentGameState = Types.GamePhase.INIT_STATE;
            FlatBufferStateObservation sso = new FlatBufferStateObservation(so, includeSemanticData, renderer.getBuffer());

            Message message = new Message(Types.GamePhase.INIT_STATE.ordinal(), sso.serialize());

            comm.commSend(message);
            Message response = comm.commRecv();

            return Types.AgentPhase.INIT_STATE.ordinal() == response.phase;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public String chooseLevel() {

        log.debug("CHOOSE LEVEL");

        try {

            Message message = new Message(Types.GamePhase.CHOOSE_ENVIRONMENT_STATE.ordinal());

            comm.commSend(message);
            Message response = comm.commRecv();

            if (Types.AgentPhase.CHOOSE_ENVIRONMENT_STATE.ordinal() == response.phase) {
                DataInputStream data = new DataInputStream(new ByteArrayInputStream(response.data));

                int levelLength = data.readInt();
                this.currentLevel = new String(data.readNBytes(levelLength), StandardCharsets.UTF_8);

                log.debug("Level chosen: [{}]", this.currentLevel);
                return currentLevel;
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // No level chosen or an error. End
        return "END";
    }

    @Override
    public Types.ACTIONS act(StateObservationMulti stateObs, ElapsedCpuTimer elapsedTimer) {
        return null;
    }

    /**
     * Function called when the game is over. This method must finish before CompetitionParameters.TEAR_DOWN_TIME,
     * or the agent will be DISQUALIFIED
     *
     * @param stateObs the game state at the end of the game
     * @returns Level to be plated.
     */
    public int endGame(StateObservation stateObs) {
        return this.comm.endGame(stateObs);
    }

    /**
     * Starts the communication between the server and the client.
     *
     * @return true or false, depending on whether the initialization has been successful
     */
    public boolean startPlayerCommunication() {
        return this.comm.startComm();
    }

    /**
     * Tells the client that this is over. Se finito.
     */
    public boolean finishPlayerCommunication() {
        return this.comm.endComm();
    }

}