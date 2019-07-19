package qmul.gvgai.server;

import lombok.extern.slf4j.Slf4j;
import qmul.gvgai.engine.core.game.Game;
import qmul.gvgai.engine.core.game.StateObservation;
import qmul.gvgai.engine.core.vgdl.VGDLFactory;
import qmul.gvgai.engine.core.vgdl.VGDLParser;
import qmul.gvgai.engine.core.vgdl.VGDLRegistry;

import java.io.IOException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Slf4j
public class LearningServer {

    private final int port;

    public LearningServer(int port) {
        this.port = port;
    }

    private static final Pattern levelPattern = Pattern.compile("(?<game>.*)-(?<level>lvl\\d+)");

    public void start() {

        log.debug("Starting GVGAI server on port [{}]", port);
        VGDLFactory.GetInstance().init();
        VGDLRegistry.GetInstance().init();

        // Player array to hold the single player
        var player = new LearningPlayer(port);

        // Wait for the player to initialize a connection
        boolean initSuccesful = player.startPlayerCommunication();
        if (!initSuccesful) {
            return;
        }

        String level = player.chooseLevel();

        while (!level.equals("END")) {
            playOneLevel(level, player);
        }

        player.finishPlayerCommunication();
    }

    public static void playOneLevel(String level, LearningPlayer player) {

        log.debug("Starting level [{}]", level);

        // Create a new random seed for the next level.
        int randomSeed = new Random().nextInt();

        var environmentInfo = parseLevelName(level);

        assert environmentInfo != null;

        Game toPlay = new VGDLParser().parseGame(environmentInfo.getGameFile());
        toPlay.buildLevel(environmentInfo.getLevelFile(), randomSeed);

        // Initialize the new learningPlayer instance.
        boolean initialized = LearningServer.initPlayer(player, randomSeed, toPlay);

        toPlay.playOnlineGame(player, randomSeed, false, 0);

        //Finally, when the game is over, we need to tear the player down.
        LearningServer.tearPlayerDown(player, toPlay);

        StateObservation so = toPlay.getObservation();

        log.debug("Ending level [{}]", level);
        player.endGame(so);

        //reset the game.
        toPlay.reset();
    }

    /**
     * Inits the player for a given game.
     *
     * @param player     Player to start.
     * @param randomSeed Seed for the sampleRandom generator of the game to be played.
     * @return true if the player is initialized OK
     */
    private static boolean initPlayer(LearningPlayer player, int randomSeed, Game toPlay) {
        //If we have a player, set it up for action recording.
        player.setup(randomSeed, false);

        //Send Init message.
        return player.init(toPlay);
    }

    /**
     * Tears the player down. This initiates the saving of actions to file.
     * It should be called when the game played is over.
     *
     * @param player player to be closed.
     */
    private static void tearPlayerDown(LearningPlayer player, Game toPlay) {
        player.teardown(toPlay);
    }

    private static GymLevelInfo parseLevelName(String level) {

        Matcher m = levelPattern.matcher(level);
        if (m.find()) {
            String game = m.group("game");
            String lvl = m.group("level");

            return new GymLevelInfo(game, lvl);
        }

        return null;
    }

}