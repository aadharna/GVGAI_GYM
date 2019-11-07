package qmul.gvgai.server.player;

import lombok.Data;

@Data
public class EnvironmentChoice {

    public static EnvironmentChoice END = new EnvironmentChoice("END", false, false, false);

    private final String environmentId;
    private final boolean includeSemanticData;
    private final boolean sendPixelInformation;
    private final boolean sendTileInformation;
    private String levelData;

    public EnvironmentChoice(String environmentId, boolean includeSemanticData, boolean sendPixelInformation, boolean sendTileInformation) {
        this.environmentId = environmentId;
        this.includeSemanticData = includeSemanticData;
        this.sendPixelInformation = sendPixelInformation;
        this.sendTileInformation = sendTileInformation;
    }

    public EnvironmentChoice(String environmentId, String levelData, boolean includeSemanticData, boolean sendPixelInformation, boolean sendTileInformation) {
        this.environmentId = environmentId;
        this.levelData = levelData;
        this.includeSemanticData = includeSemanticData;
        this.sendPixelInformation = sendPixelInformation;
        this.sendTileInformation = sendTileInformation;
    }
}
