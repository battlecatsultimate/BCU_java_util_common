package common.util.stage.info;

import common.util.stage.Stage;

public interface StageInfo {
    boolean hasExConnection();

    Stage[] getExStages();

    /**
     * chance for which ex stage to get
     */
    float[] getExChances();

    /**
     * chance for any ex stage
     */
    int getExChance();

    int getExMapId();

    int getExStageIdMin();

    int getExStageIdMax();

    Stage getStage();

    int getEnergy();

    int getXp();

    int[][] getDrop();

    int[][] getTime();
}

