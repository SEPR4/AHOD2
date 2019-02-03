package uk.ac.york.sepr4.ahod2.object.encounter;


import lombok.Getter;

import java.util.List;

@Getter
public class Encounter {

    private Integer id;
    private String name, text;
    private List<EncounterOption> options;
    private double chance;

    public Encounter() {
        //json
    }

}
