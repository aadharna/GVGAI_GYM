package qmul.gvgai.server.player;

import lombok.Data;

@Data
public class EnvironmentChoice {

    public static EnvironmentChoice END = new EnvironmentChoice("END");

    private final String environmentId;
    private String levelData;


    public EnvironmentChoice(String environmentId) {
        this.environmentId = environmentId;
    }

    public EnvironmentChoice(String environmentId, String levelData) {
        this.environmentId = environmentId;
        this.levelData = levelData;
    }
}
