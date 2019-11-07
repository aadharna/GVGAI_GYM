package qmul.gvgai.server;

import lombok.Data;

import java.nio.file.Path;

@Data
public class GymLevelInfo {
    private final String game;
    private final String lvl;

    private final String gameFileName;
    private String levelFileName = null;

    public GymLevelInfo(String gameDirectory, String game, String lvl) {
        this.game = game;
        this.lvl = lvl;

        var fullGameDirectory = Path.of(gameDirectory,game).toString() + "_v0";

        this.gameFileName = Path.of(fullGameDirectory, game + ".txt").toString();

        if(!this.lvl.equals("custom")) {
            this.levelFileName = Path.of(fullGameDirectory, game + "_" + lvl + ".txt").toString();
        }
    }
}
