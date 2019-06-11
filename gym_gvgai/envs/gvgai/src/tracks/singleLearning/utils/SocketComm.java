package tracks.singleLearning.utils;

/**
 * Created by Daniel on 05.04.2017.
 */

import core.competition.CompetitionParameters;
import core.game.SerializableStateObservation;
import ontology.Types;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketComm extends Comm {


    public int port = CompetitionParameters.SOCKET_PORT; //default
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private boolean end;
    private static int THRESHOLD = 60000;

    /**
     * Public constructor of the player.
     */
    public SocketComm(String portStr) {
        super();
        end = false;
        port = Integer.parseInt(portStr);
        initBuffers();
    }

    /**
     * Creates the buffers for pipe communication.
     */
    @Override
    public void initBuffers() {
        try {
            //Accepting the socket connection.
            while (socket == null) {
                ServerSocket serverSocket = new ServerSocket(port);
                serverSocket.setPerformancePreferences(0, 1, 2);

                socket = serverSocket.accept();
                socket.setTcpNoDelay(true);
            }


            //Initialize input and output through socket.
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

        } catch (java.net.BindException e) {
            System.out.println(e.toString());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void start() {
        try {
            initBuffers();

            while (!end) {

            }
            //out.format("Sending back: " + received);

            // may want to close this client side instead
            socket.close();
            System.out.println("Closed socket");
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a message through the pipe.
     *
     * @param data message to send.
     */
    public void commSend(Types.GAMESTATES gameState, byte[] data) throws IOException {
        out.writeLong(messageId);
        out.writeByte(gameState.ordinal());

        if(data != null) {
            out.writeInt(data.length);
            out.write(data);
        } else {
            out.writeInt(0);
        }
        out.flush();
        messageId++;
    }

    /**
     * Receives a message from the client.
     *
     * @return the response got from the client, or null if no response was received after due time.
     */
    public byte[] commRecv() {
        try {
            long receivedId = in.readLong();
            int ssoType = (int) in.readByte();
            int length = in.readInt();

            this.lastSsoType = Types.LEARNING_SSO_TYPE.values()[ssoType];

            if (receivedId == (messageId - 1)) {
                byte[] data = new byte[length];
                in.read(data, 0, length);
                return data;
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
