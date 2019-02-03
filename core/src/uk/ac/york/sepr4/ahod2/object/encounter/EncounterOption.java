package uk.ac.york.sepr4.ahod2.object.encounter;

import lombok.Data;

@Data
public class EncounterOption {

    private String text;
    private Integer gold, supplies;
    private boolean battle = false;
    private Integer difficulty;

    public EncounterOption() {
        //json
    }

}
