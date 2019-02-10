package ahod2.object.encounter;


import lombok.Getter;

import java.util.List;

@Getter
public class Encounter {

    private Integer id;
    private String name, text;
    private List<EncounterOption> options;
    private double chance;
    private String background = "default.png";

    public Encounter() {
        //json
    }

}
