package common.util.stage.info;

import common.util.stage.Stage;

public interface StageInfo {
    String getHTML();

    boolean exConnection();

    Stage[] getExStages();

    float[] getExChances();
}

