package qmul.gvgai.server;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "gvgai-server")
public class Application implements Runnable {

    @Option(names= {"-i", "--input-dir"}, defaultValue = "./games", description="A directory where additional games are stored")
    private String gameDir;

    @Option(names = {"-p", "--port"}, defaultValue = "8083", description = "The port number this server will listen on")
    private int port;

    @Option(names = {"-l", "--log-level"}, defaultValue = "INFO", description = "The default logging level")
    private String logLevel;

    public static void main(String... args) {
        var application = new CommandLine(new Application());
        var exitCode = application.execute(args);
        System.exit(exitCode);
    }

    @Override
    public void run() {

        Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.toLevel(logLevel));

        var server = new LearningServer(port, gameDir);
        server.start();
    }
}