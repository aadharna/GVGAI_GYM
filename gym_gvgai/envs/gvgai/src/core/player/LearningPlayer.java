package core.player;

import core.competition.CompetitionParameters;
import core.game.SerializableStateObservation;
import core.game.StateObservation;
import core.game.StateObservationMulti;
import core.game.serialization.FlatBufferStateObservation;
import ontology.Types;
import tools.ElapsedCpuTimer;
import tracks.singleLearning.utils.Comm;
import tracks.singleLearning.utils.Comm.Message;
import tracks.singleLearning.utils.PipeComm;
import tracks.singleLearning.utils.SocketComm;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;


/**
 * Created by Daniel on 07.03.2017.
 */
public class LearningPlayer extends Player {

    /**
     * Server communication channel
     */
    private Comm comm;

    /**
     * Fast way to store and serialize image data for learning agents
     */
    private byte[] observationBuffer;

    public byte[] getObservationBuffer() {
        return observationBuffer;
    }

    public void setObservationBuffer(byte[] observationBuffer) {
        this.observationBuffer = observationBuffer;
    }


    public boolean isRequiresImage() {
        return requiresImage;
    }

    public void setRequiresImage(boolean requiresImage) {
        this.requiresImage = requiresImage;
    }

    private boolean requiresImage;

    /**
     * Learning Player constructor.
     * Creates a new server side communication channel for every player.
     */
    public LearningPlayer(Process proc, String port) {
        if (CompetitionParameters.USE_SOCKETS) {
            //Sockets:
            this.comm = new SocketComm(port);
        }
        //Else: pipes
        else {
            this.comm = new PipeComm(proc);
        }

    }


    public Types.ACTIONS act(SerializableStateObservation stateObs, ElapsedCpuTimer elapsedTimer) {
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
        // Sending messages.
        try {

            FlatBufferStateObservation fbso = new FlatBufferStateObservation(so, getObservationBuffer());

            Message message = new Message(Types.GamePhase.ACT_STATE.ordinal(), fbso.serialize());
            comm.commSend(message);

            so.currentGameState = Types.GamePhase.ACT_STATE;

            Message response = comm.commRecv();

            if (response.phase != Types.AgentPhase.ACT_STATE.ordinal()) {
                return Types.ACTIONS.ACTION_ESCAPE;
            }

            return Types.ACTIONS.fromInt(ByteBuffer.wrap(response.data).getInt());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /***
     * @param so           State observation of the current game in its initial state
     * @param isValidation true if the level to play is a validation one.
     * @return true if Init worked.
     */
    public boolean init(StateObservation so, boolean isValidation) {
        //Sending messages.
        try {
            // Set the game state to the appropriate state and the millisecond counter, then send the serialized observation.
            so.currentGameState = Types.GamePhase.INIT_STATE;
            FlatBufferStateObservation sso = new FlatBufferStateObservation(so, getObservationBuffer());
            sso.isValidation = isValidation;

            Message message = new Message(Types.GamePhase.INIT_STATE.ordinal(), sso.serialize());

            comm.commSend(message);
            Message response = comm.commRecv();

            return Types.AgentPhase.INIT_STATE.ordinal() == response.phase;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public int chooseLevel() {
        try {

            Message message = new Message(Types.GamePhase.CHOOSE_LEVEL_STATE.ordinal());

            comm.commSend(message);
            Message response = comm.commRecv();

            if (Types.AgentPhase.CHOOSE_LEVEL_STATE.ordinal() == response.phase) {
                DataInputStream data = new DataInputStream(new ByteArrayInputStream(response.data));

                int level = data.readInt();
                this.requiresImage = data.readBoolean();

                return level;
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // No level chosen.. what to do here?
        return -1;
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
    public int result(StateObservation stateObs) {
        int result = this.comm.finishGame(stateObs);
//        System.out.println("Client replied: " + result);
        return result;
    }

    /**
     * Starts the communication between the server and the client.
     *
     * @return true or false, depending on whether the initialization has been successful
     */
    public boolean startPlayerCommunication() {

        //Initialize the controller.
        if (!this.comm.startComm())
            return false;

        return true;
    }

    /**
     * Tells the client that this is over. Se finito.
     */
    public boolean finishPlayerCommunication() {

        //Initialize the controller.
        if (!this.comm.endComm())
            return false;

        return true;
    }

    /**
     * Fast method for serializing observation data
     * writes a raw bitmap to memory to be sent when data is next serialized to the player
     */
    public void setObservation(BufferedImage imageBytes) {
        DataBufferInt imageBufferData = ((DataBufferInt) imageBytes.getData().getDataBuffer());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int[] data = imageBufferData.getData();


        for (int i = 0; i < imageBufferData.getSize(); i++) {
            int d = data[i];
            // ignore the 4th byte
            baos.write((d >> 16) & 0xff);
            baos.write((d >> 8) & 0xff);
            baos.write((d) & 0xff);
        }

        this.observationBuffer = baos.toByteArray();
    }
}