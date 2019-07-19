package qmul.gvgai.server;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
public class GymLevelInfo {
    private final String game;
    private final String lvl;

    private final String gameFile;
    private final String levelFile;

    public GymLevelInfo(String game, String lvl) {
        this.game = game;
        this.lvl = lvl;

        var gameDirectory = game + "_v0";

        this.gameFile = gameDirectory + "/" + game + ".txt";
        this.levelFile = gameDirectory + "/" + game + "_" + lvl + ".txt";
    }
}
