package tracks.singleLearning.utils;



import qmul.gvgai.engine.ontology.Types;

import java.io.*;

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


