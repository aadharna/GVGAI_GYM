package qmul.gvgai.server;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
public class GymLevelInfo {
    private final String game;
    private final String lvl;
    private final String version;

    private final String gameFile;
    private final String levelFile;

    public GymLevelInfo(String game, String lvl, String version) {
        this.game = game;
        this.lvl = lvl;
        this.version = version;

        var gameDirectory = game + "_" + version;

        this.gameFile = gameDirectory + "/" + game + ".txt";
        this.levelFile = gameDirectory + "/" + game + "_" + lvl + ".txt";
    }
}
