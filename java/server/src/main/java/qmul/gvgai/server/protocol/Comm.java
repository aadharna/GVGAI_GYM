package qmul.gvgai.server.protocol;

import qmul.gvgai.engine.core.game.StateObservation;
import qmul.gvgai.engine.core.game.serialization.FlatBufferStateObservation;
import qmul.gvgai.engine.ontology.Types;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


public abstract class Comm {

    /**
     * Message ID
     */
    protected long messageId;

    protected DataInputStream inputStream;

    protected DataOutputStream outputStream;

    /**
     * Default constructor
     */
    public Comm() {
        this.messageId = 0;
    }


    /***
     * This method is used to set the game state to either "ABORT_STATE" or "END_STATE"
     * depending on the termination of the game. Each game calls this method upon teardown
     * of the player object.
     *
     * END_STATE: Game ends normally
     * ABORT_STATE: Game is violently ended by player using "ABORT" message or ACTION_ESCAPE key
     *
     * @param so State observation of the game in progress to be used for message sending.
     * @return 1 if OK and -1 if not
     */
    public int endGame(StateObservation so) {

        try {
            // Set the game state to the appropriate state and the millisecond counter, then send the serialized observation.
            if (so.getAvatarLastAction() == Types.ACTIONS.ACTION_ESCAPE) {
                so.currentGameState = Types.GamePhase.ABORT_STATE;
            } else {
                so.currentGameState = Types.GamePhase.END_STATE;
            }

            FlatBufferStateObservation sso = new FlatBufferStateObservation(so, false, null);

            Message message = new Message(Types.GamePhase.END_STATE.ordinal(), sso.serialize());
            commSend(message);

            Message response = commRecv();

            if(response.phase == Types.AgentPhase.END_STATE.ordinal()) {
                return 1;
            }

        } catch (Exception e) {
            System.out.println("Error sending results to the client:");
            e.printStackTrace();
        }
        return -1;
    }


    /**
     * This function is called at the beginning of the game for
     * initialization.
     */
    public boolean startComm() {

        try {

            Message message = new Message((byte)Types.GamePhase.START_STATE.ordinal());
            commSend(message);

            Message response = commRecv();

            if (response.phase == (byte)Types.AgentPhase.START_STATE.ordinal()) {
                return true;
            } else {
                System.out.println("START_FAILED");
                return false;
            }

        } catch (IOException e) {
            System.out.println("Communication failed for unknown reason, could not play any games :-( ");
            throw new RuntimeException(e);
        }

    }


    /**
     * This function is called at the end of the whole process. Closes the communication.
     * Will give up if no "START_DONE" received after having received 11 responses
     */
    public boolean endComm() {

        try {

            //Send the finish message and don't wait for any response.
            Message message = new Message((byte)Types.GamePhase.END_STATE.ordinal());
            commSend(message);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    /**
     * Creates the buffers for communication.
     */
    public abstract void initBuffers();

    /**
     * Receives a message from the client.
     *
     * @return the response got from the client, or null if no response was received after due time.
     */
    public Message commRecv() {
        try {

            long receivedId = inputStream.readLong();
            byte agentPhase = inputStream.readByte();
            int length = inputStream.readInt();

            //if (receivedId == (messageId - 1)) {
                byte[] data = new byte[length];
                inputStream.read(data, 0, length);
                return new Message(agentPhase, data);
//            } else {
//                //A message from the future? Ignore and return null;
//                System.err.println("commRecv: Communication Error! Message received out of order!");
//                return null;
//            }

        } catch (IOException e) {
            System.err.println("commRecv: Communication Error!" + e.getMessage());
            return null;
        }
    }

    /**
     * Sends a message through the pipe.
     */
    public void commSend(Message message) throws IOException {
        outputStream.writeLong(messageId);
        outputStream.writeByte(message.phase);

        byte[] data = message.data;

        if(data != null) {
            outputStream.writeInt(data.length);
            outputStream.write(data);
        } else {
            outputStream.writeInt(0);
        }
        outputStream.flush();
        messageId++;
    }

}
