package uk.ac.york.sepr4.ahod2.object;

/***
 * Used to display messages on StatsHUD
 */
public enum GameStage {
    SELECT_START {
        @Override
        public String getStageText() {
            return "Select starting node!!";
        }
    },
    SELECT {
        @Override
        public String getStageText() {
            return "Select next node!";
        }
    },
    LOADING {
        @Override
        public String getStageText() {
            return "Loading... Please wait!";
        }
    };

    public abstract String getStageText();
}
