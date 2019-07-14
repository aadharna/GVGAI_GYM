package qmul.gvgai.server;



import qmul.gvgai.server.protocol.Comm;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class PipeComm extends Comm {


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
        inputStream = new DataInputStream(client.getInputStream());
        outputStream = new DataOutputStream(client.getOutputStream());
    }

}


