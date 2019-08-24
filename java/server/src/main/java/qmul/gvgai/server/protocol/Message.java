package qmul.gvgai.server.protocol;

public class Message {
    public byte phase;
    public byte[] data;

    public Message(int phase, byte[] data) {
        this.phase = (byte)phase;
        this.data = data;
    }

    public Message(int phase) {
        this.phase = (byte)phase;
    }
}