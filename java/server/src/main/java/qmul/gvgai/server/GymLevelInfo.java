package qmul.gvgai.server;

import lombok.Data;

@Data
public class GymLevelInfo {
    private final String game;
    private final String lvl;

    private final String gameFileName;
    private String levelFileName = null;

    public GymLevelInfo(String game, String lvl) {
        this.game = game;
        this.lvl = lvl;

        var gameDirectory = game + "_v0";

        this.gameFileName = gameDirectory + "/" + game + ".txt";

        if(!this.lvl.equals("custom")) {
            this.levelFileName = gameDirectory + "/" + game + "_" + lvl + ".txt";
        }
    }
}
