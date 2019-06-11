package tracks.singleLearning.utils;

/**
 * Created by Daniel on 05.04.2017.
 */

import ontology.Types;

import java.io.*;

public class PipeComm extends Comm {

    /**
     * Reader of the player. Will read actions from the client.
     */
    public static DataInputStream input;

    /**
     * Writer of the player. Used to pass the client the state view information.
     */
    public static DataOutputStream output;

    /**
     * Client process
     */
    private Process client;


    /**
     * Public constructor of the player.
     * @param client process that runs the agent.
     */
    public PipeComm(Process client) {
        super();
        this.client = client;
        initBuffers();
    }

    /**
     * Creates the buffers for pipe communication.
     */
    @Override
    public void initBuffers() {
        input = new DataInputStream(client.getInputStream());
        output = new DataOutputStream(client.getOutputStream());
    }


    /**
     * Sends a message through the pipe.
     *
     * @param data message to send.
     */
    public void commSend(Types.GAMESTATES gameState, byte[] data) throws IOException {
        output.writeLong(messageId);
        output.writeByte(gameState.ordinal());
        output.writeInt(data.length);
        output.write(data);
        output.flush();
        messageId++;
    }

    /**
     * Receives a message from the client.
     *
     * @return the response got from the client, or null if no response was received after due time.
     */
    public byte[] commRecv() {
        try {
            long receivedId = input.readLong();
            int ssoType = (int) input.readByte();
            int length = input.readInt();

            this.lastSsoType = Types.LEARNING_SSO_TYPE.values()[ssoType];

            if (receivedId == (messageId - 1)) {
                byte[] flatBufferData = new byte[length];
                input.read(flatBufferData, 0, length);
                return flatBufferData;
            } else if (receivedId < (messageId - 1)) {
                //Previous message, ignore and keep waiting.
                return commRecv();
            } else {
                //A message from the future? Ignore and return null;
                System.err.println("SocketComm: commRecv: Communication Error! A message from the future!");
                return null;
            }

        } catch (IOException e) {
            System.err.println("SocketComm: commRecv: Communication Error!" + e.getMessage());
            return null;
        }
    }

}


