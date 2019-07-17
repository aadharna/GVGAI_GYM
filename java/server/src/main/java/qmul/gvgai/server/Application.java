package qmul.gvgai.server;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "gvgai-server")
public class Application implements Runnable {

    @Option(names = {"-p", "--port"}, defaultValue = "8083", description = "The port number this server will listen on")
    private int port;

    public static void main(String... args) {
        var application = new CommandLine(new Application());
        var exitCode = application.execute(args);
        System.exit(exitCode);
    }

    @Override
    public void run() {
        var server = new LearningServer(port);
        server.start();
    }
}