package qmul.gvgai.server.player;

import lombok.Data;

@Data
public class EnvironmentChoice {

    public static EnvironmentChoice END = new EnvironmentChoice("END", false, false);

    private final String environmentId;
    private final boolean includeSemanticData;
    private final boolean oneHotObservation;
    private String levelData;


    public EnvironmentChoice(String environmentId, boolean includeSemanticData, boolean oneHotObservation) {
        this.environmentId = environmentId;
        this.includeSemanticData = includeSemanticData;
        this.oneHotObservation = oneHotObservation;
    }

    public EnvironmentChoice(String environmentId, String levelData, boolean includeSemanticData, boolean oneHotObservation) {
        this.environmentId = environmentId;
        this.levelData = levelData;
        this.includeSemanticData = includeSemanticData;
        this.oneHotObservation = oneHotObservation;
    }
}
