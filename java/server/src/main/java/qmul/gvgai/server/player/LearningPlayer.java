package qmul.gvgai.server.player;

import lombok.extern.slf4j.Slf4j;
import qmul.gvgai.engine.core.game.Game;
import qmul.gvgai.engine.core.game.StateObservation;
import qmul.gvgai.engine.core.game.StateObservationMulti;
import qmul.gvgai.engine.core.game.serialization.FlatBufferStateObservation;
import qmul.gvgai.engine.core.player.Player;
import qmul.gvgai.engine.core.vgdl.VGDLRenderer;
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

    @Override
    public void observe(StateObservation so) {

        log.debug("OBSERVE");

        try {
            renderer.paintFrameBuffer();
            FlatBufferStateObservation fbso = new FlatBufferStateObservation(so, includeSemanticData, renderer.getBuffer());

            Message message = new Message(Types.AgentPhase.OBSERVE_STATE.ordinal(), fbso.serialize());
            comm.commSend(message);

            Message response = comm.commRecv();

            if (response.phase != Types.AgentPhase.OBSERVE_STATE.ordinal()) {
                log.debug("Client not sending ACT phase message. Aborting game [{}]", response.phase);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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

            Message message = new Message(Types.AgentPhase.ACT_STATE.ordinal());
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

            Message message = new Message(Types.AgentPhase.INIT_STATE.ordinal(), sso.serialize());

            comm.commSend(message);
            Message response = comm.commRecv();

            return Types.AgentPhase.INIT_STATE.ordinal() == response.phase;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public EnvironmentChoice chooseEnvironment() {

        log.debug("CHOOSE ENVIRONMENT");

        try {

            Message message = new Message(Types.AgentPhase.CHOOSE_ENVIRONMENT_STATE.ordinal());

            comm.commSend(message);
            Message response = comm.commRecv();

            if (Types.AgentPhase.CHOOSE_ENVIRONMENT_STATE.ordinal() == response.phase) {
                DataInputStream data = new DataInputStream(new ByteArrayInputStream(response.data));

                int environmentIdLength = data.readInt();
                String environmentId = new String(data.readNBytes(environmentIdLength), StandardCharsets.UTF_8);

                log.debug("Environment chosen: [{}]", environmentId);
                int levelDataLength = data.readInt();

                if (levelDataLength > 0) {
                    String levelData = new String(data.readNBytes(levelDataLength), StandardCharsets.UTF_8);
                    log.debug("Custom Level Data [{}]", levelData);

                    return new EnvironmentChoice(environmentId, levelData);
                }

                return new EnvironmentChoice(environmentId);
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // No level chosen or an error. End
        return EnvironmentChoice.END;
    }

    @Override
    public Types.ACTIONS act(StateObservationMulti stateObs, ElapsedCpuTimer elapsedTimer) {
        return null;
    }

    /**
     * Ends communication with client
     *
     * @param stateObs
     * @return
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

    @Override
    public void teardown(Game played) {
        super.teardown(played);

        renderer.dispose();
    }
}